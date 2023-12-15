package com.github.PiotrDuma.imageshack;

import java.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImageshackApplication {
	private static final Logger log = LoggerFactory.getLogger(ImageshackApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ImageshackApplication.class, args);
		log.info("Application started");
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
