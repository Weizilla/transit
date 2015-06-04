package com.weizilla.transit;

import com.google.common.collect.Lists;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.source.DataSourceStub;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class BusControllerGetDataTest
{
    private static final String ROUTE_ID = "22";
    private static final int STOP_ID = 100;

    @Test
    public void getsCurrentTimeFromSource() throws Exception
    {
        DateTime expected = new DateTime();
        BusController controller = new BusController(new DataSourceStub(expected), null, null, null);
        DateTime actual = controller.getCurrentTime();
        assertSame(expected, actual);
    }

    @Test
    public void getsRouteListFromSource() throws Exception
    {
        Collection<Route> expected = Collections.singletonList(new Route());
        BusController controller = new BusController(new DataSourceStub(expected), null, null, null);

        Collection<Route> actual = controller.getRoutes();

        assertEquals(expected, actual);
    }

    @Test
    public void getsDirectionsFromSource() throws Exception
    {
        Collection<Direction> expected = Lists.newArrayList(Direction.Northbound, Direction.Eastbound);
        BusController controller = new BusController(new DataSourceStub(ROUTE_ID, expected), null, null, null);

        Collection<Direction> actual = controller.getDirections(ROUTE_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void getsStopFromSource() throws Exception
    {
        Direction direction = Direction.Eastbound;
        Collection<Stop> expected = Collections.singletonList(new Stop());
        BusController controller = new BusController(new DataSourceStub(ROUTE_ID, direction, expected), null, null, null);

        Collection<Stop> actual = controller.getStops(ROUTE_ID, direction);

        assertEquals(expected, actual);
    }

    @Test
    public void getsPredictionsFromSource() throws Exception
    {
        Collection<Prediction> expected = Collections.singletonList(new Prediction());
        BusController controller = new BusController(new DataSourceStub(ROUTE_ID, STOP_ID, expected), null, null, null);

        List<Integer> stopIds = Collections.singletonList(STOP_ID);
        List<String> routeIds = Collections.singletonList(ROUTE_ID);
        Collection<Prediction> actual = controller.getPredictions(stopIds, routeIds);

        assertEquals(expected, actual);
    }
}
