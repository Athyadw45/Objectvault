package com.objectvault.objectvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ObjectvaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObjectvaultApplication.class, args);

		System.out.println("Hello...Atharva Here");
	}
}
