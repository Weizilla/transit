package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.source.BusDataSource;

import java.util.Collection;

public class BusController
{
    private final BusDataSource source;

    public BusController(BusDataSource source)
    {
        this.source = source;
    }

    public Collection<Route> getRoutes()
    {
        return source.getRoutes();
    }
}
