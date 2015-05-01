package com.weizilla.transit.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig
{
    @Bean
    public BusControllerFactory busControllerFactory()
    {
        return mock(BusControllerFactory.class);
    }

    @Bean
    public String ctaApiKey()
    {
        return "CTA_API_KEY";
    }
}
