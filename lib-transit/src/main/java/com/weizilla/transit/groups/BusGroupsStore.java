package com.weizilla.transit.groups;

import com.weizilla.transit.bus.data.Stop;

import java.util.Set;

public interface BusGroupsStore
{
    Group createGroup(String name);
    Set<Group> getAllGroups();
    void deleteGroup(int id);
    void addToGroup(int id, Stop stop);
}
