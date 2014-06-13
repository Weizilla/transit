package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Route;
import org.junit.Test;

import java.io.InputStream;
import java.util.Collection;

import static org.junit.Assert.*;

public class BusDataStreamSourceTest
{
    @Test
    public void readsRoutesFromXmlStream() throws Exception
    {
        InputStream inputStream = getClass().getResourceAsStream("/getroutes.xml");
        BusDataStreamSource source = new BusDataStreamSource();
        source.setDataInputStream(new BusDataInputStreamStub(inputStream));

        Collection<Route> routes = source.getRoutes();
        assertNotNull(routes);
        assertEquals(127, routes.size());

        Route first = routes.iterator().next();
        assertEquals("1", first.getId());
        assertEquals("Bronzeville/Union Station", first.getName());
    }

}
