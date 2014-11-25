package com.weizilla.transit.favorites.sqlite;

import org.junit.Before;
import org.junit.Test;

import static com.weizilla.transit.favorites.sqlite.Favorites.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertNotNull;

public class JdbcSqliteFavStoreRouteTest extends BaseSqliteFavStoreRouteTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteFavoritesStore.createStore(dbPath);
        createTable(TABLE_NAME, "create_routes_table.sql");
    }

    @Test
    public void createsFavRoutesTableDuringInitialization() throws Exception
    {
        dropTable(TABLE_NAME);
        store = JdbcSqliteFavoritesStore.createStore(dbPath);
        assertNotNull(getTable(TABLE_NAME));
    }
}