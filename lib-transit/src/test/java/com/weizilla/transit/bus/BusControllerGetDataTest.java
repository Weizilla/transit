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
    private static final Route ROUTE = new Route("22");
    private static final Stop STOP = new Stop(100);

    @Test
    public void getsRouteListFromSource() throws Exception
    {
        Collection<Route> expected = Collections.singletonList(ROUTE);
        BusController controller = new BusController(new BusDataSourceStub(expected), null, null);

        Collection<Route> actual = controller.getRoutes();

        assertEquals(expected, actual);
    }

    @Test
    public void getsDirectionsFromSource() throws Exception
    {
        Collection<Direction> expected = Lists.newArrayList(Direction.Northbound, Direction.Eastbound);
        BusController controller = new BusController(new BusDataSourceStub(ROUTE, expected), null, null);

        Collection<Direction> actual = controller.getDirections(ROUTE);

        assertEquals(expected, actual);
    }

    @Test
    public void getsStopFromSource() throws Exception
    {
        Direction direction = Direction.Eastbound;
        Collection<Stop> expected = Collections.singletonList(new Stop());
        BusController controller = new BusController(new BusDataSourceStub(ROUTE, direction, expected), null, null);

        Collection<Stop> actual = controller.getStops(ROUTE, direction);

        assertEquals(expected, actual);
    }

    @Test
    public void getsPredictionsFromSource() throws Exception
    {
        Collection<Prediction> expected = Collections.singletonList(new Prediction());
        BusController controller = new BusController(new BusDataSourceStub(ROUTE, STOP, expected), null, null);

        Collection<Prediction> actual = controller.getPredictions(ROUTE, STOP);

        assertEquals(expected, actual);
    }
}
