package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.Route;
import org.junit.Test;

import java.io.InputStream;
import java.util.Collection;

import static org.junit.Assert.*;

public class StreamingBusDataSourceTest
{
    @Test
    public void readsRoutesFromXmlStream() throws Exception
    {
        InputStream inputStream = getClass().getResourceAsStream("/getroutes.xml");
        BusInputStreamProviderStub streamProvider = new BusInputStreamProviderStub(inputStream);
        StreamingBusDataSource source = new StreamingBusDataSource(streamProvider);

        Collection<Route> routes = source.getRoutes();
        assertNotNull(routes);
        assertEquals(127, routes.size());

        Route first = routes.iterator().next();
        assertEquals("1", first.getId());
        assertEquals("Bronzeville/Union Station", first.getName());
    }

}
