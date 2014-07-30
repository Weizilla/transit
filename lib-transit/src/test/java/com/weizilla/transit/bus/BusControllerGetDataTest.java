package com.weizilla.transit.bus;

import com.google.common.collect.Lists;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
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

    @Test
    public void getsStopFromSource() throws Exception
    {
        String route = "22";
        Direction direction = Direction.Eastbound;
        Collection<Stop> expected = Collections.singletonList(new Stop());
        BusController controller = new BusController(new BusDataSourceStub(route, direction, expected), null);

        Collection<Stop> actual = controller.getStops(route, direction);

        assertEquals(expected, actual);
    }

    @Test
    public void getsPredictionsFromSource() throws Exception
    {
        String route = "22";
        int stopId = 100;
        Collection<Prediction> expected = Collections.singletonList(new Prediction());
        BusController controller = new BusController(new BusDataSourceStub(route, stopId, expected), null);

        Collection<Prediction> actual = controller.getPredictions(route, stopId);

        assertEquals(expected, actual);
    }
}
