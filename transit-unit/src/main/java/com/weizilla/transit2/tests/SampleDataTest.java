package com.weizilla.transit2.tests;

import com.weizilla.transit2.TransitService;
import com.weizilla.transit2.data.Prediction;
import com.weizilla.transit2.data.Route;
import com.weizilla.transit2.dataproviders.TransitDataProvider;
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
        List<Route> routes = transitService.getRoutes();
        assertEquals(126, routes.size());
    }
}
