package com.weizilla.transit.web.controller;

import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.web.config.WebMvcConfig;
import com.weizilla.transit.web.utils.TestUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
@WebAppConfiguration
public class TransitControllerTest
{
    private MockMvc mockMvc;

    @Mock
    private BusController busController;

    private TransitController transitController;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        transitController = new TransitController();
        transitController.setBusController(busController);
        mockMvc = MockMvcBuilders.standaloneSetup(transitController).build();
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
}