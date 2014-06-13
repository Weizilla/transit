package com.weizilla.transit.bus.source.stream.http;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class HttpInputStreamProviderTest
{
    private static final String API_KEY = "API_KEY";

    @Test
    public void createsGetRoutesUrl() throws Exception
    {
        URL expected = new URL("http://www.ctabustracker.com/bustime/api/v1/getroutes?key=API_KEY");
        URL actual = HttpInputStreamProvider.createGetRoutesUrl(API_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getRoutesReturnsInputStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpReader connection = new HttpReaderStub(inputStream);
        HttpInputStreamProvider provider = new HttpInputStreamProvider(connection, API_KEY);

        InputStream actual = provider.getRoutes();
        assertEquals(inputStream, actual);
    }
}