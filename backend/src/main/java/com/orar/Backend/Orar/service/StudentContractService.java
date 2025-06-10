package com.orar.Backend.Orar.service;
import com.orar.Backend.Orar.model.MateriiOptionale;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.enums.MaterieStatus;
import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.exception.ValidationException;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.model.Contract;
import com.orar.Backend.Orar.model.CurriculumEntry;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.ContractRepository;
import com.orar.Backend.Orar.repository.CurriculumEntryRepository;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.MateriiOptionaleRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Transactional
public class StudentContractService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CatalogStudentMaterieRepository catalogRepo;

    @Autowired
    private MaterieRepository materieRepository;

    @Autowired
    private CurriculumEntryRepository curriculumEntryRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private MateriiOptionaleRepository optionaleRepo;

    public List<ContractDTO> getAvailableCoursesForContract(
            String studentCod,
            int anContract,
            int semestruRel  // 1 sau 2
    ) {
        // 0) Încărcăm studentul și validăm
        Student student = studentRepository.findByCod(studentCod)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        int specIdCurent = student.getSpecializare().getId();
        int studentYear  = student.getAn();
        if (anContract > studentYear) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Nu poți genera contract pentru anul " + anContract +
                            " deoarece ești în prezent în anul " + studentYear + "."
            );
        }

        // 1) Calculăm semestrul absolut (1,2 → an1; 3,4 → an2; 5,6 → an3 etc)
        int semestruAbsolut = (anContract - 1) * 2 + semestruRel;

        List<CatalogStudentMaterie> istoricul = catalogRepo.findByStudentCod(studentCod);
        istoricul.stream()
                .filter(c -> c.getStatus() == MaterieStatus.ACTIV && c.getSemestru() < semestruAbsolut)
                .forEach(c -> {
                    c.setStatus(MaterieStatus.FINALIZATA);
                    catalogRepo.save(c);
                });

        // 2) Materialele obligatorii/opționale/facultative pentru anul & semestrul curent
        List<CurriculumEntry> ownEntriesThisSem = curriculumEntryRepository
                .findBySpecializareIdAndAnAndSemestru(specIdCurent, anContract, semestruAbsolut);

        // 3) Materialele obligatorii din alte specializări (pentru același an & semestru)
        List<CurriculumEntry> externalObligatoryEntries = curriculumEntryRepository
                .findByAnAndSemestruAndTip(anContract, semestruAbsolut, Tip.OBLIGATORIE)
                .stream()
                .filter(e -> e.getSpecializare().getId() != specIdCurent)
                .toList();

        // 4) Statusurile (ACTIV/FINALIZATĂ) din catalogul studentului
        Map<String, MaterieStatus> statusMap = catalogRepo
                .findByStudentCod(studentCod)
                .stream()
                .collect(Collectors.toMap(
                        c -> c.getMaterie().getCod(),
                        CatalogStudentMaterie::getStatus,
                        (first, second) -> first
                ));

        // 5) Mapare proprie SPECIALIZARE → ContractDTO (cu materiiOptionaleId, dacă există)
        List<ContractDTO> ownDTOs = ownEntriesThisSem.stream()
                .map(e -> {
                    Materie m = e.getMaterie();
                    boolean esteObligatorie = (e.getTip() == Tip.OBLIGATORIE);
                    Integer materiiOptionaleId = null;
                    if (e.getOptionale() != null) {
                        materiiOptionaleId = e.getOptionale().getId();
                    }
                    return new ContractDTO(
                            m.getCod(),
                            m.getNume(),
                            m.getCredite(),
                            semestruRel,        // relativ (1 sau 2)
                            e.getTip(),         // OBLIGATORIE / OPTIONALA / FACULTATIVA
                            esteObligatorie,
                            materiiOptionaleId
                    );
                })
                .toList();

        // 6) Mapare „opționale externe” din alte specializări (le afișăm la fel ca OPTIONALA, fără pachet)
        List<ContractDTO> externalDTOs = externalObligatoryEntries.stream()
                .map(e -> {
                    Materie m = e.getMaterie();
                    // chiar dacă în DB e OBLIGATORIE pentru cealaltă specializare,
                    // noi o afișăm ca TIP=OPTIONALA (pentru a permite înscrierea).
                    Integer materiiOptionaleId = null;
                    if (e.getOptionale() != null) {
                        materiiOptionaleId = e.getOptionale().getId();
                    }
                    return new ContractDTO(
                            m.getCod(),
                            m.getNume(),
                            m.getCredite(),
                            semestruRel,     // tot ca semestru relativ
                            Tip.OPTIONALA,   // forțăm să apară OPTIONALA
                            false,           // nu e obligatorie pentru acest student
                            materiiOptionaleId
                    );
                })
                .toList();

        // 7) Combinăm DOAR ownDTOs + externalDTOs (fără futureDTOs!)
        List<ContractDTO> combined = new ArrayList<>();
        combined.addAll(ownDTOs);
        combined.addAll(externalDTOs);

        // 8) Filtrăm status (nu le afișăm pe cele cu status ACTIV sau FINALIZATĂ)
        List<ContractDTO> filtered = combined.stream()
                .filter(dto -> {
                    MaterieStatus st = statusMap.get(dto.getCod());
                    return st == null || st == MaterieStatus.PICATA;
                })
                .toList();

        // 9) Din cele rămase, păstrăm doar cursurile cu semestruRel == parametrul intrat
        List<ContractDTO> result = filtered.stream()
                .filter(dto -> dto.getSemestru() == semestruRel)
                .toList();

        return result;
    }



    public List<ContractDTO> getContractCourses(String studentCod, int anContract) {
        Student student = studentRepository.findByCod(studentCod)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        int specId = student.getSpecializare().getId();

        // ia toate entry-urile pentru anul respectiv, semestrul e parte din entry
        List<CurriculumEntry> entries =
                curriculumEntryRepository.findBySpecializareIdAndAn(specId, anContract);

        return entries.stream()
                .map(entry -> {
                    var m = entry.getMaterie();
                    boolean obligatorie = entry.getTip() == Tip.OBLIGATORIE;
                    boolean esteObligatorie = entry.getTip() == Tip.OBLIGATORIE;
                    Integer pachetId = null;
                    if (entry.getOptionale() != null) pachetId = entry.getOptionale().getId();
                    return new ContractDTO(
                            m.getCod(),         // codul real al materiei
                            m.getNume(),        // denumirea materiei
                            m.getCredite(),     // credite
                            entry.getSemestru(),// semestru
                            entry.getTip(),
                            obligatorie,
                            pachetId            // ID-ul pachetului de opționale, dacă există
                    );
                })
                .toList();
    }

    private void recordActiveCourses(Student student, String studentCod, List<Materie> materii) {
        for (Materie m : materii) {
            // 1) Caută entry‐ul din catalog pentru (studentCod, m.getCod())
            Optional<CatalogStudentMaterie> maybeEntry =
                    catalogRepo.findByStudentCodAndMaterieCod(studentCod, m.getCod());

            if (maybeEntry.isEmpty()) {
                // –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
                // Dacă nu există deloc, creează rând NOU cu status = ACTIV
                // –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
                CatalogStudentMaterie newEntry = new CatalogStudentMaterie();
                newEntry.setStudent(student);
                newEntry.setMaterie(m);
                newEntry.setSemestru(m.getSemestru());
                newEntry.setStatus(MaterieStatus.ACTIV);
                // (opțional: newEntry.setNota(null); dacă nu ai deja setat default-ul în entitate)
                catalogRepo.save(newEntry);

            } else {
                CatalogStudentMaterie existing = maybeEntry.get();
                MaterieStatus st = existing.getStatus();

                if (st == MaterieStatus.PICATA) {
                    // –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
                    // Dacă a fost picată anterior, atunci o pot re‐inscrie → actualizez la ACTIV
                    // (opțional: resetez nota, în caz că acolo era 4 sau 2, înainte de reînscriere)
                    // –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
                    existing.setStatus(MaterieStatus.ACTIV);
                    existing.setNota(null); // sau 0, dacă vrei să golești câmpul
                    catalogRepo.save(existing);
                }
                // Dacă st == ACTIV sau st == FINALIZATA, nu facem nimic: materia deja e în contract
            }
        }
    }


    public byte[] generateContractPdfWithoutPersist(
            String studentCod,
            int anContract,
            List<String> coduriMaterii
    ) throws Exception {
        // 1) Fetch studentul
        Student student = studentRepository.findByCod(studentCod)
                .orElseThrow(() -> new Exception("Studentul nu a fost găsit!"));

        // 2) Încarcă lista de Materie pentru codurile trimise
        List<Materie> materii = materieRepository.findAllByCodIn(coduriMaterii);
        if (materii.isEmpty()) {
            throw new Exception("Nu au fost găsite materiile selectate.");
        }

        // ───── Validare „nu poți alege obligatorii din anul următor” (rămâne neschimbat) ─────
        int studentCurrentYear = student.getAn();
        int studentSpecId    = student.getSpecializare().getId();
        for (Materie m : materii) {
            Optional<CurriculumEntry> maybeEntry =
                    curriculumEntryRepository.findBySpecializareIdAndMaterieId(studentSpecId, m.getId());
            if (maybeEntry.isPresent()) {
                CurriculumEntry entry = maybeEntry.get();
                if (entry.getTip() == Tip.OBLIGATORIE
                        && entry.getAn() > studentCurrentYear) {
                    throw new IllegalArgumentException(
                            "Nu poți alege materia „" + m.getNume() +
                                    "” deoarece aceasta este obligatorie în anul " + entry.getAn() +
                                    " al specializării tale."
                    );
                }
            }
        }
        // ────────────────────────────────────────────────────────────────────

        // ───── Înlocuiește vechiul filtraj după semestru relativ cu filtraj pe rest la 2 ─────
        // Semestrul I relativ (odd = 1, 3, 5 ...)
        List<Materie> sem1 = materii.stream()
                .filter(m -> (m.getSemestru() % 2) == 1)
                .toList();
        // Semestrul II relativ (even = 2, 4, 6 ...)
        List<Materie> sem2 = materii.stream()
                .filter(m -> (m.getSemestru() % 2) == 0)
                .toList();
        // ───────────────────────────────────────────────────────────────────────────────

        // Construcție PDF (iText / com.lowagie)
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Preview Contract de studii - Anul " + anContract));
        document.add(new Paragraph("Student: " + student.getNume() + " " + student.getPrenume()));
        document.add(new Paragraph("Grupa: " + student.getGrupa() + ", An studiu: " + student.getAn()));
        document.add(new Paragraph(" "));

        BiConsumer<String, List<Materie>> addSemesterTable = (titlu, list) -> {
            try {
                document.add(new Paragraph(titlu));
                PdfPTable tbl = new PdfPTable(3);
                tbl.setWidthPercentage(100);
                tbl.addCell("Cod");
                tbl.addCell("Materie");
                tbl.addCell("Credite");
                for (Materie mm : list) {
                    tbl.addCell(mm.getCod());
                    tbl.addCell(mm.getNume());
                    tbl.addCell(String.valueOf(mm.getCredite()));
                }
                document.add(tbl);
                document.add(new Paragraph(" "));
            } catch (Exception ex) {
                throw new RuntimeException("Eroare la tabelul " + titlu, ex);
            }
        };

        addSemesterTable.accept("Semestrul I", sem1);
        addSemesterTable.accept("Semestrul II", sem2);

        document.close();
        return out.toByteArray();
    }

    public byte[] generateContractFromSelection(
            String studentCod,
            int anContract,
            List<String> coduriMaterii
    ) throws Exception {
        // 1) Încarcă studentul
        Student student = studentRepository.findByCod(studentCod)
                .orElseThrow(() -> new Exception("Studentul nu a fost găsit!"));

        int studentCurrentYear = student.getAn();
        int studentSpecId     = student.getSpecializare().getId();

        // 2) Încarcă materiile selectate
        List<Materie> materii = materieRepository.findAllByCodIn(coduriMaterii);
        if (materii.isEmpty()) {
            throw new Exception("Nu au fost găsite materiile selectate.");
        }

        // ───── Validare „nu poți alege obligatorii din anul următor” ─────
        for (Materie m : materii) {
            Optional<CurriculumEntry> maybeEntry =
                    curriculumEntryRepository.findBySpecializareIdAndMaterieId(studentSpecId, m.getId());
            if (maybeEntry.isPresent()) {
                CurriculumEntry entry = maybeEntry.get();
                if (entry.getTip() == Tip.OBLIGATORIE
                        && entry.getAn() > studentCurrentYear) {
                    throw new IllegalArgumentException(
                            "Nu poți alege materia „" + m.getNume() +
                                    "” deoarece aceasta este obligatorie în anul " + entry.getAn() +
                                    " al specializării tale."
                    );
                }
            }
        }
        // ────────────────────────────────────────────────────────────────────

        // ───── Verificare materii deja active ─────
        List<String> dejaActive = materii.stream()
                .filter(m -> catalogRepo
                        .findByStudentCodAndMaterieCod(studentCod, m.getCod())
                        .filter(e -> e.getStatus() == MaterieStatus.ACTIV)
                        .isPresent()
                )
                .map(Materie::getCod)
                .toList();

        if (!dejaActive.isEmpty()) {
            throw new ValidationException(
                    "Următoarele materii nu pot fi adăugate din nou în contract: "
                            + String.join(", ", dejaActive)
            );
        }

        List<String> interzise = materii.stream()
                .filter(m -> catalogRepo
                        .findByStudentCodAndMaterieCod(studentCod, m.getCod())
                        .filter(e -> e.getStatus() == MaterieStatus.ACTIV || e.getStatus() == MaterieStatus.FINALIZATA)
                        .isPresent()
                )
                .map(Materie::getCod)
                .toList();

        if (!interzise.isEmpty()) {
            throw new ValidationException(
                    "Următoarele materii nu pot fi adăugate în contract deoarece sunt deja active sau finalizate: "
                            + String.join(", ", interzise)
            );
        }

        Map<Integer, List<String>> chosenByPackage = new HashMap<>();
        for (Materie m : materii) {
            // find the curriculum entry for this student’s specialization
            CurriculumEntry entry = curriculumEntryRepository
                    .findBySpecializareIdAndMaterieId(studentSpecId, m.getId())
                    .orElse(null);
            if (entry != null && entry.getOptionale() != null) {
                Integer pkgId = entry.getOptionale().getId();
                chosenByPackage
                        .computeIfAbsent(pkgId, k -> new ArrayList<>())
                        .add(m.getCod());
            }
        }
// now see if any package has >1 chosen
        // after you’ve built `chosenByPackage: Map<Integer,List<String>>`
        for (var kv : chosenByPackage.entrySet()) {
            Integer pkgId = kv.getKey();
            List<String> selectedCodes = kv.getValue();
            if (selectedCodes.size() > 1) {
                // look up the *entity* by its ID, then call .getNume() on it:
                String pkgName = optionaleRepo.findById(pkgId)
                        .map(MateriiOptionale::getNume)
                        .orElse("acest pachet");

                throw new ValidationException(
                        "Poți alege un singur optional din pachetul „" + pkgName +
                                "”, ai selectat: " + String.join(", ", selectedCodes)
                );
            }
        }


        // ────────────────────────────────────────────────────

        // 4) Înregistrează nou-venitele ca ACTIV
        recordActiveCourses(student, studentCod, materii);

        // 5) Salvează contractul
        Contract c = new Contract(studentCod, anContract);
        c.setCoduriMaterii(coduriMaterii);
        contractRepository.save(c);

        // ───── Grupăm materiile după semestrul relativ (mod 2) ─────
        List<Materie> sem1 = materii.stream()
                .filter(m -> (m.getSemestru() % 2) == 1) // sem absolut impare → sem I relativ
                .toList();
        List<Materie> sem2 = materii.stream()
                .filter(m -> (m.getSemestru() % 2) == 0) // sem absolut pare → sem II relativ
                .toList();
        // ────────────────────────────────────────────────────────────────

        // 6) Generare PDF…
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Contract de studii - Anul " + anContract));
        document.add(new Paragraph("Student: " + student.getNume() + " " + student.getPrenume()));
        document.add(new Paragraph("Grupa: " + student.getGrupa() + ", An studiu: " + student.getAn()));
        document.add(new Paragraph(" "));

        BiConsumer<String, List<Materie>> addSemesterTable = (titlu, list) -> {
            try {
                document.add(new Paragraph(titlu));
                PdfPTable tbl = new PdfPTable(3);
                tbl.setWidthPercentage(100);
                tbl.addCell("Cod"); tbl.addCell("Materie"); tbl.addCell("Credite");
                for (Materie mm : list) {
                    tbl.addCell(mm.getCod());
                    tbl.addCell(mm.getNume());
                    tbl.addCell(String.valueOf(mm.getCredite()));
                }
                document.add(tbl);
                document.add(new Paragraph(" "));
            } catch (Exception ex) {
                throw new RuntimeException("Eroare la tabelul " + titlu, ex);
            }
        };

        addSemesterTable.accept("Semestrul I", sem1);
        addSemesterTable.accept("Semestrul II", sem2);

        document.close();
        return out.toByteArray();
    }


    public boolean hasContract(String studentCod, int anContract) {
        return contractRepository
                .findByStudentCodAndAnContract(studentCod, anContract)
                .isPresent();
    }
}
