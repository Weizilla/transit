package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.Direction;

import java.io.IOException;
import java.io.InputStream;

public class BusInputStreamProviderStub implements BusInputStreamProvider
{
    private InputStream stream;

    @Override
    public InputStream getRoutes()
    {
        return stream;
    }

    @Override
    public InputStream getDirections(String route) throws IOException
    {
        return stream;
    }

    @Override
    public InputStream getStops(String route, Direction direction) throws IOException
    {
        return stream;
    }

    @Override
    public InputStream getPredictions(String route, int stopId) throws IOException
    {
        return stream;
    }

    public void setStreamFromResource(String resourceName)
    {
        stream = getClass().getResourceAsStream(resourceName);
    }

    public void closeStream()
    {
        try
        {
            if (stream != null)
            {
                stream.close();
            }
        }
        catch (IOException e)
        {
            // ignore
        }
    }
}
