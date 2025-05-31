package com.orar.Backend.Orar.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.enums.MaterieStatus;
import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.model.Contract;
import com.orar.Backend.Orar.model.CurriculumEntry;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.ContractRepository;
import com.orar.Backend.Orar.repository.CurriculumEntryRepository;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
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

    /**
     * Returnează lista de cursuri disponibile pentru un anumit student, an și semestru relativ (1 sau 2),
     * conform regulilor:
     *  - include propriile cursuri (obligatorii/opționale/facultative) din semestrul absolut = (anContract-1)*2 + semestruRel.
     *  - include cursele obligatorii din alte specializări, ca „opționale” externe.
     *  - include toate cursurile de tip OPTIONALA sau FACULTATIVA din anii > anContract (pentru propria specializare),
     *    marcate cu semestruRel = ((semestruAbsolut-1)%2)+1 și obligatorie=false.
     *  - elimină orice curs deja avut cu status ACTIV sau FINALIZATĂ.
     */
    public List<ContractDTO> getAvailableCoursesForContract(
            String studentCod,
            int anContract,
            int semestruRel // 1 sau 2
    ) {
        // 0) Încarcă studentul
        Student student = studentRepository.findByCod(studentCod)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        int specIdCurent = student.getSpecializare().getId();
        int studentYear  = student.getAn();

        // 0.a) Să nu permită generarea pentru un an > studentYear (opțional)
        if (anContract > studentYear) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Nu poți genera contract pentru anul " + anContract +
                            " deoarece ești în prezent în anul " + studentYear + "."
            );
        }

        // 1) Calculăm semestrul absolut pe baza semestrului relativ
        //    (ex: dacă anContract=2 și semestruRel=1, semestruAbsolut=3; dacă semRel=2, semAbs=4)
        int semestruAbsolut = (anContract - 1) * 2 + semestruRel;

        // 2) Preluăm toate CurriculumEntry din propria specializare pentru anContract & semestruAbsolut
        List<CurriculumEntry> ownEntriesThisSem = curriculumEntryRepository
                .findBySpecializareIdAndAnAndSemestru(specIdCurent, anContract, semestruAbsolut);

        // 3) Preluăm obligatorii din celelalte specializări (pentru același anContract & semestruAbsolut)
        List<CurriculumEntry> externalObligatoryEntries = curriculumEntryRepository
                .findByAnAndSemestruAndTip(anContract, semestruAbsolut, Tip.OBLIGATORIE)
                .stream()
                .filter(e -> e.getSpecializare().getId() != specIdCurent)
                .toList();

        // 4) Preluăm toate opusurile (OPTIONALA sau FACULTATIVA) pentru propria specializare în anii > anContract
        List<CurriculumEntry> futureOptionalFacultativeEntries = curriculumEntryRepository
                .findFutureOptionalsAndFacultatives(specIdCurent, anContract);

        // 5) Construiți un map de codMaterie -> status curent (ACTIV, FINALIZATĂ, PICATĂ) din catalogul studentului
        Map<String, MaterieStatus> statusMap = catalogRepo
                .findByStudentCod(studentCod)
                .stream()
                .collect(Collectors.toMap(
                        c -> c.getMaterie().getCod(),
                        CatalogStudentMaterie::getStatus,
                        (first, second) -> first
                ));

        // 6) Mapăm propriile entry‐uri din semestrul absolut curent
        List<ContractDTO> ownDTOs = ownEntriesThisSem.stream()
                .map(e -> {
                    Materie m = e.getMaterie();
                    boolean esteObligatorie = e.getTip() == Tip.OBLIGATORIE;
                    return new ContractDTO(
                            m.getCod(),
                            m.getNume(),
                            m.getCredite(),
                            semestruRel,           // trimitem semestru relativ (1 sau 2)
                            e.getTip(),
                            esteObligatorie
                    );
                })
                .toList();

        // 7) Mapăm entry‐urile „obligatorii” din alte specializări ca și cum ar fi „opționale externe”
        List<ContractDTO> externalDTOs = externalObligatoryEntries.stream()
                .map(e -> {
                    Materie m = e.getMaterie();
                    return new ContractDTO(
                            m.getCod(),
                            m.getNume(),
                            m.getCredite(),
                            semestruRel,         // le arătăm tot ca semestruRel
                            Tip.OPTIONALA,       // forțăm tip = OPTIONALA
                            false                // nu e obligatorie pentru acest student
                    );
                })
                .toList();

        // 8) Mapăm entry‐urile future (OPTIONALA sau FACULTATIVA) din anii > anContract
        //    Pentru fiecare trebuie să calculăm semestruRel din semestruAbsolut:
        //    semestruRelFut = ((semestruAbsolutFut - 1) % 2) + 1
        List<ContractDTO> futureDTOs = futureOptionalFacultativeEntries.stream()
                .map(e -> {
                    Materie m = e.getMaterie();
                    int semAbs = e.getSemestru(); // 3, 4, 5, 6 etc.
                    int semRelFut = ((semAbs - 1) % 2) + 1;
                    return new ContractDTO(
                            m.getCod(),
                            m.getNume(),
                            m.getCredite(),
                            semRelFut,         // semestru relativ la care trebuie să apară
                            e.getTip(),        // poate fi OPTIONALA sau FACULTATIVA
                            false              // nu e obligatorie în anul curent
                    );
                })
                .toList();

        // 9) Combinăm toate trei listele: propriile, externe și future
        List<ContractDTO> combined = new ArrayList<>();
        combined.addAll(ownDTOs);
        combined.addAll(externalDTOs);
        combined.addAll(futureDTOs);

        // 10) Filtrăm oricare dintre aceste cursuri care deja există în catalog cu status ACTIV sau FINALIZATĂ
        List<ContractDTO> filtered = combined.stream()
                .filter(dto -> {
                    MaterieStatus currentStatus = statusMap.get(dto.getCod());
                    // Dacă status e ACTIV sau FINALIZATĂ, nu-l mai afișăm
                    if (currentStatus == MaterieStatus.ACTIV || currentStatus == MaterieStatus.FINALIZATA) {
                        return false;
                    }
                    // în rest îl păstrăm
                    return true;
                })
                .toList();

        // 11) Din cele rămase, păstrăm doar materiile care au semestruRel == parametrul de intrare
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
                    return new ContractDTO(
                            m.getCod(),         // codul real al materiei
                            m.getNume(),        // denumirea materiei
                            m.getCredite(),     // credite
                            entry.getSemestru(),// semestru
                            entry.getTip(),
                            obligatorie
                    );
                })
                .toList();
    }

    private void recordActiveCourses(Student student, String studentCod, List<Materie> materii) {
        materii.stream()
                .filter(m -> catalogRepo.findByStudentCodAndMaterieCod(studentCod, m.getCod()).isEmpty())
                .map(m -> {
                    CatalogStudentMaterie entry = new CatalogStudentMaterie();
                    entry.setStudent(student);
                    entry.setMaterie(m);
                    entry.setSemestru(m.getSemestru());
                    entry.setStatus(MaterieStatus.ACTIV);
                    return entry;
                })
                .forEach(catalogRepo::save);
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
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Următoarele materii nu pot fi adăugate din nou în contract: "
                            + String.join(", ", dejaActive)
            );
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
