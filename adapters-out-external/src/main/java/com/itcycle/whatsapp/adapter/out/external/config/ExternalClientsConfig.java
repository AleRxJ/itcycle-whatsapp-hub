package com.itcycle.whatsapp.adapter.out.external.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * ExternalClientsConfig - configuration for external HTTP clients.
 */
@Configuration
public class ExternalClientsConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
