package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.Direction;

import java.io.IOException;
import java.io.InputStream;

public interface BusInputStreamProvider
{
    InputStream getRoutes() throws IOException;
    InputStream getDirections(String routeId) throws IOException;
    InputStream getStops(String routeId, Direction direction) throws IOException;
    InputStream getPredictions(String routeId, int stopId) throws IOException;
}
