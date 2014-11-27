package com.weizilla.transit.source.stream.http;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.Direction;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.weizilla.transit.source.stream.http.HttpInputStreamProvider.createGetPredictionsUrl;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpInputStreamProviderTest
{
    private static final String API_KEY = "API_KEY";
    private static final String ROUTE_ID_1 = "22";
    private static final String ROUTE_ID_2 = "36";
    private static final String ROUTE_ID_3 = "151";
    private static final List<String> ROUTE_ID_1_L = singletonList(ROUTE_ID_1);
    private static final Direction DIRECTION = Direction.Northbound;
    private static final int STOP_ID_1 = 100;
    private static final int STOP_ID_2 = 200;
    private static final int STOP_ID_3 = 300;
    private static final List<Integer> STOP_ID_1_L = singletonList(STOP_ID_1);

    @Test
    public void createsGetRoutesUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getroutes?key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetRoutesUrl(API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getRoutesReturnsInputStreamFromProperUrl() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader reader = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        InputStream actual = provider.getRoutes();
        assertSame(inputStream, actual);
    }

    @Test
    public void getRoutesCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetRoutesUrl(API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getRoutes();
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetDirectionsUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getdirections?" +
            "rt=" + ROUTE_ID_1 + "&key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetDirectionsUrl(ROUTE_ID_1, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getDirectionsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getDirections(ROUTE_ID_1);
        assertSame(inputStream, actual);
    }

    @Test
    public void getDirectionsCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetDirectionsUrl(ROUTE_ID_1, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getDirections(ROUTE_ID_1);
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetStopsUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getstops?" +
            "rt=" + ROUTE_ID_1 + "&dir=" + DIRECTION + "&key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetStopsUrl(ROUTE_ID_1, DIRECTION, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getsStopsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getStops(ROUTE_ID_1, DIRECTION);
        assertSame(inputStream, actual);
    }

    @Test
    public void getStopsCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetStopsUrl(ROUTE_ID_1, DIRECTION, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getStops(ROUTE_ID_1, DIRECTION);
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetPredictionsUrlWithSingleStopAndRoute() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?" +
            "stpid=" + STOP_ID_1 + "&rt=" + ROUTE_ID_1 + "&key=" + API_KEY);
        URL actual = createGetPredictionsUrl(STOP_ID_1_L, ROUTE_ID_1_L, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void createsGetPredictionUrlWithMultipleStopsAndRoutes() throws Exception
    {
        List<Integer> stops = Lists.newArrayList(STOP_ID_1, STOP_ID_2, STOP_ID_3);
        List<String> routes = Lists.newArrayList(ROUTE_ID_1, ROUTE_ID_2, ROUTE_ID_3);
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?" +
            "stpid=" + Joiner.on(",").join(stops) + "&rt=" + Joiner.on(",").join(routes) +
            "&key=" + API_KEY);
        URL actual = createGetPredictionsUrl(stops, routes, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void createsGetPredictionsUrlWithSingleStopAndNoRoutes() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?" +
            "stpid=" + STOP_ID_1 + "&key=" + API_KEY);
        URL actual = createGetPredictionsUrl(STOP_ID_1_L, Collections.<String>emptyList(), API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void createsGetPredictionsUrlWithMultipleStopsAndNoRoutes() throws Exception
    {
        List<Integer> stops = Lists.newArrayList(STOP_ID_1, STOP_ID_2, STOP_ID_3);
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?" +
            "stpid=" + Joiner.on(",").join(stops) + "&key=" + API_KEY);
        URL actual = createGetPredictionsUrl(stops, Collections.<String>emptyList(), API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void predictionUrlDoesNotContainContainCommaSeparatorInStopId() throws Exception
    {
        int stopId = 10000;
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?" +
            "stpid=" + stopId + "&rt=" + ROUTE_ID_1 + "&key=" + API_KEY);
        URL actual = createGetPredictionsUrl(singletonList(stopId), ROUTE_ID_1_L, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getsPredictionsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getPredictions(STOP_ID_1_L, ROUTE_ID_1_L);
        assertSame(inputStream, actual);
    }

    @Test
    public void callsGetPredictionWithSingleStopAndRouteWithProperUrl() throws Exception
    {
        URL url = createGetPredictionsUrl(STOP_ID_1_L, ROUTE_ID_1_L, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getPredictions(STOP_ID_1_L, ROUTE_ID_1_L);
        verify(reader).connectAndReadStream(url);
    }
}