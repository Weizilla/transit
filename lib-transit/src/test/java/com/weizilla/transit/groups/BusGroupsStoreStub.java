package com.weizilla.transit.groups;

import com.weizilla.transit.bus.data.Stop;

import java.util.Set;

public class BusGroupsStoreStub implements BusGroupsStore
{
    private int newGroupId;
    private Group group;

    public BusGroupsStoreStub(int newGroupId)
    {
        this.newGroupId = newGroupId;
    }

    public BusGroupsStoreStub(Group group)
    {
        this.group = group;
    }

    public Group createGroup(String groupName)
    {
        return group;
    }

    @Override
    public Set<Group> getAllGroups()
    {
        throw new RuntimeException("not used in stub");
    }

    @Override
    public void deleteGroup(int id)
    {
        throw new RuntimeException("not used in stub");
    }

    @Override
    public void addToGroup(int groupId, Stop stop)
    {
        throw new RuntimeException("not used in stub");
    }

    @Override
    public void renameGroup(int id, String newName)
    {
        throw new RuntimeException("not used in stub");
    }

    @Override
    public void removeFromGroup(int groupId, int stopId)
    {
        throw new RuntimeException("not used in stub");
    }
}
