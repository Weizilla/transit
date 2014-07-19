package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BusDataSourceStub implements BusDataSource
{
    private Collection<Route> routes;
    private Map<String, Collection<Direction>> directions = new HashMap<>();
    private Map<StopKey, Collection<Stop>> stops = new HashMap<>();

    public BusDataSourceStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    public BusDataSourceStub(String route, Collection<Direction> directions)
    {
        this.directions.put(route, directions);
    }

    public BusDataSourceStub(String route, Direction direction, Collection<Stop> stops)
    {
        this.stops.put(new StopKey(route, direction), stops);
    }

    @Override
    public Collection<Route> getRoutes()
    {
        return routes;
    }

    @Override
    public Collection<Direction> getDirections(String route)
    {
        return directions.get(route);
    }

    @Override
    public Collection<Stop> getStops(String route, Direction direction)
    {
        return stops.get(new StopKey(route, direction));
    }

    private static class StopKey
    {
        private final String route;
        private final Direction direction;

        private StopKey(String route, Direction direction)
        {
            this.route = route;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            StopKey that = (StopKey) o;

            if (direction != that.direction)
            {
                return false;
            }
            if (route != null ? !route.equals(that.route) : that.route != null)
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = route != null ? route.hashCode() : 0;
            result = 31 * result + (direction != null ? direction.hashCode() : 0);
            return result;
        }
    }
}
