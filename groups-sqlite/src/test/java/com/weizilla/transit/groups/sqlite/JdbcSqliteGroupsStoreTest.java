package com.weizilla.transit.groups.sqlite;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class JdbcSqliteGroupsStoreTest extends BaseSqliteGroupsStoreTest
{
    private static final String GROUPS_TABLE_NAME = "groups";

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteGroupsStore.createStore(dbPath);
        createTable(GROUPS_TABLE_NAME, "create_groups_table.sql");
    }

    @Test
    public void createsGroupsTableDuringInitialization() throws Exception
    {
        dropTable(GROUPS_TABLE_NAME);
        JdbcSqliteGroupsStore.createStore(dbPath);
        assertNotNull(getTable(GROUPS_TABLE_NAME));
    }

    @Test
    public void createsGroupsTableIfDoesNotExist() throws Exception
    {
        dropTable(GROUPS_TABLE_NAME);

        IDatabaseConnection connection = null;
        try
        {
            connection = databaseTester.getConnection();
            try
            (
                Connection conn = connection.getConnection()
            )
            {
                JdbcSqliteGroupsStore.createGroupsTable(conn);
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }

        assertNotNull(getTable(GROUPS_TABLE_NAME));
    }
}