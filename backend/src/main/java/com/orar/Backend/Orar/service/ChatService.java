package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.OrarRepository;
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


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final MaterieRepository materieRepo;
    private final StudentRepository studentRepo;
    private final CatalogStudentMaterieRepository catalogRepo;
    private final OrarRepository orarRepo;
    private final WebClient client;
    private final String model;

    public ChatService(
            MaterieRepository materieRepo, StudentRepository studentRepo, CatalogStudentMaterieRepository catalogRepo, OrarRepository orarRepo,
            WebClient.Builder webClientBuilder,
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.model:gpt-3.5-turbo}") String model
    ) {
        this.materieRepo = materieRepo;
        this.studentRepo = studentRepo;
        this.catalogRepo = catalogRepo;
        this.orarRepo = orarRepo;
        this.model = model;
        this.client = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
        logger.info("OpenAI model set to {}", model);
    }

    public String chat(String userMessage, String username) {
        // 1. Încarcă contextul din DB
        List<Materie> materii = materieRepo.findAll();
        String catalog = materii.stream()
                .map(m -> String.format("%s – %s (%d credite)",
                        m.getCod(), m.getNume(), m.getCredite()))
                .collect(Collectors.joining("\n"));

        // 2. Construiește prompt-ul de sistem
        String systemPrompt = """
        Ești un asistent virtual pentru platforma universitară.
        Numele de utilizator al studentului este: %s
        Ai la dispoziție catalogul următor:
        %s

        Exemple:
        Student: „Câte credite are materia Tehnici Avansate de Programare?”
        Asistent: „Materia «TAP101 – Tehnici Avansate de Programare» are 5 credite.”

        Student: „Ce se învață la Rețele de Calculatoare?”
        Asistent: „«RC202 – Rețele de Calculatoare»: Model OSI, TCP/IP, protocoale...”

        Răspunde concis și corect.
        """.formatted(username, catalog);

        // 3. Pregătește body-ul JSON
        Map<String,Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user",   "content", userMessage)
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
            return response.bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {});
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
}
