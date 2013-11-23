package com.weizilla.transit;

import com.weizilla.transit.data.BustimeResponse;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * peforms various high level route, prediction, direction lookup
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:40 PM
 */
public class TransitService
{
    private static final String TAG = "TransitService";
    private TransitDataProvider dataProvider;
    private Serializer serializer;

    public TransitService(TransitDataProvider transitDataProvider)
    {
        Strategy strategy = new AnnotationStrategy();
        serializer = new Persister(strategy);
        dataProvider = transitDataProvider;
    }

    public List<Route> lookupRoutes()
    {
        List<Route> results = Collections.emptyList();
        InputStream inputStream = dataProvider.getRoutes();

        if (inputStream == null)
        {
            return results;
        }

        try
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
            results = response.getRoutes();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeStream(inputStream);
        }

        return results;
    }

    public List<Direction> lookupDirections(String route)
    {
        List<Direction> directions = Collections.emptyList();

        InputStream inputStream = dataProvider.getDirections(route);

        try
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
            directions = response.getDirections();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeStream(inputStream);
        }

        return directions;
    }

    public List<Stop> lookupStops(String route, Direction direction)
    {
        List<Stop> stops = Collections.emptyList();

        InputStream inputStream = dataProvider.getStops(route, direction);
        try
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
            stops = response.getStops();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeStream(inputStream);
        }

        return stops;
    }

    public List<Prediction> lookupPredictions(int busStopId)
    {
        return lookupPredictions(Collections.singletonList(busStopId), Collections.<Integer>emptyList());
    }

    public List<Prediction> lookupPredictions(List<Integer> stops, List<Integer> routes)
    {
        List<Prediction> results = Collections.emptyList();
        InputStream inputStream = dataProvider.getPredictions(stops, routes);

        try
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
            results = response.getPredictions();
        }
        catch (Exception e)
        {
            //TODO find logging framework that works in junit and android
            e.printStackTrace();
        }
        finally
        {
            closeStream(inputStream);
        }

        return results;
    }

    private static void closeStream(InputStream inputStream)
    {
        if (inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }
}
