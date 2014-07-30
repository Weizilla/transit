package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.BustimeResponse;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.bus.source.BusDataSource;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

public class StreamingBusDataSource implements BusDataSource
{
    private static final Logger logger = LoggerFactory.getLogger(StreamingBusDataSource.class);
    private final Persister serializer;
    private final BusInputStreamProvider streamProvider;

    public StreamingBusDataSource(BusInputStreamProvider streamProvider)
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
            BustimeResponse response = serializer.read(BustimeResponse.class, input);
            return response.getRoutes();
        }
        catch (Exception e)
        {
            logger.error("Error getting routes from stream", e);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<Direction> getDirections(String route)
    {
        try
        (
            InputStream input = streamProvider.getDirections(route)
        )
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, input);
            return response.getDirections();
        }
        catch (Exception e)
        {
            logger.error("Error getting direction for route {}", route, e);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<Stop> getStops(String route, Direction direction)
    {
        try
        (
            InputStream input = streamProvider.getStops(route, direction)
        )
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, input);
            return response.getStops();
        }
        catch (Exception e)
        {
            logger.error("Error getting stops for route {} direction {}", route, direction);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<Prediction> getPredictions(String route, int stopId)
    {
        try
        (
            InputStream input = streamProvider.getPredictions(route, stopId)
        )
        {
            BustimeResponse response = serializer.read(BustimeResponse.class, input);
            return response.getPredictions();
        }
        catch (Exception e)
        {
            logger.error("Error getting predictions for route {} stopId {}", route, stopId, e);
        }

        return Collections.emptyList();
    }
}
