package com.weizilla.transit.source.stream;

import com.weizilla.transit.data.BusResponse;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.source.DataSource;
import com.weizilla.transit.source.DataSourceException;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collection;

public class StreamingDataSource implements DataSource
{
    private static final Logger logger = LoggerFactory.getLogger(StreamingDataSource.class);
    private final Persister serializer;
    private final InputStreamProvider streamProvider;

    public StreamingDataSource(InputStreamProvider streamProvider)
    {
        Strategy strategy = new AnnotationStrategy();
        serializer = new Persister(strategy);
        this.streamProvider = streamProvider;
    }

    @Override
    public Collection<Route> getRoutes()
    {
        try
        (
            InputStream input = streamProvider.getRoutes()
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            throwIfErrors(response);
            return response.getRoutes();
        }
        catch (DataSourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new DataSourceException("Error getting routes from stream", e);
        }
    }

    @Override
    public Collection<Direction> getDirections(String routeId)
    {
        try
        (
            InputStream input = streamProvider.getDirections(routeId)
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            throwIfErrors(response);
            return response.getDirections();
        }
        catch (DataSourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new DataSourceException("Error getting direction for route id " + routeId, e);
        }
    }

    @Override
    public Collection<Stop> getStops(String routeId, Direction direction)
    {
        try
        (
            InputStream input = streamProvider.getStops(routeId, direction)
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            throwIfErrors(response);
            Collection<Stop> stops = response.getStops();
            for (Stop stop : stops)
            {
                stop.setRouteId(routeId);
                stop.setDirection(direction);
            }
            return stops;
        }
        catch (DataSourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new DataSourceException("Error getting stops for route id " + routeId +
                " direction " + direction, e);
        }
    }

    @Override
    public Collection<Prediction> getPredictions(String routeId, int stopId)
    {
        try
        (
            InputStream input = streamProvider.getPredictions(routeId, stopId)
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            throwIfErrors(response);
            return response.getPredictions();
        }
        catch (DataSourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new DataSourceException(
                "Error getting predictions for route id " + routeId + " stop id " + stopId, e);
        }
    }

    private static void throwIfErrors(BusResponse response)
    {
        String errorMsg = response.getErrorMsg();
        if (errorMsg != null)
        {
            throw new DataSourceException(errorMsg);
        }
    }
}
