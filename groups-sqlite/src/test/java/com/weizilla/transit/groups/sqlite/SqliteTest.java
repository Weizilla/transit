package com.weizilla.transit.groups.sqlite;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;


public class SqliteTest
{
    protected static final String[] EMPTY = {};
    protected JdbcDatabaseTester databaseTester;
    protected Path dbPath;
    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    protected void executeSql(String filename) throws Exception
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
                URL url = Resources.getResource(filename);
                String sql = Resources.toString(url, Charsets.UTF_8);
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

    protected ITable getTable(String tableName) throws Exception
    {
        return databaseTester.getConnection().createDataSet().getTable(tableName);
    }

    protected static IDataSet readDataSet(String filename) throws Exception
    {
        URL file = Resources.getResource(filename);
        return new FlatXmlDataSetBuilder().build(file);
    }
}
