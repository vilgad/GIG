package com.snippet.gig.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GigConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
