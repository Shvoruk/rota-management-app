package dev.oleksii.rotamanagementapp.configuration;

import dev.oleksii.rotamanagementapp.security.JwtAuthenticationFilter;
import dev.oleksii.rotamanagementapp.security.JwtService;
import dev.oleksii.rotamanagementapp.services.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configures the HTTP security for the application.
     *
     * @param http The HttpSecurity instance to configure.
     * @param authenticationProvider The custom authentication provider.
     * @param jwtAuthenticationFilter The JWT authentication filter for processing JWT tokens.
     * @return Configured SecurityFilterChain.
     * @throws Exception in case of configuration errors.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            AuthenticationProvider authenticationProvider,
                                            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                // Disable CSRF since we're using JWTs for authentication (stateless API)
                .csrf(AbstractHttpConfigurer::disable)
                // Enable CORS with default settings (customize if needed)
                .cors(Customizer.withDefaults())
                // Configure authorization rules for HTTP requests
                .authorizeHttpRequests(req -> req
                        // Allow public (unauthenticated) access to endpoints under "/api/v1/auth/**"
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated())
                // Set session management to stateless, so no session is created or used by Spring Security
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Register the custom authentication provider
                .authenticationProvider(authenticationProvider)
                // Add the JWT authentication filter before the default UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured SecurityFilterChain instance
        return http.build();
    }

    /**
     * Defines the authentication provider bean using a DAO-based approach.
     *
     * @param userDetailsService The service that retrieves user details from the database.
     * @param passwordEncoder The encoder used for encoding and matching passwords.
     * @return A configured AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService,
                                                         PasswordEncoder passwordEncoder) {

        // Create an instance of DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Set the UserDetailsService implementation to fetch user details during authentication
        authProvider.setUserDetailsService(userDetailsService);
        // Set the password encoder to validate user passwords securely
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Configures the password encoder bean using BCrypt.
     *
     * @return A PasswordEncoder instance that uses BCrypt hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the AuthenticationManager bean that handles authentication operations.
     *
     * @param authenticationConfiguration The configuration object that holds authentication settings.
     * @return The AuthenticationManager instance.
     * @throws Exception if the authentication manager cannot be retrieved.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the JWT authentication filter bean.
     *
     * @param jwtService The service responsible for handling JWT operations.
     * @param userDetailsService The service that retrieves user details from the database.
     * @return A new instance of JwtAuthenticationFilter.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }
}
