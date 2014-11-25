package com.weizilla.transit.sqlite;

import com.google.common.io.Resources;
import com.weizilla.transit.utils.ResourceUtils;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
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
import static org.junit.Assert.assertNotNull;


public abstract class BaseSqliteTest
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected JdbcDatabaseTester databaseTester;
    protected Path dbPath;
    protected File dbFile;

    @Before
    public void setUp() throws Exception
    {
        dbPath = Files.createTempFile(getClass().getSimpleName() + '-', ".db");
        dbFile = dbPath.toFile();
        logger.debug("Temp db file: {}", dbPath);
        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbPath);
    }

    @After
    public void tearDown() throws Exception
    {
        databaseTester.onTearDown();
        Files.delete(dbPath);
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

    protected ITable getTable(String tableName) throws Exception
    {
        return databaseTester.getConnection().createDataSet().getTable(tableName);
    }

    protected void createTable(String table, String filename) throws Exception
    {
        executeSqlFromFile(filename);
        assertNotNull(getTable(table));
    }

    protected void dropTable(String table) throws Exception
    {
        executeSql("DROP TABLE IF EXISTS " + table);
        String[] tableNames = databaseTester.getConnection().createDataSet().getTableNames();
        assertFalse(Arrays.asList(tableNames).contains(table));
    }

    protected static IDataSet readDataSet(String filename) throws Exception
    {
        URL file = Resources.getResource(filename);
        return new FlatXmlDataSetBuilder().build(file);
    }

}
