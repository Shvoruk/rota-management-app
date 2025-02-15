package dev.oleksii.rotamanagementapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt") // Binds properties prefixed with "jwt" from application.properties or application.yml to this class
public class JwtConfig {
    // Secret key used for signing and validating JWTs
    private String secret;
    // Token expiration duration in milliseconds
    private long expirationMilliseconds;
}
