package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.source.BusSourceStub;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class BusControllerTest
{
    private BusController controller;

    @Before
    public void setUp() throws Exception
    {
        controller = new BusController();
    }

    @Test
    public void getsRouteListFromSource() throws Exception
    {
        Collection<Route> expected = Collections.singletonList(new Route());
        controller.setSource(new BusSourceStub(expected));

        Collection<Route> actual = controller.getRoutes();

        assertEquals(expected, actual);
    }
}
