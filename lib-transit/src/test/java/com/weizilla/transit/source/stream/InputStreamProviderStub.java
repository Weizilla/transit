package com.weizilla.transit.source.stream;

import com.weizilla.transit.data.Direction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class InputStreamProviderStub implements InputStreamProvider
{
    private InputStream stream;

    @Override
    public InputStream getRoutes()
    {
        return stream;
    }

    @Override
    public InputStream getDirections(String routeId) throws IOException
    {
        return stream;
    }

    @Override
    public InputStream getStops(String routeId, Direction direction) throws IOException
    {
        return stream;
    }

    @Override
    public InputStream getPredictions(List<Integer> stopIds, List<String> routeIds) throws IOException
    {
        return stream;
    }

    @Override
    public InputStream getCurrentTime() throws IOException
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
