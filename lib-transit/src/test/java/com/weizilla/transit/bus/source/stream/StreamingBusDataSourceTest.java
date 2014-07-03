package com.weizilla.transit.bus.source.stream;

import com.weizilla.transit.bus.data.Route;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StreamingBusDataSourceTest
{
    private BusInputStreamProviderStub streamProvider;
    private StreamingBusDataSource source;

    @Before
    public void setUp() throws Exception
    {
        streamProvider = new BusInputStreamProviderStub();
        source = new StreamingBusDataSource(streamProvider);
    }

    @After
    public void tearDown() throws Exception
    {
        streamProvider.closeStream();
    }

    @Test
    public void readsRoutesFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getroutes.xml");

        Collection<Route> routes = source.getRoutes();
        assertNotNull(routes);
        assertEquals(127, routes.size());

        Route first = routes.iterator().next();
        assertEquals("1", first.getId());
        assertEquals("Bronzeville/Union Station", first.getName());

        streamProvider.closeStream();
    }


}
