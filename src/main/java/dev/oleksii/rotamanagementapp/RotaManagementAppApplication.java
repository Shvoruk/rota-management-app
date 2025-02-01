package dev.oleksii.rotamanagementapp;

import dev.oleksii.rotamanagementapp.configuration.JwtConfig;
import dev.oleksii.rotamanagementapp.configuration.EmailVerificationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties({ JwtConfig.class, EmailVerificationConfig.class })
public class RotaManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RotaManagementAppApplication.class, args);
	}

}
