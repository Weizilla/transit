package com.weizilla.transit.groups;

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
        //TODO Auto-generated
        return null;
    }

    @Override
    public void deleteGroup(int id)
    {
        //TODO Auto-generated
    }
}
