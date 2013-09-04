package com.weizilla.transit2.data;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 9/3/13
 *         Time: 9:20 PM
 */
@Root(name = "dir")
public class Direction {
    public static final String KEY = Direction.class.getName();

    @Text
    private String name;

    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "name='" + name + '\'' +
                '}';
    }

}
