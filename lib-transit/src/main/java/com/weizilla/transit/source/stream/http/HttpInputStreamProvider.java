package com.weizilla.transit.source.stream.http;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.source.stream.InputStreamProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

public class HttpInputStreamProvider implements InputStreamProvider
{
    private static final String GET_ROUTES = "http://www.ctabustracker.com/bustime/api/v1/getroutes?key={0}";
    private static final String GET_DIRECTIONS = "http://www.ctabustracker.com/bustime/api/v1/getdirections?rt={0}&key={1}";
    private static final String GET_STOPS = "http://www.ctabustracker.com/bustime/api/v1/getstops?rt={0}&dir={1}&key={2}";
    private static final String GET_PREDICTIONS =
        "http://www.ctabustracker.com/bustime/api/v1/getpredictions?rt={0}&stpid={1,number,#}&key={2}";
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
    public InputStream getDirections(String routeId) throws IOException
    {
        URL url = createGetDirectionsUrl(routeId, apiKey);
        return reader.connectAndReadStream(url);
    }

    @Override
    public InputStream getStops(String routeId, Direction direction) throws IOException
    {
        URL url = createGetStopsUrl(routeId, direction, apiKey);
        return reader.connectAndReadStream(url);
    }

    @Override
    public InputStream getPredictions(String routeId, int stopId) throws IOException
    {
        URL url = createGetPredictionsUrl(routeId, stopId, apiKey);
        return reader.connectAndReadStream(url);
    }

    protected static URL createGetRoutesUrl(String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_ROUTES, apiKey));
    }

    protected static URL createGetDirectionsUrl(String routeId, String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_DIRECTIONS, routeId, apiKey));
    }

    protected static URL createGetStopsUrl(String routeId, Direction direction, String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_STOPS, routeId, direction, apiKey));
    }

    protected static URL createGetPredictionsUrl(String routeId, int stopId, String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_PREDICTIONS, routeId, stopId, apiKey));
    }
}
