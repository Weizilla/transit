package com.weizilla.transit.bus.data;

import org.simpleframework.xml.Root;

@Root(name = "dir")
public enum Direction
{
    Northbound,
    Eastbound,
    Southbound,
    Westbound
}
