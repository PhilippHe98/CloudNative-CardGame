package de.hskl.cloudnative.deck_of_cards.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DeckConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

