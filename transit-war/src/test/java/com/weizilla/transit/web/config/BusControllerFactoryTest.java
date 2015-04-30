package com.weizilla.transit.web.config;

import com.weizilla.transit.BusController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.Assert.assertNotNull;

public class BusControllerFactoryTest
{
    private BusControllerFactory factory;

    @Before
    public void setUp() throws Exception
    {
        factory = new BusControllerFactory();
    }

    @Test
    public void createsBusControllerWithCtaApiKey() throws Exception
    {
        factory.setCtaApiKey("CTA API KEY");
        BusController controller = factory.create();
        assertNotNull(controller);
    }

    @Test(expected = NoCtaApiKeyException.class)
    public void throwsExceptionWithoutCtaApiKey() throws Exception
    {
        factory.setCtaApiKey(null);
        factory.create();
    }

    @Test(expected = NoCtaApiKeyException.class)
    public void throwsExceptionWithoutCtaApiKeySpring() throws Exception
    {
        try
        (
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)
        )
        {
            BusControllerFactory factory = context.getBean(BusControllerFactory.class);
            factory.create();
        }
    }

    protected static class TestConfig
    {
        @Bean
        public BusControllerFactory busControllerFactory()
        {
            return new BusControllerFactory();
        }

        @Bean
        public String ctaApiKey()
        {
            return null;
        }
    }
}