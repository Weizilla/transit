package com.weizilla.transit.cache.sqlite;

import com.weizilla.transit.cache.sqlite.BaseSqliteCacheStoreStopTest;
import com.weizilla.transit.cache.sqlite.JdbcSqliteCacheStore;
import org.junit.Before;
import org.junit.Test;

import static com.weizilla.transit.cache.sqlite.Cache.StopEntry.TABLE_NAME;
import static org.junit.Assert.assertTrue;

public class JdbcSqliteCacheStoreStopTest extends BaseSqliteCacheStoreStopTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteCacheStore.createStore(dbPath);
        createTable(TABLE_NAME, "cache/create_stops_table.sql" );
    }

    @Test
    public void createsFavStopsTableDuringInitialization() throws Exception
    {
        dropTable(TABLE_NAME);
        JdbcSqliteCacheStore.createStore(dbPath);
        assertTrue(tableExists(TABLE_NAME));
    }
}