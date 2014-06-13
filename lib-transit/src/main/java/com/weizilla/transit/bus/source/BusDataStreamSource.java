package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.BustimeResponse;
import com.weizilla.transit.bus.data.Route;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

public class BusDataStreamSource implements BusDataSource
{
    private static final Logger logger = LoggerFactory.getLogger(BusDataStreamSource.class);
    private final Persister serializer;
    private BusDataInputStream dataInputStream;

    public BusDataStreamSource()
    {
        Strategy strategy = new AnnotationStrategy();
        serializer = new Persister(strategy);
    }

    @Override
    public Collection<Route> getRoutes()
    {
        try
        (
            InputStream input = dataInputStream.getRoutes()
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

    public void setDataInputStream(BusDataInputStream dataInputStream)
    {
        this.dataInputStream = dataInputStream;
    }
}
