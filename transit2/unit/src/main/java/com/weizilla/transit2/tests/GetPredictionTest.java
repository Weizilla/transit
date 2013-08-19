package com.weizilla.transit2.tests;

import com.weizilla.transit2.Prediction;
import com.weizilla.transit2.TransitDataProvider;
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
    private TransitDataProvider provider;

    @Before
    public void setUp()
    {
        provider = new MockTransitDataProvider();
    }

    @Test
    public void testGettingSampleData()
    {
        List<Prediction> predictions = provider.getPredictions(null, null);
        assertFalse(predictions.isEmpty());
    }
}
