package com.weizilla.transit.bus.source.stream.http;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class HttpReaderTest
{

    @Test
    public void callsConnect() throws Exception
    {
        HttpURLConnection connection = spy(new HttpURLConnectionStub(HttpURLConnection.HTTP_OK));
        HttpReader reader = new HttpReader(new HttpUrlConnectionFactoryStub(connection));

        reader.connectAndReadStream(null);

        verify(connection).connect();
    }

    @Test
    public void setsTimeoutValues() throws Exception
    {
        HttpURLConnection connection = spy(new HttpURLConnectionStub(HttpURLConnection.HTTP_OK));
        HttpReader reader = new HttpReader(new HttpUrlConnectionFactoryStub(connection));

        reader.connectAndReadStream(null);

        verify(connection).setReadTimeout(HttpReader.READ_TIMEOUT_MS);
        verify(connection).setConnectTimeout(HttpReader.CONNECT_TIMEOUT_MS);
    }

    @Test
    public void readsAndReturnsStream() throws Exception
    {
        InputStream inputStream = mock(InputStream.class);
        HttpURLConnection connection = new HttpURLConnectionStub(HttpURLConnection.HTTP_OK, inputStream);
        HttpReader reader = new HttpReader(new HttpUrlConnectionFactoryStub(connection));

        InputStream actual = reader.connectAndReadStream(null);
        assertEquals(inputStream, actual);
    }

    @Test
    public void throwsErrorIfResponseCodeIsUnsuccessful() throws Exception
    {
        int code = HttpURLConnection.HTTP_FORBIDDEN;
        try
        {
            HttpURLConnection connection = new HttpURLConnectionStub(code);
            HttpReader reader = new HttpReader(new HttpUrlConnectionFactoryStub(connection));

            reader.connectAndReadStream(null);
            fail("Expected IO Exception to be thrown");
        }
        catch (IOException e)
        {
            assertTrue(e.getMessage().contains(String.valueOf(code)));
        }
    }

    private static class HttpUrlConnectionFactoryStub extends HttpReader.HttpURLConnectionFactory
    {
        private HttpURLConnection connection;

        private HttpUrlConnectionFactoryStub(HttpURLConnection connection)
        {
            this.connection = connection;
        }

        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException
        {
            return connection;
        }
    }

}
