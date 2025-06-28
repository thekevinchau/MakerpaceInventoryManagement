package com.Makerspace.MakerspaceInventoryApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // AWS SES requires this to be enabled for email sending
public class MakerspaceInventoryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MakerspaceInventoryAppApplication.class, args);
	}

}
