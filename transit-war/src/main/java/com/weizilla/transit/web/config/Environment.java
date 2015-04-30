package com.weizilla.transit.web.config;

import org.springframework.stereotype.Component;

@Component
public class Environment
{
    private static final String CTA_API_KEY = "CTA_API_KEY";

    public String getCtaApiKey()
    {
        return System.getenv(CTA_API_KEY);
    }

}
