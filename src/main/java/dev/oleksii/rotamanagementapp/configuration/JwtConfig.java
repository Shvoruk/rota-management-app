package dev.oleksii.rotamanagementapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private long expirationMilliseconds;

}
