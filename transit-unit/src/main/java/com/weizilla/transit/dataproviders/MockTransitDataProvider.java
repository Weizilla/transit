package com.weizilla.transit.dataproviders;

import com.weizilla.transit.data.Direction;

import java.io.InputStream;
import java.util.List;

/**
 * Returns sample xml
 *
 * @author wei
 * Date: 8/18/13
 * Time: 5:32 PM
 */
public class MockTransitDataProvider implements TransitDataProvider
{
    @Override
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes)
    {
        return readSampleFile("predictions_s1916.xml");
    }

    @Override
    public InputStream getRoutes()
    {
        return readSampleFile("routes.xml");
    }

    @Override
    public InputStream getStops(String routeId, Direction direction)
    {
        return readSampleFile("stops_n22.xml");
    }

    @Override
    public InputStream getDirections(String route)
    {
        return readSampleFile("directions_ew.xml");
    }

    @Override
    public InputStream getCurrentTime()
    {
        return readSampleFile("current_time.xml");
    }

    private static InputStream readSampleFile(String filename)
    {
        InputStream in = MockTransitDataProvider.class.getClassLoader().getResourceAsStream(filename);
        return in;
    }

}
