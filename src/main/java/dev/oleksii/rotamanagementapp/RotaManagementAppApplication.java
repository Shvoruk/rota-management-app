package dev.oleksii.rotamanagementapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("dev.oleksii.rotamanagementapp.configuration")
public class RotaManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RotaManagementAppApplication.class, args);
	}

}
