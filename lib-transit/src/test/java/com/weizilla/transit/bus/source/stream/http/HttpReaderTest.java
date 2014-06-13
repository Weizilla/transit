package com.weizilla.transit.bus.source.stream.http;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class HttpReaderTest
{
    @Test
    public void connectsAndReadsStream() throws Exception
    {
        final InputStream inputStream = mock(InputStream.class);
        HttpReader.HttpURLConnectionFactory factory = new HttpReader.HttpURLConnectionFactory()
        {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException
            {
                return new HttpURLConnectionStub(inputStream);
            }
        };
        HttpReader connection = new HttpReader(factory);

        InputStream actual = connection.connectAndReadStream(null);
        assertEquals(inputStream, actual);
    }

}
