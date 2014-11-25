package com.weizilla.transit.groups.sqlite;

import org.junit.Before;
import org.junit.Test;

import static com.weizilla.transit.groups.sqlite.Groups.GroupEntry.TABLE_NAME;
import static org.junit.Assert.assertNotNull;

public class JdbcSqliteGroupsStoreTest extends BaseSqliteGroupsStoreTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteGroupsStore.createStore(dbPath);
        createTable(TABLE_NAME, "create_groups_table.sql");
    }

    @Test
    public void createsGroupsTableDuringInitialization() throws Exception
    {
        dropTable(TABLE_NAME);
        store = JdbcSqliteGroupsStore.createStore(dbPath);
        assertNotNull(getTable(TABLE_NAME));
    }
}