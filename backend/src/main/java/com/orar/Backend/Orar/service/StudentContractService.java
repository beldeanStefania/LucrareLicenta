package com.orar.Backend.Orar.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.orar.Backend.Orar.model.MaterieStatus.FINALIZATA;

@Service
public class StudentContractService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CatalogStudentMaterieRepository catalogRepo;

    @Autowired
    private MaterieRepository materieRepository;

    public List<MaterieDTO> getAvailableCoursesForContract(String studentCod, int semestru) {
        List<Materie> toateMateriile = materieRepository.findAll().stream().filter(m -> m.getSemestru().equals(semestru)) //m -> m.getAn() == an &&
                .collect(Collectors.toList());

        List<CatalogStudentMaterie> contractate = catalogRepo.findByStudentCod(studentCod);

        Set<String> coduriDeEvitat = contractate.stream().filter(c -> c.getStatus() == FINALIZATA).map(c -> c.getMaterie().getCod()).collect(Collectors.toSet());

        return toateMateriile.stream().filter(m -> !coduriDeEvitat.contains(m.getCod())).map(m -> new MaterieDTO(m.getNume(), m.getSemestru(), m.getCod(), m.getCredite())).collect(Collectors.toList());
    }

    public byte[] generateContractFromSelection(String studentCod, int semestru, List<String> coduriMaterii) throws Exception {
        Student student = studentRepository.findByCod(studentCod).orElseThrow(() -> new Exception("Studentul nu a fost găsit!"));

        List<Materie> toateMateriile = materieRepository.findAllByCodIn(coduriMaterii);

        if (toateMateriile.isEmpty()) {
            throw new Exception("Nu au fost găsite materiile selectate.");
        }

        System.out.println("Materii gasite:" + toateMateriile.size());

        int totalCredite = toateMateriile.stream().mapToInt(Materie::getCredite).sum();
        if (totalCredite < 30) {
            throw new Exception("Numărul total de credite selectate este " + totalCredite + ". Minim necesar: 30.");
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        try {
            document.open();
            document.add(new Paragraph("Contract de studii"));
            document.add(new Paragraph("Student: " + student.getNume() + " " + student.getPrenume()));
            document.add(new Paragraph("Grupa: " + student.getGrupa() + ", An: " + student.getAn()));
            document.add(new Paragraph("Semestru: " + semestru));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Cod");
            table.addCell("Materie");
            table.addCell("Credite");

            for (Materie materie : toateMateriile) {
                table.addCell(materie.getCod());
                table.addCell(materie.getNume());
                table.addCell(String.valueOf(materie.getCredite()));
            }

            document.add(table);
            document.add(new Paragraph("Total credite: " + totalCredite));
        } catch (Exception e) {
            System.out.println("Eroare la scrierea în PDF: " + e.getMessage());
            throw e;
        } finally {
            document.close();
        }

        byte[] pdfBytes = out.toByteArray();
        try (FileOutputStream fos = new FileOutputStream("C:\\temp\\debug_contract.pdf")) {
            fos.write(pdfBytes);
            fos.flush();
        } catch (Exception e) {
            System.out.println("Eroare la scrierea fișierului debug: " + e.getMessage());
        }
        return out.toByteArray();
    }
}