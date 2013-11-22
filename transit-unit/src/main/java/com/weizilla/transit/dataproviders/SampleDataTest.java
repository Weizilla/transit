package com.weizilla.transit.dataproviders;

import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * TODO auto-generated header
 *
 * @author wei
 * Date: 8/18/13
 * Time: 5:45 PM
 */

public class SampleDataTest {
    private TransitService transitService;

    @Before
    public void setUp()
    {
        transitService = new TransitService();
        TransitDataProvider provider = new MockTransitDataProvider();
        transitService.setDataProvider(provider);
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
        List<Route> routes = transitService.lookupRoutes();
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
        List<Stop> stops = transitService.lookupStops(null, null);
        assertEquals(85, stops.size());
    }
}
