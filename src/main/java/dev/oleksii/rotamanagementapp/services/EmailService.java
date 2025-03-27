package dev.oleksii.rotamanagementapp.services;

/**
 * Service interface for sending various types of emails. In this context,
 * it focuses on sending a verification email, but can be extended as needed.
 */
public interface EmailService {

    /**
     * Sends a verification email to a given recipient using a specified template.
     *
     * @param to             The email address of the recipient.
     * @param subject        The subject line of the email.
     * @param templateName   The name of the Thymeleaf template to render.
     * @param verificationLink The link to be inserted in the email for verification.
     */
    void sendVerificationEmail(String to, String subject, String templateName, String verificationLink);
}
