package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Implementation of the {@link EmailService} interface that sends
 * verification emails using Thymeleaf templates and the JavaMailSender.
 * <p>
 * It constructs a MIME message with HTML content rendered by Thymeleaf
 * and sends it to the specified email recipient.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    // Spring's JavaMailSender for constructing and sending email messages
    private final JavaMailSender mailSender;

    // Thymeleaf engine to process and render HTML templates
    private final TemplateEngine templateEngine;

    /**
     * Sends a verification email to the specified recipient. The method:
     * <ul>
     *   <li>Builds a Thymeleaf context and inserts the verification link.</li>
     *   <li>Processes the specified template to create HTML content.</li>
     *   <li>Wraps the HTML content in a MimeMessage.</li>
     *   <li>Logs success or failure of the email sending process.</li>
     * </ul>
     *
     * @param to              The recipient's email address.
     * @param subject         The subject of the email.
     * @param templateName    The name of the Thymeleaf template file to render.
     * @param verificationLink The link the user should click to verify their email.
     */
    @Override
    public void sendVerificationEmail(String to, String subject, String templateName, String verificationLink) {
        // Set up the Thymeleaf context, adding variables the template will use
        Context context = new Context();
        context.setVariable("link", verificationLink);

        // Render the template as an HTML string
        String htmlContent = templateEngine.process(templateName, context);

        try {
            // Create a MimeMessage using Spring's JavaMailSender
            MimeMessage message = mailSender.createMimeMessage();

            // Use MimeMessageHelper to set properties like recipient, subject, and HTML content
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);  // true indicates HTML content

            // Send the email
            mailSender.send(message);
            log.info("HTML email sent successfully to {}", to);

        } catch (MessagingException ex) {
            // Log any failures in sending the email
            log.error("Failed to send HTML email to {}: {}", to, ex.getMessage());
        }
    }
}
