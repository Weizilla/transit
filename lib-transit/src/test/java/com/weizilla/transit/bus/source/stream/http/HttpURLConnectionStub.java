package com.weizilla.transit.bus.source.stream.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HttpURLConnectionStub extends HttpURLConnection
{
    private InputStream inputStream;

    public HttpURLConnectionStub(InputStream inputStream)
    {
        super(null);
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return inputStream;
    }

    @Override
    public void connect() throws IOException
    {
        // do nothing
    }

    @Override
    public void disconnect()
    {
        // do nothing
    }

    @Override
    public boolean usingProxy()
    {
        return false;
    }
}
