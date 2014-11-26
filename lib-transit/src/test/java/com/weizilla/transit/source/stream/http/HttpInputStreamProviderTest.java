package com.weizilla.transit.source.stream.http;

import com.weizilla.transit.data.Direction;
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
    private static final String ROUTE_ID = "22";
    private static final Direction DIRECTION = Direction.Northbound;
    private static final int STOP_ID = 100;

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
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getdirections?rt="
            + ROUTE_ID + "&key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetDirectionsUrl(ROUTE_ID, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getDirectionsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getDirections(ROUTE_ID);
        assertSame(inputStream, actual);
    }

    @Test
    public void getDirectionsCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetDirectionsUrl(ROUTE_ID, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getDirections(ROUTE_ID);
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetStopsUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getstops?rt="
            + ROUTE_ID + "&dir=" + DIRECTION + "&key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetStopsUrl(ROUTE_ID, DIRECTION, API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getsStopsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getStops(ROUTE_ID, DIRECTION);
        assertSame(inputStream, actual);
    }

    @Test
    public void getStopsCallsReaderWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetStopsUrl(ROUTE_ID, DIRECTION, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getStops(ROUTE_ID, DIRECTION);
        verify(reader).connectAndReadStream(url);
    }

    @Test
    public void createsGetPredictionsUrl() throws Exception
    {
        URL exected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?rt="
            + ROUTE_ID + "&stpid=" + STOP_ID + "&key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetPredictionsUrl(ROUTE_ID, STOP_ID, API_KEY);
        assertEquals(exected, actual);
    }

    @Test
    public void predictionUrlDoesNotContainContainCommaSeparatorInStopId() throws Exception
    {
        int stopId = 10000;
        URL exected = new URL("http://www.ctabustracker.com/bustime/api/v1/getpredictions?rt="
            + ROUTE_ID + "&stpid=" + stopId + "&key=" + API_KEY);
        URL actual = HttpInputStreamProvider.createGetPredictionsUrl(ROUTE_ID, stopId, API_KEY);
        assertEquals(exected, actual);
    }

    @Test
    public void getsPredictionsReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getPredictions(ROUTE_ID, STOP_ID);
        assertSame(inputStream, actual);
    }

    @Test
    public void callsGetPredictionWithProperUrl() throws Exception
    {
        URL url = HttpInputStreamProvider.createGetPredictionsUrl(ROUTE_ID, STOP_ID, API_KEY);
        HttpReader reader = mock(HttpReader.class);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, API_KEY);

        provider.getPredictions(ROUTE_ID, STOP_ID);
        verify(reader).connectAndReadStream(url);
    }
}