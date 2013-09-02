package com.weizilla.transit2.tests;

import com.weizilla.transit2.dataproviders.TransitDataProvider;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Returns sample xml
 *
 * @author wei
 * Date: 8/18/13
 * Time: 5:32 PM
 */
public class MockTransitDataProvider implements Serializable, TransitDataProvider {

    @Override
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes) {
        return readSampleFile("predictions_s1916.xml");
    }

    @Override
    public InputStream getRoutes() {
        return readSampleFile("routes.xml");
    }

    private static InputStream readSampleFile(String filename)
    {
        InputStream in = MockTransitDataProvider.class.getClassLoader().getResourceAsStream(filename);
        return in;
    }

}
