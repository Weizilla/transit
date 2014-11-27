package com.weizilla.transit.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Error
{
//    @Element(name = "rt")
//    private String routeId;
//
//    @Element(name = "stpid")
//    private int stopId;

    @Element(name = "msg")
    private String message;

    public String getMessage()
    {
        return message;
    }
}
