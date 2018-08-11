package com.outfittery.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OutfitteryCodeChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutfitteryCodeChallengeApplication.class, args);
	}
}
