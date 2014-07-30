package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.io.IOException;
import java.io.InputStream;

public interface BusInputStreamProvider
{
    InputStream getRoutes() throws IOException;
    InputStream getDirections(Route route) throws IOException;
    InputStream getStops(Route route, Direction direction) throws IOException;
    InputStream getPredictions(Route route, Stop stop) throws IOException;
}
