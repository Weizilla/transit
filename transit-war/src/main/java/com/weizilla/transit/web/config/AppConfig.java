package com.weizilla.transit.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.weizilla.transit")
public class AppConfig
{
    @Bean
    public String ctaApiKey()
    {
        return System.getenv("CTA_API_KEY");
    }
}
