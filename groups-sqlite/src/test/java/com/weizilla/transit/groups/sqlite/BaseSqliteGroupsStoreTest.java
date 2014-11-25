package com.weizilla.transit.groups.sqlite;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Sets;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.groups.sqlite.Groups.GroupEntry;
import com.weizilla.transit.groups.sqlite.Groups.StopEntry;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public abstract class BaseSqliteGroupsStoreTest extends BaseSqliteTest
{
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
        ITable expectedTable = expected.getTable(GroupEntry.TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (int i = 0; i < 3; i++)
        {
            String groupName = "GROUP " + i;
            Group expectedGroup = new Group(i + 1, groupName);
            Group actual = store.createGroup(groupName);
            assertEquals(expectedGroup, actual);
        }

        ITable actual = getTable(GroupEntry.TABLE_NAME);

        Assertion.assertEquals(expectedTable, actual);
    }

    @Test
    public void throwsErrorWhenDuplicateGroupName() throws Exception
    {
        Logger logger = (Logger) LoggerFactory.getLogger(store.getClass());

        IDataSet expected = readDataSet("create_groups.xml");
        ITable expectedTable = expected.getTable(GroupEntry.TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (int i = 0; i < 3; i++)
        {
            String groupName = "GROUP " + i;
            store.createGroup(groupName);
        }

        ITable actual = getTable(GroupEntry.TABLE_NAME);
        Assertion.assertEquals(expectedTable, actual);

        String groupName = "GROUP 0";
        try
        {
            logger.setLevel(Level.OFF);

            store.createGroup(groupName);
            fail("Should have thrown exception for duplicate group name");
        }
        catch (DuplicateGroupException e)
        {
            assertEquals(groupName, e.getGroupName());
        }
        finally
        {
            logger.setLevel(null);
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
    public void deleteGroupWithoutStop() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("delete_group_before.xml"));

        store.deleteGroup(8);

        IDataSet expected = readDataSet("delete_group_after.xml");
        ITable expectedTable = expected.getTable(GroupEntry.TABLE_NAME);
        ITable actual = getTable(GroupEntry.TABLE_NAME);

        Assertion.assertEquals(expectedTable, actual);
    }

    @Test
    public void deleteGroupWithStops() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("delete_group_w_stops_before.xml"));

        store.deleteGroup(2);

        IDataSet expected = readDataSet("delete_group_w_stops_after.xml");
        IDataSet actual = getDataSet();

        assertTableEquals(GroupEntry.TABLE_NAME, expected, actual);
        assertTableEquals(StopEntry.TABLE_NAME, expected, actual);
    }

    @Test
    public void addsStopsToGroup() throws Exception
    {
        IDataSet expected = readDataSet("add_stop.xml");

        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("create_groups.xml"));

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        store.addToGroup(2, new Stop(400, "STOP C"));
        store.addToGroup(1, new Stop(100, "STOP A"));
        store.addToGroup(2, new Stop(200, "STOP B"));
        store.addToGroup(3, new Stop(300, "STOP D"));

        assertTableInDbEqualsFile(StopEntry.TABLE_NAME, "add_stop.xml");
    }

    @Test
    public void replacesDuplicateStopWithNewestName() throws Exception
    {
        IDataSet expected = readDataSet("add_stop.xml");
        ITable expectedTable = expected.getTable(StopEntry.TABLE_NAME);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("create_groups.xml"));
        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        store.addToGroup(3, new Stop(300, "STOP D1"));
        store.addToGroup(2, new Stop(400, "STOP C"));
        store.addToGroup(3, new Stop(300, "STOP D2"));
        store.addToGroup(1, new Stop(100, "STOP A"));
        store.addToGroup(2, new Stop(200, "STOP B"));
        store.addToGroup(3, new Stop(300, "STOP D3"));
        store.addToGroup(3, new Stop(300, "STOP D"));

        ITable actual = getTable(StopEntry.TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual,
            expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(new SortedTable(expectedTable),
            new SortedTable(actualFiltered));
    }

    @Test
    public void addStopToInvalidGroupIdThrowException() throws Exception
    {
        int invalidGroupId = 100;
        try
        {

            store.addToGroup(invalidGroupId, new Stop(200, "STOP A"));
            fail("Exception expected for invalid group id");
        }
        catch (InvalidGroupException e)
        {
            assertEquals(invalidGroupId, e.getId());
        }
    }

    @Test
    public void removeStopFromGroup() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("remove_stop_before.xml"));

        store.removeFromGroup(2, 200);

        IDataSet expected = readDataSet("remove_stop_after.xml");
        ITable expectedTable = expected.getTable(StopEntry.TABLE_NAME);
        ITable actual = getTable(StopEntry.TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual,
            expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(new SortedTable(expectedTable),
            new SortedTable(actualFiltered));
    }

    @Test
    public void getStopsForGroup() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("get_stops.xml"));
        Set<Stop> expected = Sets.newHashSet(
            new Stop(200, "STOP B"),
            new Stop(400, "STOP C")
        );

        Collection<Stop> actual = store.getStops(2);
        assertEquals(expected, actual);
    }

    @Test
    public void renameGroup() throws Exception
    {
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("rename_group_before.xml"));

        store.renameGroup(2, "NEW GROUP");

        assertTableInDbEqualsFile(GroupEntry.TABLE_NAME, "rename_group_after.xml");
    }
}