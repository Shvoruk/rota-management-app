package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, String content){

        Context context = new Context();
        context.setVariable("link", content);

        String htmlContent = templateEngine.process("verify-email", context);
        try {
            // Create a MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // Enable the multipart flag and HTML support with MimeMessageHelper
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);

            // Set HTML content
            helper.setText(htmlContent, true);

            // Send
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
