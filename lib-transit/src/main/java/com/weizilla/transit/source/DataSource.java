package com.weizilla.transit.source;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

public interface DataSource
{
    Collection<Route> getRoutes();
    Collection<Direction> getDirections(String routeId);
    Collection<Stop> getStops(String routeId, Direction direction);
    Collection<Prediction> getPredictions(List<Integer> stopIds, List<String> routeIds);
    DateTime getCurrentTime();
}
