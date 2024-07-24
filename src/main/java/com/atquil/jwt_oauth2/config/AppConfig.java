package com.atquil.jwt_oauth2.config;

import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
