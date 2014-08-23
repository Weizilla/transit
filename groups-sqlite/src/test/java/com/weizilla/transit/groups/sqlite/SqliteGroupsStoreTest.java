package com.weizilla.transit.groups.sqlite;

import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class SqliteGroupsStoreTest extends SqliteTest
{
    private static final String GROUPS_TABLE_NAME = "groups";

    @Test
    public void createsGroupsTableDuringInitialization() throws Exception
    {
        executeSql("drop_groups_table.sql");
        assertArrayEquals(EMPTY, databaseTester.getConnection().createDataSet().getTableNames());
        SqliteGroupsStore.createStore(dbPath);
        assertNotNull(getTable(GROUPS_TABLE_NAME));
    }

    @Test
    public void createsGroupsTableIfDoesNotExist() throws Exception
    {
        executeSql("drop_groups_table.sql");
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
                SqliteGroupsStore.createGroupsTable(conn);
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

    @Test
    public void createsGroups() throws Exception
    {
        SqliteGroupsStore store = SqliteGroupsStore.createStore(dbPath);
        executeSql("create_groups_table.sql");

        IDataSet expected = readDataSet("create_groups.xml");
        ITable expectedTable = expected.getTable(GROUPS_TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (int i = 0; i < 3; i++)
        {
            String groupName = "GROUP " + i;
            store.createGroup(groupName);
        }

        ITable actual = getTable(GROUPS_TABLE_NAME);

        Assertion.assertEquals(expectedTable, actual);
    }
}