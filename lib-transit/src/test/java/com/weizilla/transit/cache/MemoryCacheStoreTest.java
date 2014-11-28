package com.weizilla.transit.cache;

import org.junit.Before;

public class MemoryCacheStoreTest extends BaseCacheStoreTest
{
    @Before
    public void setUp() throws Exception
    {
        store = new MemoryCacheStore();
    }

}