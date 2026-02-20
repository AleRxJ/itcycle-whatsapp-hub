package com.itcycle.whatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * iTCycle WhatsApp Hub - Main Application
 * B2B conversational automation platform with hexagonal architecture.
 */
@SpringBootApplication(scanBasePackages = "com.itcycle.whatsapp")
@EnableJpaRepositories(basePackages = "com.itcycle.whatsapp.adapter.out.persistence.repository")
@EntityScan(basePackages = "com.itcycle.whatsapp.adapter.out.persistence.entity")
public class ItcycleWhatsappHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItcycleWhatsappHubApplication.class, args);
    }
}
