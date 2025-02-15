package dev.oleksii.rotamanagementapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer { // Implements WebMvcConfigurer to customize the default Spring MVC configuration.

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     * This method allows your backend to handle cross-origin requests from the specified origins,
     * HTTP methods, and with credentials if needed.
     *
     * @param registry The CorsRegistry to add mapping configurations.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Apply CORS settings to all endpoints under /api/**.
                .allowedOrigins("http://localhost:5173")  // Allow requests only from this origin.
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permit only these HTTP methods.
                .allowCredentials(true); // Allow cookies, authorization headers, or TLS client certificates to be included in the requests.
    }
}