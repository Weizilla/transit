package com.weizilla.transit.groups.sqlite;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Sets;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.groups.GroupsStore;
import com.weizilla.transit.groups.sqlite.Groups.GroupEntry;
import com.weizilla.transit.groups.sqlite.Groups.StopEntry;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public abstract class BaseSqliteGroupsStoreTest extends BaseSqliteTest
{
    protected GroupsStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void createsGroups() throws Exception
    {
        String dataSetFile = "groups/create_groups.xml";
        deleteFromDb(dataSetFile);

        for (int i = 0; i < 3; i++)
        {
            String groupName = "GROUP " + i;
            Group expectedGroup = new Group(i + 1, groupName);
            Group actual = store.createGroup(groupName);
            assertEquals(expectedGroup, actual);
        }

        assertTablesEqualFile(dataSetFile, GroupEntry.TABLE_NAME);
    }

    @Test
    public void throwsErrorWhenDuplicateGroupName() throws Exception
    {
        Logger logger = (Logger) LoggerFactory.getLogger(store.getClass());
        loadIntoDb("groups/create_groups.xml");

        String dupGroupName = "GROUP 0";
        try
        {
            logger.setLevel(Level.OFF);
            store.createGroup(dupGroupName);
            fail("Should have thrown exception for duplicate group name");
        }
        catch (DuplicateGroupException e)
        {
            assertEquals(dupGroupName, e.getGroupName());
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
        loadIntoDb("groups/get_all_groups.xml");

        Collection<Group> actualGroups = store.getAllGroups();
        assertEquals(expected, actualGroups);
    }

    @Test
    public void deleteGroupWithoutStop() throws Exception
    {
        loadIntoDb("groups/delete_group_before.xml");
        store.deleteGroup(8);
        assertTablesEqualFile("groups/delete_group_after.xml", GroupEntry.TABLE_NAME);
    }

    @Test
    public void deleteGroupWithStops() throws Exception
    {
        loadIntoDb("groups/delete_group_w_stops_before.xml");
        store.deleteGroup(2);
        assertTablesEqualFile("groups/delete_group_w_stops_after.xml",
            GroupEntry.TABLE_NAME, StopEntry.TABLE_NAME);
    }

    @Test
    public void addsStopsToGroup() throws Exception
    {
        loadIntoDb("groups/create_groups.xml");
        String dataSetFile = "groups/add_stop.xml";
        deleteFromDb(dataSetFile);

        store.addToGroup(2, 400);
        store.addToGroup(1, 100);
        store.addToGroup(2, 200);
        store.addToGroup(3, 300);

        assertTablesEqualFile(dataSetFile, StopEntry.TABLE_NAME);
    }

    @Test
    public void replacesDuplicateStopWithNewestName() throws Exception
    {
        loadIntoDb("groups/create_groups.xml");
        String dataSetFile = "groups/add_stop.xml";
        deleteFromDb(dataSetFile);

        store.addToGroup(3, 300);
        store.addToGroup(2, 400);
        store.addToGroup(3, 300);
        store.addToGroup(1, 100);
        store.addToGroup(2, 200);
        store.addToGroup(3, 300);
        store.addToGroup(3, 300);

        assertTablesEqualFile(dataSetFile, StopEntry.TABLE_NAME);
    }

    @Test
    public void addStopToInvalidGroupIdThrowException() throws Exception
    {
        int invalidGroupId = 100;
        try
        {

            store.addToGroup(invalidGroupId, 200);
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
        loadIntoDb("groups/remove_stop_before.xml");
        store.removeFromGroup(2, 200);
        assertTablesEqualFile("groups/remove_stop_after.xml", StopEntry.TABLE_NAME);
    }

    @Test
    public void getStopsForGroup() throws Exception
    {
        loadIntoDb("groups/get_stops.xml");
        Set<Integer> expected = Sets.newHashSet(200, 400);

        Collection<Integer> actual = store.getStopIds(2);
        assertEquals(expected, actual);
    }

    @Test
    public void renameGroup() throws Exception
    {
        loadIntoDb("groups/rename_group_before.xml");
        store.renameGroup(2, "NEW GROUP");
        assertTablesEqualFile("groups/rename_group_after.xml", GroupEntry.TABLE_NAME);
    }
}