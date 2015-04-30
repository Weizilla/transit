package com.weizilla.transit.web.controller;

import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.web.config.BusControllerFactory;
import com.weizilla.transit.web.utils.TestUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransitControllerTest
{
    private BusController busController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        //TODO
        // should be able to use a spring test context to set all this up
        // but can't get it working because it's always using the real implementations
        busController = mock(BusController.class);
        BusControllerFactory busControllerFactory = mock(BusControllerFactory.class);
        when(busControllerFactory.create()).thenReturn(busController);
        TransitController transitController = new TransitController(busControllerFactory);
        mockMvc = MockMvcBuilders.standaloneSetup(transitController).build();
        transitController.init(); // called by spring as a post-construct
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