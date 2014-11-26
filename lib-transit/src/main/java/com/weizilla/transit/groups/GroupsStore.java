package com.weizilla.transit.groups;

import java.util.Collection;

public interface GroupsStore
{
    Group createGroup(String name);
    void renameGroup(int id, String newName);
    Collection<Group> getAllGroups();
    void deleteGroup(int id);

    void addToGroup(int groupId, int stopId);
    void removeFromGroup(int groupId, int stopId);

    Collection<Integer> getStopIds(int groupId);
}
