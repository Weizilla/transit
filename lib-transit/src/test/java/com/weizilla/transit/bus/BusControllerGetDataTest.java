package com.weizilla.transit.bus;

import com.google.common.collect.Lists;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.source.BusDataSourceStub;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void getsDirectionsFromSource() throws Exception
    {
        String route = "22";
        Collection<Direction> expected = Lists.newArrayList(Direction.Northbound, Direction.Eastbound);
        BusController controller = new BusController(new BusDataSourceStub(route, expected), null);

        Collection<Direction> actual = controller.getDirections(route);

        assertEquals(expected, actual);
    }
}
