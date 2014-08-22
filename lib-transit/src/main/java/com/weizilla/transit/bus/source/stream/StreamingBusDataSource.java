package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.BusResponse;
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
            BusResponse response = serializer.read(BusResponse.class, input);
            return response.getRoutes();
        }
        catch (Exception e)
        {
            logger.error("Error getting routes from stream", e);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<Direction> getDirections(Route route)
    {
        try
        (
            InputStream input = streamProvider.getDirections(route)
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            return response.getDirections();
        }
        catch (Exception e)
        {
            logger.error("Error getting direction for route id {}", route.getId(), e);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<Stop> getStops(Route route, Direction direction)
    {
        try
        (
            InputStream input = streamProvider.getStops(route, direction)
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            Collection<Stop> stops = response.getStops();
            for (Stop stop : stops)
            {
                stop.setRouteId(route.getId());
                stop.setDirection(direction);
            }
            return stops;
        }
        catch (Exception e)
        {
            logger.error("Error getting stops for route id {} direction {}", route.getId(), direction, e);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<Prediction> getPredictions(Route route, Stop stop)
    {
        try
        (
            InputStream input = streamProvider.getPredictions(route, stop)
        )
        {
            BusResponse response = serializer.read(BusResponse.class, input);
            return response.getPredictions();
        }
        catch (Exception e)
        {
            logger.error("Error getting predictions for route id {} stop Id {}", route.getId(), stop.getId(), e);
        }

        return Collections.emptyList();
    }
}
