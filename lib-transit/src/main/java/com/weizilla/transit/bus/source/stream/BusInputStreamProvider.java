package com.weizilla.transit.bus.source.stream;

import java.io.IOException;
import java.io.InputStream;

public interface BusInputStreamProvider
{
    InputStream getRoutes() throws IOException;
    InputStream getDirections(String route) throws IOException;
}
