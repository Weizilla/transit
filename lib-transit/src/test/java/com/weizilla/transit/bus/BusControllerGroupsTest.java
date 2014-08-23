package com.weizilla.transit.bus;

import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.BusGroupsStoreStub;
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

        BusGroupsStore store = spy(new BusGroupsStoreStub(newGroupId));
        BusController controller = new BusController(null, null, store);

        int actual = controller.createGroup(groupName);

        verify(store).createGroup(groupName);
        assertEquals(newGroupId, actual);
    }
}
