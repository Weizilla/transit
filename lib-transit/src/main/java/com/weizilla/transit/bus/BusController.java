package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.source.BusDataSource;

import java.util.Collection;

public class BusController
{
    private BusDataSource source;

    public Collection<Route> getRoutes()
    {
        return source.getRoutes();
    }

    public void setSource(BusDataSource source)
    {
        this.source = source;
    }
}
