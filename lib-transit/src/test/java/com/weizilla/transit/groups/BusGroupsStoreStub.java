package com.weizilla.transit.groups;

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
}
