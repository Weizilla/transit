package com.weizilla.transit.bus;

import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.BusGroupsStoreStub;
import com.weizilla.transit.groups.Group;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BusControllerGroupsTest
{
    @Test
    public void createsGroupCallsGroupsStore() throws Exception
    {
        int newGroupId = 10;
        String groupName = "GROUP NAME";
        Group expected = new Group(newGroupId, groupName);

        BusGroupsStore store = spy(new BusGroupsStoreStub(expected));
        BusController controller = new BusController(null, null, store);

        Group actual = controller.createGroup(groupName);

        verify(store).createGroup(groupName);
        assertEquals(expected, actual);
    }
}
