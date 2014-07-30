package com.weizilla.transit.bus.source.stream.http;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpInputStreamProviderTest
{
    private static final String API_KEY = "API_KEY";
    private static final Route ROUTE = new Route("22");
    private static final Direction DIRECTION = Direction.Northbound;
    private static final Stop STOP = new Stop(100);

    @Test
    public void createsGetRoutesUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getroutes?key=API_KEY");
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
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getdirections?rt=" + ROUTE.getId() + "&key=API_KEY");
        URL actual = HttpInputStreamProvider.createGetDirectionsUrl(ROUTE, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getDirectionsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getDirections(ROUTE);
        assertSame(inputStream, actual);
    }

    @Test
    public void getDirectionsCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetDirectionsUrl(ROUTE, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getDirections(ROUTE);
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetStopsUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getstops?rt=" + ROUTE.getId() + "&dir=" + DIRECTION + "&key=API_KEY");
        URL actual = HttpInputStreamProvider.createGetStopsUrl(ROUTE, DIRECTION, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getsStopsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getStops(ROUTE, DIRECTION);
        assertSame(inputStream, actual);
    }

    @Test
    public void getStopsCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetStopsUrl(ROUTE, DIRECTION, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getStops(ROUTE, DIRECTION);
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetPredictionsUrl() throws Exception
    {
        URL exected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?rt=" + ROUTE.getId() + "&stpid=" + STOP.getId() + "&key=API_KEY");
        URL actual = HttpInputStreamProvider.createGetPredictionsUrl(ROUTE, STOP, API_KEY);
        assertEquals(exected, actual);
    }

    @Test
    public void getsPredictionsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getPredictions(ROUTE, STOP);
        assertSame(inputStream, actual);
    }

    @Test
    public void callsGetPredictionWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetPredictionsUrl(ROUTE, STOP, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getPredictions(ROUTE, STOP);
        verify(reader).connectAndReadStream(url);
    }
}