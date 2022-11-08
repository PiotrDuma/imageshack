package com.github.PiotrDuma.imageshack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"java.time.Clock"})
public class ImageshackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageshackApplication.class, args);
	}

}
