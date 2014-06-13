package com.weizilla.transit.bus.source.stream.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReader
{
    protected static final int READ_TIMEOUT_MS = 10000;
    protected static final int CONNECT_TIMEOUT_MS = 10000;
    private HttpURLConnectionFactory connectionFactory;

    public HttpReader(HttpURLConnectionFactory connectionFactory)
    {
        this.connectionFactory = connectionFactory;
    }

    public InputStream connectAndReadStream(URL url) throws IOException
    {
        HttpURLConnection connection = connectionFactory.createConnection(url);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);

        connection.connect();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new IOException("Http exception. Code: " + connection.getResponseCode());
        }

        return connection.getInputStream();
    }

    protected static class HttpURLConnectionFactory
    {
        protected HttpURLConnection createConnection(URL url) throws IOException
        {
            return (HttpURLConnection) url.openConnection();
        }
    }
}
