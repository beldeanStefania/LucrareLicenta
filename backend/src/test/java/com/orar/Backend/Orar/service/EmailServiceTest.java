package com.orar.Backend.Orar.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private final String to = "test@example.com";
    private final String subject = "Subject";
    private final String text = "Hello";
    private SimpleMailMessage capturedSimple;

    @BeforeEach
    void setup() {
        // nothing
    }

    @Test
    @DisplayName("sendSimpleEmail should send a SimpleMailMessage successfully")
    void testSendSimpleEmailSuccess() {
        // capture message
        doAnswer(invocation -> {
            SimpleMailMessage msg = invocation.getArgument(0);
            capturedSimple = msg;
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendSimpleEmail(to, subject, text);

        assertNotNull(capturedSimple);
        assertArrayEquals(new String[]{to}, capturedSimple.getTo());
        assertEquals(subject, capturedSimple.getSubject());
        assertEquals(text, capturedSimple.getText());
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("sendSimpleEmail should catch exceptions and not throw")
    void testSendSimpleEmailException() {
        doThrow(new RuntimeException("fail")).when(mailSender).send(any(SimpleMailMessage.class));
        // should not throw
        assertDoesNotThrow(() -> emailService.sendSimpleEmail(to, subject, text));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("sendHtmlEmail should create and send a MimeMessage with HTML content")
    void testSendHtmlEmailSuccess() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // use real helper logic, spy on send
        doNothing().when(mailSender).send(mimeMessage);

        String html = "<p>Hello</p>";
        emailService.sendHtmlEmail(to, subject, html);

        // verify helper set fields via MimeMessage
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        assertSame(mimeMessage, captor.getValue());
    }
}
