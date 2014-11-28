package com.weizilla.transit.cache.sqlite;

import org.junit.Before;
import org.junit.Test;

import static com.weizilla.transit.cache.sqlite.Cache.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertTrue;

public class JdbcSqliteCacheStoreRouteTest extends BaseSqliteCacheStoreRouteTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteCacheStore.createStore(dbPath);
        createTable(TABLE_NAME, "cache/create_routes_table.sql" );
    }

    @Test
    public void createsCacheRoutesTableDuringInitialization() throws Exception
    {
        dropTable(TABLE_NAME);
        JdbcSqliteCacheStore.createStore(dbPath);
        assertTrue(tableExists(TABLE_NAME));
    }
}