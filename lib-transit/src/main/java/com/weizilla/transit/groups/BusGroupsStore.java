package com.weizilla.transit.groups;

import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public interface BusGroupsStore
{
    Group createGroup(String name);
    void renameGroup(int id, String newName);
    Collection<Group> getAllGroups();
    void deleteGroup(int id);

    void addToGroup(int groupId, Stop stop);
    void removeFromGroup(int groupId, int stopId);

    Collection<Stop> getStops(int groupId);
}
