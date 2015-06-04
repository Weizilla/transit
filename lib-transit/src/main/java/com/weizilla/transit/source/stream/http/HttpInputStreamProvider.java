package com.weizilla.transit.source.stream.http;

import com.google.common.base.Joiner;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.source.stream.InputStreamProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

public class HttpInputStreamProvider implements InputStreamProvider
{
    private static final String BASE_URL = "http://www.ctabustracker.com/bustime/api/v1/";
    private static final String GET_ROUTES = BASE_URL + "getroutes?key={0}";
    private static final String GET_DIRECTIONS = BASE_URL + "getdirections?rt={0}&key={1}";
    private static final String GET_STOPS = BASE_URL + "getstops?rt={0}&dir={1}&key={2}";
    private static final String GET_PREDICTIONS = BASE_URL + "getpredictions?stpid={0}{1}&key={2}";
    private static final String GET_TIME = BASE_URL + "gettime?key={0}";
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
    public InputStream getPredictions(List<Integer> stopIds, List<String> routeIds) throws IOException
    {
        URL url = createGetPredictionsUrl(stopIds, routeIds, apiKey);
        return reader.connectAndReadStream(url);
    }

    @Override
    public InputStream getCurrentTime() throws IOException
    {
        URL url = createGetTimeUrl(apiKey);
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

    protected static URL createGetPredictionsUrl(List<Integer> stopIds, List<String> routeIds, String apiKey) throws IOException
    {
        String stopIdStr = Joiner.on(",").join(stopIds);
        String routeIdStr = "";
        if (routeIds != null && ! routeIds.isEmpty()) {
            routeIdStr = "&rt=" + Joiner.on(",").join(routeIds);
        }
        return new URL(MessageFormat.format(GET_PREDICTIONS, stopIdStr, routeIdStr, apiKey));
    }

    protected static URL createGetTimeUrl(String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_TIME, apiKey));
    }
}
