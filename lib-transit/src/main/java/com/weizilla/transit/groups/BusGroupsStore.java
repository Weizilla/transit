package com.weizilla.transit.groups;

import java.util.Set;

public interface BusGroupsStore
{
    Group createGroup(String name);
    Set<Group> getAllGroups();
    void deleteGroup(int id);
}
