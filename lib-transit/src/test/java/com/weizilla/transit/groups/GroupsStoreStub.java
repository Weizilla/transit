package com.weizilla.transit.groups;

import java.util.Collection;
import java.util.List;

public class GroupsStoreStub implements GroupsStore
{
    private int groupId;
    private List<Integer> stopIds;
    private Group group;
    private List<Group> groups;

    public GroupsStoreStub(Group group)
    {
        this.group = group;
    }

    public GroupsStoreStub(List<Group> groups)
    {
        this.groups = groups;
    }

    public GroupsStoreStub(int groupId, List<Integer> stopIds)
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
