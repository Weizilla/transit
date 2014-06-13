package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.BustimeResponse;
import com.weizilla.transit.bus.data.Route;
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
}
