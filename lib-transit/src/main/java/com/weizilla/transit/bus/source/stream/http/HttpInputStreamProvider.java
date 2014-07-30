package com.weizilla.transit.bus.source.stream.http;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.bus.source.stream.BusInputStreamProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

public class HttpInputStreamProvider implements BusInputStreamProvider
{
    private static final String GET_ROUTES = "http://www.ctabustracker.com/bustime/api/v1/getroutes?key={0}";
    private static final String GET_DIRECTIONS = "http://www.ctabustracker.com/bustime/api/v1/getdirections?rt={0}&key={1}";
    private static final String GET_STOPS = "http://www.ctabustracker.com/bustime/api/v1/getstops?rt={0}&dir={1}&key={2}";
    private static final String GET_PREDICTIONS = "http://www.ctabustracker.com/bustime/api/v1/getpredictions?rt={0}&stpid={1}&key={2}";
    private final HttpReader reader;
    private final String apiKey;

    public HttpInputStreamProvider(HttpReader reader, String apiKey)
    {
        this.reader = reader;
        this.apiKey = apiKey;
    }

    @Override
    public InputStream getRoutes() throws IOException
    {
        URL url = createGetRoutesUrl(apiKey);
        return reader.connectAndReadStream(url);
    }

    @Override
    public InputStream getDirections(Route route) throws IOException
    {
        URL url = createGetDirectionsUrl(route, apiKey);
        return reader.connectAndReadStream(url);
    }

    @Override
    public InputStream getStops(Route route, Direction direction) throws IOException
    {
        URL url = createGetStopsUrl(route, direction, apiKey);
        return reader.connectAndReadStream(url);
    }

    @Override
    public InputStream getPredictions(Route route, Stop stop) throws IOException
    {
        URL url = createGetPredictionsUrl(route, stop, apiKey);
        return reader.connectAndReadStream(url);
    }

    protected static URL createGetRoutesUrl(String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_ROUTES, apiKey));
    }

    protected static URL createGetDirectionsUrl(Route route, String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_DIRECTIONS, route.getId(), apiKey));
    }

    protected static URL createGetStopsUrl(Route route, Direction direction, String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_STOPS, route.getId(), direction, apiKey));
    }

    protected static URL createGetPredictionsUrl(Route route, Stop stop, String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_PREDICTIONS, route.getId(), stop.getId(), apiKey));
    }
}
