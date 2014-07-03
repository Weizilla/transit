package com.weizilla.transit.bus.source.stream.http;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpInputStreamProviderTest
{
    private static final String API_KEY = "API_KEY";
    public static final String ROUTE = "22";

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
        assertEquals(inputStream, actual);

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
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getdirections?rt=" + ROUTE + "&key=API_KEY");
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
        assertEquals(inputStream, actual);
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
}