package com.weizilla.transit.favorites.sqlite;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.weizilla.transit.bus.data.Route;
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

import static org.junit.Assert.assertEquals;

public class SqliteFavoriteStoreTest
{
    private static final Logger logger = LoggerFactory.getLogger(SqliteFavoriteStoreTest.class);
    private static final String TABLE_NAME = "fav_routes";
    private JdbcDatabaseTester databaseTester;
    private Path dbPath;
    private SqliteFavoriteStore store;

    @Before
    public void setUp() throws Exception
    {
        initDb();
        store = new SqliteFavoriteStore(dbPath);
    }

    @After
    public void tearDown() throws Exception
    {
        databaseTester.onTearDown();
        Files.delete(dbPath);
    }

    @Test
    public void getRoutesReturnsDbData() throws Exception
    {
        Set<String> expected = Sets.newHashSet("22", "36");
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet("/get_routes.xml"));

        Collection<String> actualIds = store.getFavoriteRoutes();
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteRoutes() throws Exception
    {
        Collection<String> routeIds = Lists.newArrayList("134", "156");
        IDataSet expected = readDataSet("/save_routes.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (String routeId : routeIds)
        {
            store.saveFavorite(new Route(routeId));
        }

        ITable actual = databaseTester.getConnection().createDataSet().getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }

    private void initDb() throws Exception
    {
        dbPath = Files.createTempFile(getClass().getSimpleName() + "-", ".db");
        logger.debug("Temp db file: {}", dbPath);

        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbPath);
        IDatabaseConnection connection = databaseTester.getConnection();
        try
        (
            Connection conn = connection.getConnection();
            Statement statement = conn.createStatement()
        )
        {
            URL url = Resources.getResource("create_routes_table.sql");
            String sql = Resources.toString(url, Charsets.UTF_8);
            statement.executeUpdate(sql);
        }
        connection.close();
    }

    private static IDataSet readDataSet(String filename) throws Exception
    {
        return new FlatXmlDataSetBuilder().build(SqliteFavoriteStore.class.getResourceAsStream(filename));
    }
}