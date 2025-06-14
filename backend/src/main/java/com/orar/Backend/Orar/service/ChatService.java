package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.model.CurriculumEntry;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.CladireRepository;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.OrarRepository;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RepartizareProfRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.orar.Backend.Orar.model.MateriiOptionale;
import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.model.CurriculumEntry;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final MaterieRepository materieRepo;
    private final StudentRepository studentRepo;
    private final CatalogStudentMaterieRepository catalogRepo;
    private final OrarRepository orarRepo;
    private final ProfesorRepository profesorRepo;
    private final CladireRepository cladireRepo;

    private final RepartizareProfRepository repartizareProfRepo;
    private final WebClient client;
    private final String model;

    public ChatService(
            MaterieRepository materieRepo, StudentRepository studentRepo, CatalogStudentMaterieRepository catalogRepo, OrarRepository orarRepo, ProfesorRepository profesorRepo, CladireRepository cladireRepo, RepartizareProfRepository repartizareProfRepo,
            WebClient.Builder webClientBuilder,
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.model:gpt-3.5-turbo}") String model
    ) {
        this.materieRepo = materieRepo;
        this.studentRepo = studentRepo;
        this.catalogRepo = catalogRepo;
        this.orarRepo = orarRepo;
        this.profesorRepo = profesorRepo;
        this.cladireRepo = cladireRepo;
        this.repartizareProfRepo = repartizareProfRepo;
        this.model = model;
        this.client = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
        logger.info("OpenAI model set to {}", model);
    }

    public String chat(String userMessage, String username) {
        // 1. Încarcă contextul din DB
        Student stud = studentRepo.findByUserUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Specializare spec = stud.getSpecializare();
        List<CatalogStudentMaterie> istoricul = catalogRepo.findByStudent(stud);
        List<Materie> materii = materieRepo.findAll();
        List<CurriculumEntry> curriculum = orarRepo.findCurriculumBySpecializare(spec);
        List<Orar> orar = orarRepo.findByGrupa(stud.getGrupa());
        String conflictReply = buildOptionalConflictReply(userMessage, stud.getAn(), curriculum);
        if (conflictReply != null) {
            return conflictReply;
        }

        boolean hasContract = !istoricul.isEmpty();

        // 2. Construiește secțiunea de profil
        String noteProfil = istoricul.stream()
                .map(c -> String.format("%s – %s: nota %.1f",
                        c.getMaterie().getCod(),
                        c.getMaterie().getNume(),
                        c.getNota()))
                .collect(Collectors.joining("\n"));
        String cursuriSpec = curriculum.stream()
                .map(e -> String.format("%s (%d credite, sem %d anul %d)",
                        e.getMaterie().getNume(),
                        e.getMaterie().getCredite(),
                        e.getSemestru(),
                        e.getAn()))
                .collect(Collectors.joining("\n"));
        String orarInfo = orar.stream()
                .map(o -> String.format("%s: %s %s (%s), sala %s, prof. %s",
                        o.getZi(), o.getRepartizareProf().getMaterie().getNume(), o.getRepartizareProf().getTip(), o.getOraInceput() + ":00-" + o.getOraSfarsit() + ":00",
                        o.getSala(), o.getRepartizareProf().getProfesor().getNume()))
                .collect(Collectors.joining("\n"));
        String contractStatus = hasContract
                ? "Studentul are deja contracte active."
                : "Studentul NU are încă un contract de studii generat.";
        String contractMaterii = istoricul.stream()
                .map(c -> String.format("%s (an %d, sem %d, credite: %d)",
                        c.getMaterie().getNume(),
                        c.getMaterie().getAn(),
                        c.getMaterie().getSemestru(),
                        c.getMaterie().getCredite()))
                .collect(Collectors.joining("\n"));


        // 3. Prompt de sistem extins
        String systemPrompt = """
                Ești un asistent virtual pentru platforma universitară.
                Student: %s
                Specializare: %s
                Grupa: %s

                Istoric note:
                %s

                Curriculum specializare:
                %s

                Orar săptămânal:
                %s

                Contractul curent:
                %s

                Folosește aceste date pentru a:
                 - răspunde la întrebări despre credite, conținut de curs, profesori
                 - face recomandări de materii opționale pe baza notelor și intereselor
                 - răspunde la întrebări despre programul dintr-o anumită zi (ex: „Câte cursuri am marți?”)
                 - oferi răspunsuri de tip FAQ (echivalări, contract de studii, etc.)
                 - genera drafturi de cereri (de ex. pentru echivalări)
                Răspunde concis și exact.
                """.formatted(
                username,
                contractStatus,
                spec.getSpecializare(),
                stud.getGrupa(),
                noteProfil,
                cursuriSpec,
                orarInfo
        );


        // 4. Pregătim body-ul și apelăm OpenAI așa cum ai deja…
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
        ));

        int maxRetries = 3;
        int attempt = 0;

        while (true) {
            try {
                // apelul către OpenAI
                Map<String, Object> resp = client.post()
                        .uri("/chat/completions")
                        .bodyValue(body)
                        .exchangeToMono(this::handleResponse)  // vezi metoda handleResponse din exemplu anterior
                        .block();

                if (resp == null || !resp.containsKey("choices")) {
                    return "Îmi pare rău, nu am putut genera un răspuns.";
                }

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices =
                        (List<Map<String, Object>>) resp.get("choices");
                if (choices == null || choices.isEmpty()) {
                    return "Îmi pare rău, nu am putut genera un răspuns.";
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> message =
                        (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");

            } catch (TooManyRequestsException tre) {
                attempt++;
                if (attempt > maxRetries) {
                    throw new ResponseStatusException(
                            HttpStatus.TOO_MANY_REQUESTS,
                            "Limita de cereri atinsă. Reîncearcă mai târziu."
                    );
                }
                // așteptăm conform Retry-After
                try {
                    Thread.sleep(tre.getRetryAfterSeconds() * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Retry întrerupt"
                    );
                }
                // retry automat
            } catch (ResponseStatusException rse) {
                throw rse;
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Eroare internă: " + e.getMessage()
                );
            }
        }
    }

    private String buildOptionalConflictReply(
            String userMessage,
            int anContract,
            List<CurriculumEntry> curriculum
    ) {
        // regex care prinde două nume separate prin "și" sau "si"
        Pattern p = Pattern.compile("alege\\s+([\\p{L}0-9 ]{1,50})\\s+(?:și|si)\\s+([\\p{L}0-9 ]{1,50})", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(userMessage);
        if (!m.find()) return null;

        String name1 = m.group(1).trim();
        String name2 = m.group(2).trim();

        // găsim entry‐urile pentru acel anContract și tip OPTIONALA
        CurriculumEntry e1 = curriculum.stream()
                .filter(e -> e.getAn() == anContract && e.getTip() == Tip.OPTIONALA)
                .filter(e -> e.getMaterie().getNume().equalsIgnoreCase(name1))
                .findFirst().orElse(null);
        CurriculumEntry e2 = curriculum.stream()
                .filter(e -> e.getAn() == anContract && e.getTip() == Tip.OPTIONALA)
                .filter(e -> e.getMaterie().getNume().equalsIgnoreCase(name2))
                .findFirst().orElse(null);

        if (e1 != null && e2 != null
                && e1.getOptionale() != null
                && e1.getOptionale().getId().equals(e2.getOptionale().getId()))
        {
            MateriiOptionale pack = e1.getOptionale();
            // grupăm curriculum‐ul pentru acel an pe pachete opționale
            Map<MateriiOptionale, List<String>> byPack = curriculum.stream()
                    .filter(e -> e.getAn() == anContract && e.getTip() == Tip.OPTIONALA && e.getOptionale() != null)
                    .collect(Collectors.groupingBy(
                            CurriculumEntry::getOptionale,
                            Collectors.mapping(ent -> ent.getMaterie().getNume(), Collectors.toList())
                    ));

            StringBuilder sb = new StringBuilder();
            sb.append(String.format(
                    "Nu poți alege \"%s\" și \"%s\" deoarece fac parte din același pachet opțional: \"%s\".%n",
                    name1, name2, pack.getNume()));
            sb.append("Pachetele opționale disponibile pentru anul ")
                    .append(anContract)
                    .append(" sunt:\n");
            byPack.forEach((pkg, names) ->
                    sb.append("• ")
                            .append(pkg.getNume())
                            .append(": ")
                            .append(String.join(", ", names))
                            .append("\n")
            );
            return sb.toString();
        }

        return null;
    }

    public String chatForProfessor(String userMessage, String username) {
        // 1. Încarcă prof după username
        Profesor prof = profesorRepo.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesorul nu a fost găsit"));

        // 2. Colectează orarul profesorului (listă Orar)
        List<Orar> profSchedule = orarRepo.findByRepartizareProf_Profesor_Id(prof.getId());
        String orarInfo = profSchedule.stream()
                .map(o -> String.format("%s: %02d:00-%02d:00, grupa %s, sala %s, tip %s",
                        o.getZi(),
                        o.getOraInceput(), o.getOraSfarsit(),
                        o.getGrupa(),
                        o.getSala().getNume(),
                        o.getRepartizareProf().getTip()
                ))
                .collect(Collectors.joining("\n"));

        if (orarInfo.isEmpty()) {
            orarInfo = "Profesorul nu are ore programate momentan.";
        }

        // 3. Colectează lista de clădiri + săli
        //   - presupunem că Cladire are o metodă findAll()
        //   - fiecare Cladire conține mai multe entități Sala asociate
        StringBuilder cladiriSaliInfo = new StringBuilder();
        cladireRepo.findAll().forEach(cladire -> {
            String numeCladire = cladire.getNume();
            // presupunem că fiecare entitate Cladire are metoda getSali()
            cladire.getSala().forEach(sala -> {
                cladiriSaliInfo
                        .append(String.format("%s - Sala: %s, Capacitate: %d\n",
                                numeCladire,
                                sala.getNume(),
                                sala.getCapacitate() != null ? sala.getCapacitate() : 0));
            });
        });
        if (cladiriSaliInfo.length() == 0) {
            cladiriSaliInfo.append("Nu există săli înregistrate în sistem.");
        }

        // 4. Construiește prompt-ul de sistem pentru profesor
        String systemPrompt = """
                Ești un asistent virtual dedicat profesorilor din platforma universitară.
                Profesor: %s %s
                Discipline predate: %s

                Orarul profesorului (orele deja programate):
                %s

                Lista de clădiri și săli disponibile:
                %s

                Folosește aceste informații pentru a:
                 - răspunde la întrebări de tip „Ce ore am marți?” sau „Care intervale sunt ocupate joi?”
                 - sugera intervale disponibile într-o anumită zi
                 - indica clădirile și sălile neocupate pentru a rezerva
                 - verifica conflicte de orar atunci când adaugă o nouă oră
                Răspunde concis, clar și indică exact ziua / intervalul / sala potrivită.
                """.formatted(
                prof.getNume(),
                prof.getPrenume(),
                prof.getRepartizareProfs().stream()
                        .map(rp -> rp.getMaterie().getNume())
                        .collect(Collectors.joining(", ")),
                orarInfo,
                cladiriSaliInfo.toString()
        );

        // 5. Pregătim body-ul de apel OpenAI
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
        ));

        // 6. Apelăm OpenAI cu retry logic ca mai sus
        int maxRetries = 3;
        int attempt = 0;
        while (true) {
            try {
                Map<String, Object> resp = client.post()
                        .uri("/chat/completions")
                        .bodyValue(body)
                        .exchangeToMono(this::handleResponse)
                        .block();

                if (resp == null || !resp.containsKey("choices")) {
                    return "Îmi pare rău, nu am putut genera un răspuns.";
                }

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices =
                        (List<Map<String, Object>>) resp.get("choices");
                if (choices == null || choices.isEmpty()) {
                    return "Îmi pare rău, nu am putut genera un răspuns.";
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> message =
                        (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");

            } catch (TooManyRequestsException tre) {
                attempt++;
                if (attempt > maxRetries) {
                    throw new ResponseStatusException(
                            HttpStatus.TOO_MANY_REQUESTS,
                            "Limita de cereri atinsă. Reîncearcă mai târziu."
                    );
                }
                try {
                    Thread.sleep(tre.getRetryAfterSeconds() * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Retry întrerupt"
                    );
                }
            } catch (ResponseStatusException rse) {
                throw rse;
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Eroare internă: " + e.getMessage()
                );
            }
        }
    }


    private Mono<Map<String, Object>> handleResponse(ClientResponse response) {
        HttpStatusCode statusCode = response.statusCode();
        logger.debug("OpenAI răspuns status={} headers={}", statusCode, response.headers().asHttpHeaders());

        // 429
        if (statusCode.value() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            String ra = response.headers()
                    .header("Retry-After")
                    .stream()
                    .findFirst()
                    .orElse("1");
            long retryAfter = Long.parseLong(ra);
            return Mono.error(new TooManyRequestsException(retryAfter));
        }
        // 2xx
        else if (statusCode.is2xxSuccessful()) {
            return response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            });
        }
        // alte erori
        else {
            return response.createException().flatMap(Mono::error);
        }
    }


    // excepţie custom pentru retry
    private static class TooManyRequestsException extends RuntimeException {
        private final long retryAfterSeconds;

        public TooManyRequestsException(long retryAfterSeconds) {
            super("429 Too Many Requests, retry after " + retryAfterSeconds + "s");
            this.retryAfterSeconds = retryAfterSeconds;
        }

        public long getRetryAfterSeconds() {
            return retryAfterSeconds;
        }
    }

    public String chatForAdmin(String userMessage, String username) {
        long totalStudents   = studentRepo.count();
        long totalProfessors = profesorRepo.count();
        long totalMaterii    = materieRepo.count();

        Map<Integer, Long> studentsByYear = studentRepo.findAll().stream()
                .collect(Collectors.groupingBy(Student::getAn, Collectors.counting()));

        Map<String, List<String>> teachMap = repartizareProfRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        rp -> rp.getMaterie().getNume(),
                        Collectors.mapping(
                                rp -> rp.getProfesor().getNume() + " " + rp.getProfesor().getPrenume(),
                                Collectors.toList()
                        )
                ));

        String mappingText = teachMap.entrySet().stream()
                .map(e -> e.getKey() + " → " + String.join(", ", e.getValue()))
                .collect(Collectors.joining("\n"));

        String systemPrompt = String.format("""
      Ești asistentul virtual al administratorului.
      Username-ul tău este: %s

      În sistem avem:
       • Studenți în total: %d
       • Distribuție pe ani: %s
       • Profesori în total: %d
       • Materii în total: %d

      Lista materii → profesori:
      %s

      Folosește aceste date pentru a răspunde la întrebări de tip:
       – “Câți studenți sunt în anul 2?”
       – “Cine predă materia Algebra?”
       – orice statistică despre studenți, profesori, materii.
      Răspunde concis și clar.
      """,
                username,
                totalStudents,
                studentsByYear.entrySet().stream()
                        .map(en -> "an " + en.getKey() + ": " + en.getValue())
                        .collect(Collectors.joining(", ")),
                totalProfessors,
                totalMaterii,
                mappingText
        );

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role","system","content", systemPrompt),
                        Map.of("role","user","content", userMessage)
                )
        );
        @SuppressWarnings("unchecked")
        Map<String,Object> resp = client.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();

        if (resp == null || !resp.containsKey("choices")) {
            return "Îmi pare rău, nu am putut genera un răspuns.";
        }

        @SuppressWarnings("unchecked")
        List<Map<String,Object>> choices = (List<Map<String,Object>>) resp.get("choices");
        if (choices == null || choices.isEmpty()) {
            return "Îmi pare rău, nu am obținut răspuns.";
        }
        @SuppressWarnings("unchecked")
        Map<String,Object> message = (Map<String,Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }
}
