package com.weizilla.transit.bus.source.stream.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpReaderStub extends HttpReader
{
    private InputStream inputStream;

    public HttpReaderStub(InputStream inputStream)
    {
        super(null);
        this.inputStream = inputStream;
    }

    @Override
    public InputStream connectAndReadStream(URL url) throws IOException
    {
        return inputStream;
    }
}
