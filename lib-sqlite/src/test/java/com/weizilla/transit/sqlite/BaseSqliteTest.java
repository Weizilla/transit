package com.weizilla.transit.sqlite;

import com.google.common.io.Resources;
import com.weizilla.transit.utils.ResourceUtils;
import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public abstract class BaseSqliteTest
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Path dbPath;
    protected File dbFile;
    private JdbcDatabaseTester databaseTester;

    @Before
    public void setUp() throws Exception
    {
        dbPath = Files.createTempFile(getClass().getSimpleName() + '-', ".db");
        dbFile = dbPath.toFile();
        dbFile.deleteOnExit();
        logger.debug("Temp db file: {}", dbPath);
        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbPath);
    }

    @After
    public void tearDown() throws Exception
    {
        databaseTester.onTearDown();
    }

    protected void executeSql(String sql) throws Exception
    {
        IDatabaseConnection connection = null;
        try
        {
            connection = databaseTester.getConnection();
            try
            (
                Connection conn = connection.getConnection();
                Statement statement = conn.createStatement()
            )
            {
                statement.executeUpdate(sql);
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
    }

    protected void executeSqlFromFile(String filename) throws Exception
    {
        String sql = ResourceUtils.readFile(filename);
        executeSql(sql);
    }

    protected void deleteFromDb(String dataSetFile) throws Exception
    {
        IDataSet dataSet = readDataSet(dataSetFile);
        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), dataSet);
    }

    protected void loadIntoDb(String dataSetFile) throws Exception
    {
        IDataSet dataSet = readDataSet(dataSetFile);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), dataSet);
    }

    protected void assertTablesEqualFile(String dataSetFile, String ... tables) throws Exception
    {
        IDataSet actual = databaseTester.getConnection().createDataSet();
        IDataSet expected = readDataSet(dataSetFile);
        for (String table : tables)
        {
            assertTableEquals(table, expected, actual);
        }
    }

    private static void assertTableEquals(String table, IDataSet expected, IDataSet actual) throws Exception
    {
        ITable expectedTable = new SortedTable(expected.getTable(table));
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(
            actual.getTable(table), expectedTable.getTableMetaData().getColumns());
        ITable actualSorted = new SortedTable(actualFiltered);
        Assertion.assertEquals(expectedTable, actualSorted);
    }

    protected boolean tableExists(String table) throws Exception
    {
        String[] tableNames = databaseTester.getConnection().createDataSet().getTableNames();
        return Arrays.asList(tableNames).contains(table);
    }

    protected void createTable(String table, String filename) throws Exception
    {
        executeSqlFromFile(filename);
        assertTrue(tableExists(table));
    }

    protected void dropTable(String table) throws Exception
    {
        executeSql("DROP TABLE IF EXISTS " + table);
        assertFalse(tableExists(table));
    }

    protected static IDataSet readDataSet(String filename) throws Exception
    {
        URL file = Resources.getResource(filename);
        return new FlatXmlDataSetBuilder().build(file);
    }

}
