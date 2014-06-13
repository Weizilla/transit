package com.weizilla.transit.bus.source;

import java.io.InputStream;

public class BusDataInputStreamStub implements BusDataInputStream
{
    private InputStream stream;

    public BusDataInputStreamStub(InputStream stream)
    {
        this.stream = stream;
    }

    @Override
    public InputStream getRoutes()
    {
        return stream;
    }
}
