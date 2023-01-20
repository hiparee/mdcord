package com.lemon.mdcord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MdcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdcodeApplication.class, args);
	}

}
