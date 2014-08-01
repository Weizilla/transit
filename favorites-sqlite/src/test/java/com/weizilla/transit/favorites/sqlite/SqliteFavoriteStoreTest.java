package com.weizilla.transit.favorites.sqlite;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
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

public class SqliteFavoriteStoreTest
{
    private static final Logger logger = LoggerFactory.getLogger(SqliteFavoriteStoreTest.class);
    private JdbcDatabaseTester databaseTester;
    private Path dbPath;

    @Before
    public void setUp() throws Exception
    {
        dbPath = Files.createTempFile("sqlitefavoritestoretest-", ".db");
        logger.debug("Temp db file: " + dbPath);
        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbPath);

        Connection connection = databaseTester.getConnection().getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(readSqlFile("create_routes_table.sql"));
        statement.close();
    }

    @After
    public void tearDown() throws Exception
    {
        databaseTester.onTearDown();
        Files.delete(dbPath);
    }

    @Test
    public void test() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet());
        IDataSet dataSet = databaseTester.getConnection().createDataSet();
        System.out.println(dataSet.getTable("routes"));
    }

    private static String readSqlFile(String filename) throws Exception
    {
        URL url = Resources.getResource(filename);
        return Resources.toString(url, Charsets.UTF_8);
    }

    private static IDataSet readDataSet() throws Exception
    {
        return new FlatXmlDataSetBuilder().build(SqliteFavoriteStore.class.getResourceAsStream("/get_routes.xml"));
    }
}