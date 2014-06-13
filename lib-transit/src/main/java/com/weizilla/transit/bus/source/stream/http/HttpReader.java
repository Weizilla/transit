package com.weizilla.transit.bus.source.stream.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReader
{
    private HttpURLConnectionFactory connectionFactory;

    public HttpReader(HttpURLConnectionFactory connectionFactory)
    {
        this.connectionFactory = connectionFactory;
    }

    public InputStream connectAndReadStream(URL url) throws IOException
    {
        HttpURLConnection connection = connectionFactory.createConnection(url);
        //TODO connection.connect();
        //TODO check response code
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
