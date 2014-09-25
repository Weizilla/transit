package com.weizilla.transit.groups;

import java.util.Set;

public class BusGroupsStoreStub implements BusGroupsStore
{
    private final int newGroupId;

    public BusGroupsStoreStub(int newGroupId)
    {
        this.newGroupId = newGroupId;
    }

    public int createGroup(String groupName)
    {
        return newGroupId;
    }

    @Override
    public Set<Group> getAllGroups()
    {
        //TODO Auto-generated
        return null;
    }

    @Override
    public void deleteGroup(int id)
    {
        //TODO Auto-generated
    }
}
