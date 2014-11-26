package com.weizilla.transit.source;

public class DataSourceException extends RuntimeException
{
    public DataSourceException(String message)
    {
        super(message);
    }

    public DataSourceException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
