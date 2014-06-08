package com.weizilla.transit;

import com.weizilla.transit.data.*;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * peforms various high level route, prediction, direction lookup against a
 * transit data provider
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:40 PM
 */
public class TransitService implements BusRoutesProvider, BusStopsProvider
{
    private static final String TAG = "transit.TransitService";
    private TransitDataProvider dataProvider;
    private Serializer serializer;
    private String dataProviderName;

    public TransitService(TransitDataProvider transitDataProvider)
    {
        Strategy strategy = new AnnotationStrategy();
        serializer = new Persister(strategy);
        dataProvider = transitDataProvider;
        dataProviderName = transitDataProvider.getClass().getSimpleName();
    }

    public List<Route> getRoutes()
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

            //TODO find logging framework that works in junit and android
//            Log.d(TAG, "Got " + results.size() + " from " + dataProviderName);
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

    public List<Direction> lookupDirections(Route route)
    {
        List<Direction> directions = Collections.emptyList();

        InputStream inputStream = dataProvider.getDirections(route.getId());

        try
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
            directions = response.getDirections();
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

        return directions;
    }

    public List<Stop> getStops(Route route, Direction direction)
    {
        List<Stop> stops = Collections.emptyList();

        InputStream inputStream = dataProvider.getStops(route.getId(), direction);
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

    public Date getCurrentTime()
    {
        Date results = new Date();
        InputStream inputStream = dataProvider.getCurrentTime();

        try
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
            results = response.getCurrentTime();
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
