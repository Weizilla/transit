package com.weizilla.transit.favorites.sqlite;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.weizilla.transit.bus.data.Route;
import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SqliteFavoritesStoreRouteTest extends SqliteTest
{
    private static final String TABLE_NAME = "fav_routes";

    @Test
    public void createsFavRoutesTableDuringInitialization() throws Exception
    {
        executeSql("drop_routes_table.sql");
        assertArrayEquals(EMPTY, databaseTester.getConnection().createDataSet().getTableNames());
        SqliteFavoritesStore.createStore(dbPath);
        assertNotNull(getTable(TABLE_NAME));
    }

    @Test
    public void createsFavRoutesTableIfDoesNotExist() throws Exception
    {
        executeSql("drop_routes_table.sql");
        assertArrayEquals(EMPTY, databaseTester.getConnection().createDataSet().getTableNames());

        IDatabaseConnection connection = null;
        try
        {
            connection = databaseTester.getConnection();
            try
            (
                Connection conn = connection.getConnection()
            )
            {
                SqliteFavoritesStore.createRoutesTable(conn);
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

    @Test
    public void getRouteIdsReturnsDbData() throws Exception
    {
        SqliteFavoritesStore store = SqliteFavoritesStore.createStore(dbPath);
        executeSql("create_routes_table.sql");

        Set<String> expected = Sets.newHashSet("22", "36", "54A");
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet("get_routes.xml"));

        Collection<String> actualIds = store.getFavoriteRouteIds();
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteRoutes() throws Exception
    {
        SqliteFavoritesStore store = SqliteFavoritesStore.createStore(dbPath);
        executeSql("create_routes_table.sql");

        Collection<String> routeIds = Lists.newArrayList("134", "156", "J14");
        IDataSet expected = readDataSet("save_routes.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (String routeId : routeIds)
        {
            store.saveFavorite(new Route(routeId));
        }

        ITable actual = getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }
}