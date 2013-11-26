package com.weizilla.transit.dataproviders;

import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Tests that parsing the mock xml data works correctly with
 * the mock data provider
 *
 * @author wei
 * Date: 8/18/13
 * Time: 5:45 PM
 */

public class SampleDataTest {
    private static final Route TEST_ROUTE = new Route("TEST_ID", "TEST_NAME", false);
    private TransitService transitService;

    @Before
    public void setUp()
    {
        transitService = new TransitService(new MockTransitDataProvider());
    }

    @Test
    public void testGettingPrediction()
    {
        List<Prediction> predictions = transitService.lookupPredictions(null, null);
        assertEquals(4, predictions.size());
    }

    @Test
    public void testGettingRoutes()
    {
        List<Route> routes = transitService.getRoutes();
        assertEquals(126, routes.size());
    }

    @Test
    public void testGettingDirections()
    {
        List<Direction> directions = transitService.lookupDirections(null);
        assertEquals(2, directions.size());
        assertEquals(Direction.Eastbound, directions.get(0));
    }

    @Test
    public void testGettingStops()
    {
        List<Stop> stops = transitService.getStops(TEST_ROUTE, null);
        assertEquals(85, stops.size());
    }

    @Test
    public void testGettingCurrentTime()
    {
        Date currentTime = transitService.getCurrentTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, Calendar.NOVEMBER, 25, 18, 9, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        Date expected = calendar.getTime();
        assertEquals(expected, currentTime);
    }
}
