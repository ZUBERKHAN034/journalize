package com.journalize.journalize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class JournalizeApplication {

	public static void main(String[] args) {
		// Configure Dotenv to ignore missing .env file and load environment variables
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Set environment variables from .env file to system properties
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});
		SpringApplication.run(JournalizeApplication.class, args);
	}

}
