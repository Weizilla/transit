package com.weizilla.transit2.tests;

import com.weizilla.transit2.data.Prediction;
import com.weizilla.transit2.TransitDataProvider;
import com.weizilla.transit2.TransitService;
import com.weizilla.transit2.data.Prediction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:45 PM
 */

public class GetPredictionTest {
    private TransitService transitService;

    @Before
    public void setUp()
    {
        transitService = new TransitService();
        TransitDataProvider provider = new MockTransitDataProvider();
        transitService.setDataProvider(provider);
    }

    @Test
    public void testGettingSampleData()
    {
        List<Prediction> predictions = transitService.getPredictions(null, null);
        assertFalse(predictions.isEmpty());
    }
}
