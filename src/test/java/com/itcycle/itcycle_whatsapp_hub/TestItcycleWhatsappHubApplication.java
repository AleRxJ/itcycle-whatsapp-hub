package com.itcycle.itcycle_whatsapp_hub;

import org.springframework.boot.SpringApplication;

public class TestItcycleWhatsappHubApplication {

	public static void main(String[] args) {
		SpringApplication.from(ItcycleWhatsappHubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
