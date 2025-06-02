package com.orar.Backend.Orar.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Trimite un email simplu (text/plain).
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        // poți seta și: setFrom(...) dacă vrei o adresă diferită
        try {
            mailSender.send(message);
            log.info("Email trimis către {}", to);
        } catch (Exception ex) {
            log.error("Eroare la trimiterea email-ului către {}: {}", to, ex.getMessage());
        }
    }

    /**
     * Dacă vrei să trimiți HTML, poți folosi MimeMessage și MimeMessageHelper.
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // al doilea param true => conținut HTML
            mailSender.send(mimeMessage);
            log.info("HTML email trimis către {}", to);
        } catch (MessagingException e) {
            log.error("Eroare la trimiterea HTML email-ului către {}: {}", to, e.getMessage());
        }
    }
}
