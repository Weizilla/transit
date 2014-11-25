package com.weizilla.transit.groups.sqlite;

public class DuplicateGroupException extends RuntimeException
{
    private final String groupName;

    public DuplicateGroupException(String groupName, Throwable t)
    {
        super(t);
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
}
