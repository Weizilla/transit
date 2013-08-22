package com.weizilla.transit2.tests;

import com.weizilla.transit2.data.Prediction;
import com.weizilla.transit2.TransitDataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Returns sample xml
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:32 PM
 */
public class MockTransitDataProvider implements TransitDataProvider {

    @Override
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes) {
        return readSampleFile("predictions_s1916.xml");
    }

    private static InputStream readSampleFile(String filename)
    {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        return in;
    }

}
