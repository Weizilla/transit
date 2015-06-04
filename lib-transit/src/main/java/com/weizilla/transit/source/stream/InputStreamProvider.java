package com.weizilla.transit.source.stream;

import com.weizilla.transit.data.Direction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface InputStreamProvider
{
    InputStream getRoutes() throws IOException;
    InputStream getDirections(String routeId) throws IOException;
    InputStream getStops(String routeId, Direction direction) throws IOException;
    InputStream getPredictions(List<Integer> stopIds, List<String> routeIds) throws IOException;
    InputStream getCurrentTime() throws IOException;
}
