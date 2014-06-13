package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public class BusDataSourceStub implements BusDataSource
{
    private Collection<Route> routes;

    public BusDataSourceStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    @Override
    public Collection<Route> getRoutes()
    {
        return routes;
    }
}
