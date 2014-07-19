package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.Direction;

import java.io.IOException;
import java.io.InputStream;

public interface BusInputStreamProvider
{
    InputStream getRoutes() throws IOException;
    InputStream getDirections(String route) throws IOException;
    InputStream getStops(String route, Direction direction) throws IOException;
}
