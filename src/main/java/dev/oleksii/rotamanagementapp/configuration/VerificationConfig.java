package dev.oleksii.rotamanagementapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "verification.token") // Binds properties with prefix "verification.token" from your configuration (e.g., application.properties or application.yml).
public class VerificationConfig {
    // The expiration time for the verification token in minutes.
    private int tokenExpirationMinutes;
    // The verification link that is sent to users for confirming their email.
    private String verificationLink;
}
