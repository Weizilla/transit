package com.weizilla.transit.groups;

import java.util.Collection;

public class GroupsStoreStub implements GroupsStore
{
    private int groupId;
    private Collection<Integer> stopIds;
    private Group group;
    private Collection<Group> groups;

    public GroupsStoreStub(Group group)
    {
        this.group = group;
    }

    public GroupsStoreStub(Collection<Group> groups)
    {
        this.groups = groups;
    }

    public GroupsStoreStub(int groupId, Collection<Integer> stopIds)
    {
        this.groupId = groupId;
        this.stopIds = stopIds;
    }

    public Group createGroup(String groupName)
    {
        return group;
    }

    @Override
    public Collection<Group> getAllGroups()
    {
        return groups;
    }

    @Override
    public void deleteGroup(int id)
    {
        throw new RuntimeException("not used in stub");
    }

    @Override
    public void addToGroup(int groupId, int stopId)
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

    @Override
    public Collection<Integer> getStopIds(int groupId)
    {
        if (this.groupId == groupId)
        {
            return stopIds;
        }
        else
        {
            throw new RuntimeException("getStopIdsForGroup() argument does not match stub input");
        }
    }
}
