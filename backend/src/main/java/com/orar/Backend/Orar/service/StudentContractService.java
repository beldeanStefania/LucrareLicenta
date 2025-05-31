package com.orar.Backend.Orar.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.dto.MaterieDTO;
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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.orar.Backend.Orar.enums.MaterieStatus.FINALIZATA;
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

    public List<ContractDTO> getAvailableCoursesForContract(
            String studentCod,
            int anContract,
            int semestru
    ) {
        // 1) Extragem studentul și specializarea
        Student student = studentRepository.findByCod(studentCod)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        int specId = student.getSpecializare().getId();

        // 2) Preluăm toate CurriculumEntry pentru anul și semestrul cerut
        List<CurriculumEntry> entries = curriculumEntryRepository
                .findBySpecializareIdAndAn(specId, anContract)
                .stream()
                .filter(e -> e.getSemestru() == semestru)
                .toList();

        // 3) Construim un map codMaterie → status curent în catalogul studentului
        Map<String, MaterieStatus> statusMap = catalogRepo
                .findByStudentCod(studentCod)
                .stream()
                .collect(Collectors.toMap(
                        c -> c.getMaterie().getCod(),
                        CatalogStudentMaterie::getStatus,
                        (first, second) -> first  // în caz de dubluri, păstrăm primul
                ));

        // 4) Mapăm fiecare entry în DTO și apoi filtrăm:
        //    - dacă statusMap nu conține codul (deci n-a avut niciodată materia) → OK
        //    - dacă status == PICATA → OK
        //    - altfel (ACTIV sau FINALIZATA) → excludem
        return entries.stream()
                .map(e -> {
                    Materie m = e.getMaterie();
                    boolean obligatorie = e.getTip() == Tip.OBLIGATORIE;
                    return new ContractDTO(
                            m.getCod(),
                            m.getNume(),
                            m.getCredite(),
                            e.getSemestru(),
                            e.getTip(),
                            obligatorie
                    );
                })
                .filter(dto -> {
                    MaterieStatus st = statusMap.get(dto.getCod());
                    if (st == MaterieStatus.ACTIV || st == MaterieStatus.PICATA) return false;
                    return true;
                })
                .toList();
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

        // ** NU verificăm dacă sunt deja active – e doar preview **
        // ** NU apelăm recordActiveCourses **
        // ** NU salvăm Contract în contractRepository **

        // Filtrăm materiile pe semestru
        List<Materie> sem1 = materii.stream()
                .filter(m -> m.getSemestru() == 1)
                .toList();
        List<Materie> sem2 = materii.stream()
                .filter(m -> m.getSemestru() == 2)
                .toList();

        // Construcție PDF (folosim iText / com.lowagie)
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
                for (Materie m : list) {
                    tbl.addCell(m.getCod());
                    tbl.addCell(m.getNume());
                    tbl.addCell(String.valueOf(m.getCredite()));
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

        // 2) Încarcă materiile selectate
        List<Materie> materii = materieRepository.findAllByCodIn(coduriMaterii);
        if (materii.isEmpty()) {
            throw new Exception("Nu au fost găsite materiile selectate.");
        }

        // ─────────────────────────────────────────────────────────────────────────
        // 3) Verifică dacă vreuna dintre ele e deja ACTIV
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
        // ─────────────────────────────────────────────────────────────────────────

        // 4) Înregistrează nou-venitele ca ACTIV
        recordActiveCourses(student, studentCod, materii);

        // 5) Salvează contractul
        Contract c = new Contract(studentCod, anContract);
        c.setCoduriMaterii(coduriMaterii);
        contractRepository.save(c);

        // 6) Generare PDF…
        List<Materie> sem1 = materii.stream()
                .filter(m -> m.getSemestru() == 1)
                .toList();
        List<Materie> sem2 = materii.stream()
                .filter(m -> m.getSemestru() == 2)
                .toList();

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
                for (Materie m : list) {
                    tbl.addCell(m.getCod());
                    tbl.addCell(m.getNume());
                    tbl.addCell(String.valueOf(m.getCredite()));
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
