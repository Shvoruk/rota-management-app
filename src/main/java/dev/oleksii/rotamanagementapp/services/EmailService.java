package dev.oleksii.rotamanagementapp.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
