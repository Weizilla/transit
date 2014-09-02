package com.weizilla.transit.groups.sqlite;

public class DuplicateGroupException extends RuntimeException
{
    private String groupName;

    public DuplicateGroupException(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
}
