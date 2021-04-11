package com.sanjay.ewallet.walletservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
