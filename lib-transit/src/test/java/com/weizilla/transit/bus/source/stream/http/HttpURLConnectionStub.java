package com.weizilla.transit.bus.source.stream.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HttpURLConnectionStub extends HttpURLConnection
{
    private final InputStream inputStream;
    private final int responseCode;

    public HttpURLConnectionStub(int responseCode)
    {
        this(responseCode, null);
    }

    public HttpURLConnectionStub(int responseCode, InputStream inputStream)
    {
        super(null);
        this.responseCode = responseCode;
        this.inputStream = inputStream;
    }

    @Override
    public int getResponseCode() throws IOException
    {
        return responseCode;
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
