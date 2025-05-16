package com.growthon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Farm2YouApplication {

	public static void main(String[] args) {
		SpringApplication.run(Farm2YouApplication.class, args);
	}

}
