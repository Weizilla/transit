package com.weizilla.transit.groups.sqlite;

public class InvalidGroupException extends RuntimeException
{
    private final int id;

    public InvalidGroupException(int id, Throwable t)
    {
        super(t);
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
