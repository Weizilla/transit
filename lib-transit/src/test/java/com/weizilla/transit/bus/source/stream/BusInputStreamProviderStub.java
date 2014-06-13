package com.weizilla.transit.bus.source.stream;

import java.io.InputStream;

public class BusInputStreamProviderStub implements BusInputStreamProvider
{
    private InputStream stream;

    public BusInputStreamProviderStub(InputStream stream)
    {
        this.stream = stream;
    }

    @Override
    public InputStream getRoutes()
    {
        return stream;
    }
}
