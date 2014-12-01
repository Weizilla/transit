package com.weizilla.transit.favorites.sqlite;

import org.junit.Before;
import org.junit.Test;

import static com.weizilla.transit.favorites.sqlite.Favorites.StopEntry.TABLE_NAME;
import static org.junit.Assert.assertTrue;

public class JdbcSqliteFavStoreStopTest extends BaseSqliteFavStoreStopTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteFavoritesStore.createStore(dbPath);
        createTable(TABLE_NAME, "favorites/create_stops_table.sql");
    }

    @Test
    public void createsFavStopsTableDuringInitialization() throws Exception
    {
        dropTable(TABLE_NAME);
        JdbcSqliteFavoritesStore.createStore(dbPath);
        assertTrue(tableExists(TABLE_NAME));
    }
}