package com.weizilla.transit.favorites.sqlite;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static com.weizilla.transit.favorites.sqlite.Favorites.RoutesEntry.TABLE_NAME;
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

        JdbcSqliteFavoritesStore.createStore(dbPath);
        assertNotNull(getTable(TABLE_NAME));
    }

    @Test
    public void createsFavRoutesTableIfDoesNotExist() throws Exception
    {
        dropTable(TABLE_NAME);

        IDatabaseConnection connection = null;
        try
        {
            connection = databaseTester.getConnection();
            try
            (
                Connection conn = connection.getConnection()
            )
            {
                JdbcSqliteFavoritesStore.createRoutesTable(conn);
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }

        assertNotNull(getTable(TABLE_NAME));
    }
}