package com.weizilla.transit;

import com.google.common.collect.Lists;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.groups.GroupsStore;
import com.weizilla.transit.groups.GroupsStoreStub;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BusControllerGroupsTest
{
    private static final int TEST_GROUP_ID = 10;
    private static final String TEST_NAME = "GROUP NAME";
    private static final Group TEST_GROUP = new Group(TEST_GROUP_ID, TEST_NAME);
    private static final int TEST_STOP_ID = 12;

    @Test
    public void createsGroupCallsGroupsStore() throws Exception
    {
        GroupsStore store = spy(new GroupsStoreStub(TEST_GROUP));
        BusController controller = new BusController(null, null, store, null);

        Group actual = controller.createGroup(TEST_NAME);

        verify(store).createGroup(TEST_NAME);
        assertEquals(TEST_GROUP, actual);
    }

    @Test
    public void renameGroupCallsGroupStore() throws Exception
    {
        GroupsStore mockStore = mock(GroupsStore.class);
        BusController controller = new BusController(null, null, mockStore, null);

        controller.renameGroup(TEST_GROUP_ID, TEST_NAME);

        verify(mockStore).renameGroup(TEST_GROUP_ID, TEST_NAME);
    }

    @Test
    public void getsAllGroupsFromGroupStore() throws Exception
    {
        List<Group> expected = Lists.newArrayList(TEST_GROUP, TEST_GROUP);
        GroupsStoreStub stub = new GroupsStoreStub(expected);
        BusController controller = new BusController(null, null, stub, null);

        Collection<Group> actual = controller.getAllGroups();
        assertSame(expected, actual);
    }

    @Test
    public void deleteGroupCallsGroupStore() throws Exception
    {
        GroupsStore mockStore = mock(GroupsStore.class);
        BusController controller = new BusController(null, null, mockStore, null);

        controller.deleteGroup(TEST_GROUP_ID);

        verify(mockStore).deleteGroup(TEST_GROUP_ID);
    }

    @Test
    public void addStopToGroupCallsGroupStore() throws Exception
    {
        GroupsStore mockStore = mock(GroupsStore.class);
        BusController controller = new BusController(null, null, mockStore, null);

        controller.addStopToGroup(TEST_GROUP_ID, TEST_STOP_ID);

        verify(mockStore).addToGroup(TEST_GROUP_ID, TEST_STOP_ID);
    }

    @Test
    public void removeStopFromGroupCallsGroupStore() throws Exception
    {
        GroupsStore mockStore = mock(GroupsStore.class);
        BusController controller = new BusController(null, null, mockStore, null);

        controller.removeStopFromGroup(TEST_GROUP_ID, TEST_STOP_ID);

        verify(mockStore).removeFromGroup(TEST_GROUP_ID, TEST_STOP_ID);
    }
}
