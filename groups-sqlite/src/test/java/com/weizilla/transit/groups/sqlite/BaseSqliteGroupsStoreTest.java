package com.weizilla.transit.groups.sqlite;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Sets;
import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public abstract class BaseSqliteGroupsStoreTest extends BaseSqliteTest
{
    private static final String GROUPS_TABLE_NAME = "groups";
    protected BusGroupsStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void createsGroups() throws Exception
    {
        IDataSet expected = readDataSet("create_groups.xml");
        ITable expectedTable = expected.getTable(GROUPS_TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (int i = 0; i < 3; i++)
        {
            String groupName = "GROUP " + i;
            Group expectedGroup = new Group(i + 1, groupName);
            Group actual = store.createGroup(groupName);
            assertEquals(expectedGroup, actual);
        }

        ITable actual = getTable(GROUPS_TABLE_NAME);

        Assertion.assertEquals(expectedTable, actual);
    }

    @Test
    public void throwsErrorWhenDuplicateGroupName() throws Exception
    {
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

        String groupName = "GROUP 0";
        try
        {
            Logger logger = (Logger) LoggerFactory.getLogger(JdbcSqliteGroupsStore.class);
            logger.setLevel(Level.OFF);

            store.createGroup(groupName);
            fail("Should have thrown exception for duplicate group name");
        }
        catch (DuplicateGroupException e)
        {
            assertEquals(groupName, e.getGroupName());
        }
    }

    @Test
    public void getAllGroupsReturnsDbData() throws Exception
    {
        Set<Group> expected = Sets.newHashSet(
            new Group(4, "GROUP 4"),
            new Group(5, "GROUP 5"),
            new Group(6, "GROUP 6")
        );

        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("get_all_groups.xml"));

        Set<Group> actualGroups = store.getAllGroups();
        assertEquals(expected, actualGroups);
    }

    @Test
    public void deleteGroupDeletesGroupFromDb() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("delete_group_before.xml"));

        store.deleteGroup(8);

        IDataSet expected = readDataSet("delete_group_after.xml");
        ITable expectedTable = expected.getTable(GROUPS_TABLE_NAME);
        ITable actual = getTable(GROUPS_TABLE_NAME);

        Assertion.assertEquals(expectedTable, actual);
    }
}