package com.weizilla.transit2.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 9/4/13
 *         Time: 5:39 AM
 */
@Root(name="stop")
public class Stop {
    public static final String KEY = Stop.class.getName();

    @Element(name="stpid")
    public int id;

    @Element(name="stpnm")
    public String name;

    @Element(name="lat")
    public double latitude;

    @Element(name="lon")
    public double longitude;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
