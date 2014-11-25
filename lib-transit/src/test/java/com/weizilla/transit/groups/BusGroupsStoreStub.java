package com.weizilla.transit.groups;

import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;
import java.util.List;

public class BusGroupsStoreStub implements BusGroupsStore
{
    private int groupId;
    private List<Stop> stops;
    private Group group;
    private List<Group> groups;

    public BusGroupsStoreStub(Group group)
    {
        this.group = group;
    }

    public BusGroupsStoreStub(List<Group> groups)
    {
        this.groups = groups;
    }

    public BusGroupsStoreStub(int groupId, List<Stop> stops)
    {
        this.groupId = groupId;
        this.stops = stops;
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

    @Override
    public Collection<Stop> getStops(int groupId)
    {
        if (this.groupId == groupId)
        {
            return stops;
        }
        else
        {
            throw new RuntimeException("getStops() argument does not match stub input");
        }
    }
}
