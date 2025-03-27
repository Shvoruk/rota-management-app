package dev.oleksii.rotamanagementapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfig {

    private String host;
    private int port;
    private String username;
    private String password;

    /**
     * Creates and configures a JavaMailSender bean for sending emails.
     *
     * @return Configured JavaMailSender instance.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        // Create an instance of the default JavaMailSender implementation.
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the SMTP server details.
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Retrieve the JavaMail properties to customize email settings.
        Properties props = mailSender.getJavaMailProperties();
        // Specify the protocol to be used for email transport.
        props.put("mail.transport.protocol", "smtp");
        // Enable SMTP authentication.
        props.put("mail.smtp.auth", "true");
        // Enable STARTTLS to secure the connection.
        props.put("mail.smtp.starttls.enable", "true");
        // Enable debugging for troubleshooting email issues.
        props.put("mail.debug", "true");

        // Return the configured JavaMailSender.
        return mailSender;
    }
}
