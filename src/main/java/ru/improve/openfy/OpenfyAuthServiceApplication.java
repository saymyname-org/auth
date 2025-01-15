package ru.improve.openfy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OpenfyAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenfyAuthServiceApplication.class, args);
	}
}
