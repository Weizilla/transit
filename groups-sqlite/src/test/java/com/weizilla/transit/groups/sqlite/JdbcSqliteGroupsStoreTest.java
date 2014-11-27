package com.weizilla.transit.groups.sqlite;

import com.weizilla.transit.groups.sqlite.Groups.GroupEntry;
import com.weizilla.transit.groups.sqlite.Groups.StopEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JdbcSqliteGroupsStoreTest extends BaseSqliteGroupsStoreTest
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        store = JdbcSqliteGroupsStore.createStore(dbPath);
        createTable(GroupEntry.TABLE_NAME, "create_groups_table.sql");
        createTable(StopEntry.TABLE_NAME, "create_groups_stops_table.sql");
    }

    @Test
    public void createsGroupsTableDuringInitialization() throws Exception
    {
        dropTable(GroupEntry.TABLE_NAME);
        store = JdbcSqliteGroupsStore.createStore(dbPath);
        assertTrue(tableExists(GroupEntry.TABLE_NAME));
    }

    @Test
    public void createsStopsTableDuringInitialization() throws Exception
    {
        dropTable(StopEntry.TABLE_NAME);
        store = JdbcSqliteGroupsStore.createStore(dbPath);
        assertTrue(tableExists(StopEntry.TABLE_NAME));
    }
}