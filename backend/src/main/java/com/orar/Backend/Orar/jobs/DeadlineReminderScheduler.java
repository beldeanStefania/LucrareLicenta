package com.orar.Backend.Orar.jobs;

import com.orar.Backend.Orar.model.TodoItem;
import com.orar.Backend.Orar.service.EmailService;
import com.orar.Backend.Orar.service.TodoItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DeadlineReminderScheduler {

    @Autowired
    private TodoItemService todoItemService;

    @Autowired
    private EmailService emailService;

    /**
     * Rulează zilnic la ora 08:00 (Europe/Bucharest) și trimite emailuri pentru
     * sarcinile nefinalizate cu deadline AZI sau MÂINE.
     */
    @Scheduled(cron = "0 0 8 * * *", zone = "Europe/Bucharest") // ora 08:00:00 zilnic , in fiecare minut "0 * * * * *"
    @Transactional
    public void sendDeadlineReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<TodoItem> dueToday = todoItemService.getTodosWithDeadline(today);
        List<TodoItem> dueTomorrow = todoItemService.getTodosWithDeadline(tomorrow);

        List<TodoItem> allReminders = new ArrayList<>();
        allReminders.addAll(dueToday);
        allReminders.addAll(dueTomorrow);

        if (allReminders.isEmpty()) {
            log.info("Nu există sarcini cu deadline azi ({}) sau mâine ({}).", today, tomorrow);
            return;
        }

        for (TodoItem todo : allReminders) {
            var student = todo.getStudent();
            var user = student.getUser();
            String toEmail = user.getEmail();

            if (toEmail == null || toEmail.isBlank()) {
                log.warn("Studentul {} nu are email configurat. Sarcina ID={} nu primește reminder.", student.getCod(), todo.getId());
                continue;
            }

            LocalDate deadline = todo.getDeadline();
            String zi = deadline.equals(today) ? "azi" : "mâine";

            String subject = "[Reminder] Sarcină în termen " + zi + ": " + todo.getTitle();
            String text = "Salut " + student.getNume() + ",\n\n" +
                    "Ai o sarcină în to-do list care expiră " + zi + " (" + deadline + "):\n" +
                    "- Titlu: " + todo.getTitle() + "\n" +
                    "- Descriere: " + (todo.getDescription() != null ? todo.getDescription() : "(fără descriere)") + "\n\n" +
                    "Te rugăm să o finalizezi sau să actualizezi status-ul.\n\n" +
                    "Mult succes,\nEchipa Orar App";

            emailService.sendSimpleEmail(toEmail, subject, text);
            log.info("Trimis reminder către {} pentru sarcina '{}'", toEmail, todo.getTitle());
        }
    }
}
