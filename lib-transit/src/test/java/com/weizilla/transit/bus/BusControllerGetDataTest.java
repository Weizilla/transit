package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.source.BusDataSourceStub;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class BusControllerGetDataTest
{
    @Test
    public void getsRouteListFromSource() throws Exception
    {
        Collection<Route> expected = Collections.singletonList(new Route());
        BusController controller = new BusController(new BusDataSourceStub(expected), null);

        Collection<Route> actual = controller.getRoutes();

        assertEquals(expected, actual);
    }
}
