package dev.oleksii.rotamanagementapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "verification.token")
public class VerificationConfig {

    private int tokenExpirationMinutes;
    private String verificationLink;

}
