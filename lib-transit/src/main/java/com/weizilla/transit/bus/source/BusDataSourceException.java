package com.weizilla.transit.bus.source;

public class BusDataSourceException extends RuntimeException
{
    public BusDataSourceException(String message)
    {
        super(message);
    }

    public BusDataSourceException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
