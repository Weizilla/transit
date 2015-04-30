package com.weizilla.transit.web.controller;

import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.web.config.Environment;
import com.weizilla.transit.web.config.NoCtaApiKeyException;
import com.weizilla.transit.web.config.WebMvcConfig;
import com.weizilla.transit.web.utils.TestUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, TransitControllerTest.TestConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransitControllerTest
{
    private MockMvc mockMvc;

    @Mock
    private BusController busController;

    @Autowired
    private Environment environment;

    private TransitController transitController;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        transitController = new TransitController();
        transitController.setBusController(busController);
        mockMvc = MockMvcBuilders.standaloneSetup(transitController).build();
    }

    @Test(expected = NoCtaApiKeyException.class)
    public void throwsExceptionIfNoCtaKeySet() throws Exception
    {
        when(environment.getCtaApiKey()).thenReturn(null);
        transitController.init();
    }

    @Test
    public void postConstructCreatesBusController() throws Exception
    {
        transitController.init();
        assertNotNull(transitController.busController);
    }

    @Test
    public void returnsCurrentTime() throws Exception
    {
        long now = System.currentTimeMillis();
        DateTimeUtils.setCurrentMillisFixed(now);

        mockMvc.perform(get("/now"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtils.APPLICATION_JSON_UTF_8))
            .andExpect(jsonPath("$.now", is(new DateTime().toString())));
    }

    @Test
    public void returnsAllRoutes() throws Exception
    {
        String id = "ROUTE ID";
        String name = "ROUTE NAME";
        List<Route> routes = Collections.singletonList(new Route(id, name));
        when(busController.getRoutes()).thenReturn(routes);

        mockMvc.perform(get("/routes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtils.APPLICATION_JSON_UTF_8))
            .andExpect(jsonPath("$[0].id", is(id)))
            .andExpect(jsonPath("$[0].name", is(name)));
    }

    protected static class TestConfig
    {
        @Bean
        public Environment environment()
        {
            Environment environment = mock(Environment.class);
            when(environment.getCtaApiKey()).thenReturn("CTA API KEY");
            return environment;
        }
    }
}