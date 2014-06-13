package com.weizilla.transit.bus.source.stream.http;

import com.weizilla.transit.bus.source.stream.BusInputStreamProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

public class HttpInputStreamProvider implements BusInputStreamProvider
{
    private static final String GET_ROUTES = "http://www.ctabustracker.com/bustime/api/v1/getroutes?key={0}";
    private final HttpReader reader;
    private final String apiKey;

    public HttpInputStreamProvider(HttpReader reader, String apiKey)
    {
        this.reader = reader;
        this.apiKey = apiKey;
    }

    @Override
    public InputStream getRoutes() throws IOException
    {
        URL url = createGetRoutesUrl(apiKey);
        return reader.connectAndReadStream(url);
    }

    protected static URL createGetRoutesUrl(String apiKey) throws IOException
    {
        return new URL(MessageFormat.format(GET_ROUTES, apiKey));
    }
}
