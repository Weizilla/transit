package com.weizilla.transit.favorites.sqlite;

import com.google.common.collect.Sets;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Stop;
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

public class SqliteFavoritesStoreStopTest extends SqliteTest
{
    private static final String TABLE_NAME = "fav_stops";

    @Test
    public void createsFavStopsTableDuringInitialization() throws Exception
    {
        executeSql("drop_stops_table.sql");
        assertArrayEquals(EMPTY, databaseTester.getConnection().createDataSet().getTableNames());
        SqliteFavoritesStore.createStore(dbPath);
        assertNotNull(getTable(TABLE_NAME));
    }

    @Test
    public void createsFavStopsTableIfDoesNotExist() throws Exception
    {
        executeSql("drop_stops_table.sql");
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
                SqliteFavoritesStore.createStopsTable(conn);
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
    public void getStopsReturnsDbData() throws Exception
    {
        SqliteFavoritesStore store = SqliteFavoritesStore.createStore(dbPath);
        executeSql("create_stops_table.sql");

        String route = "22";
        Direction direction = Direction.Northbound;
        Set<Integer> expected = Sets.newHashSet(100, 400);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet("get_stops.xml"));

        Collection<Integer> actualIds = store.getFavoriteStops(route, direction);
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteStops() throws Exception
    {
        SqliteFavoritesStore store = SqliteFavoritesStore.createStore(dbPath);
        executeSql("create_stops_table.sql");

        IDataSet expected = readDataSet("save_stops.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        store.saveFavorite(new Stop(100, "36", Direction.Northbound));
        store.saveFavorite(new Stop(200, "N22", Direction.Westbound));
        store.saveFavorite(new Stop(300, "156", Direction.Eastbound));
        store.saveFavorite(new Stop(400, "N22", Direction.Westbound));

        ITable actual = getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }
}