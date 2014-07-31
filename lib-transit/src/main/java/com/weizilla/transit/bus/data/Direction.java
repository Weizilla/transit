package com.weizilla.transit.bus.data;

import org.simpleframework.xml.Root;

@Root(name = "dir", strict = false)
public enum Direction
{
    Northbound,
    Eastbound,
    Southbound,
    Westbound
}
