package com.weizilla.transit;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.List;

/**
 * anything that provides a list of bus stops
 *
 * @author wei
 *         Date: 11/25/13
 *         Time: 2:52 PM
 */
public interface BusStopsProvider
{
    List<Stop> getStops(Route route, Direction direction);
}
