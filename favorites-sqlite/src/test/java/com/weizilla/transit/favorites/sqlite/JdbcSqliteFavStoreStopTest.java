package com.weizilla.transit.favorites.sqlite;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static com.weizilla.transit.favorites.sqlite.Favorites.StopsEntry.TABLE_NAME;
import static org.junit.Assert.assertNotNull;

public class JdbcSqliteFavStoreStopTest extends BaseSqliteFavStoreStopTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteFavoritesStore.createStore(dbPath);
        createTable(TABLE_NAME, "create_stops_table.sql");
    }

    @Test
    public void createsFavStopsTableDuringInitialization() throws Exception
    {
        dropTable(TABLE_NAME);

        JdbcSqliteFavoritesStore.createStore(dbPath);
        assertNotNull(getTable(TABLE_NAME));
    }

    @Test
    public void createsFavStopsTableIfDoesNotExist() throws Exception
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
                JdbcSqliteFavoritesStore.createStopsTable(conn);
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