package com.weizilla.transit.favorites.sqlite;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Stop;
import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SqliteFavoritesStoreStopTest
{
    private static final Logger logger = LoggerFactory.getLogger(SqliteFavoritesStoreStopTest.class);
    private static final String TABLE_NAME = "fav_stops";
    public static final String[] EMPTY = new String[]{};
    private JdbcDatabaseTester databaseTester;
    private Path dbPath;

    @Before
    public void setUp() throws Exception
    {
        dbPath = Files.createTempFile(getClass().getSimpleName() + "-", ".db");
        logger.debug("Temp db file: {}", dbPath);

        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbPath);
    }

    @After
    public void tearDown() throws Exception
    {
        databaseTester.onTearDown();
        Files.delete(dbPath);
    }

    @Test
    public void createsFavStopsTableDuringInitialization() throws Exception
    {
        executeSql("drop_stops_table.sql");
        assertArrayEquals(EMPTY, databaseTester.getConnection().createDataSet().getTableNames());
        SqliteFavoritesStore.createStore(dbPath);
        assertNotNull(databaseTester.getConnection().createDataSet().getTable(TABLE_NAME));
    }

    @Test
    public void createsFavStopsTableIfDoesNotExist() throws Exception
    {
        executeSql("drop_stops_table.sql");
        assertArrayEquals(EMPTY, databaseTester.getConnection().createDataSet().getTableNames());

        IDatabaseConnection connection = databaseTester.getConnection();
        try
        (
            Connection conn = connection.getConnection()
        )
        {
            SqliteFavoritesStore.createStopsTable(conn);
        }
        connection.close();

        assertNotNull(databaseTester.getConnection().createDataSet().getTable(TABLE_NAME));
    }

    @Test
    public void getStopsReturnsDbData() throws Exception
    {
        SqliteFavoritesStore store = SqliteFavoritesStore.createStore(dbPath);
        executeSql("create_stops_table.sql");

        String route = "22";
        Direction direction = Direction.Northbound;
        Set<Integer> expected = Sets.newHashSet(100, 400);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet("/get_stops.xml"));

        Collection<Integer> actualIds = store.getFavoriteStops(route, direction);
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteStops() throws Exception
    {
        SqliteFavoritesStore store = SqliteFavoritesStore.createStore(dbPath);
        executeSql("create_stops_table.sql");

        IDataSet expected = readDataSet("/save_stops.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        store.saveFavorite(new Stop(100, "36", Direction.Northbound));
        store.saveFavorite(new Stop(200, "N22", Direction.Westbound));
        store.saveFavorite(new Stop(300, "156", Direction.Eastbound));
        store.saveFavorite(new Stop(400, "N22", Direction.Westbound));

        ITable actual = databaseTester.getConnection().createDataSet().getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }

    private void executeSql(String filename) throws Exception
    {
        IDatabaseConnection connection = databaseTester.getConnection();
        try
        (
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement()
        )
        {
            URL url = Resources.getResource(filename);
            String sql = Resources.toString(url, Charsets.UTF_8);
            statement.executeUpdate(sql);
        }
        connection.close();
    }

    private static IDataSet readDataSet(String filename) throws Exception
    {
        return new FlatXmlDataSetBuilder().build(SqliteFavoritesStore.class.getResourceAsStream(filename));
    }
}