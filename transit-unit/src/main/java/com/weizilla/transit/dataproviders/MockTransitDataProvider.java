package com.weizilla.transit.dataproviders;

import com.google.common.base.Joiner;
import com.weizilla.transit.data.Direction;

import java.io.InputStream;
import java.util.Collections;
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

    private static final String DEFAULT_PREDICTIONS_FILE = "predictions_1916.xml";

    @Override
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes)
    {
        if (stops == null || stops.isEmpty())
        {
            return readSampleFile(DEFAULT_PREDICTIONS_FILE);
        }

        Collections.sort(stops);
        String stopIdFileName = Joiner.on("_").join(stops);
        InputStream file = readSampleFile("predictions_" + stopIdFileName + ".xml");

        if (file == null)
        {
            file = readSampleFile(DEFAULT_PREDICTIONS_FILE);
        }

        return file;
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
