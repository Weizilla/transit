package com.weizilla.spike.sqlite;

/**
 * pojo to represent a fruit
 *
 * @author wei
 *         Date: 11/16/13
 *         Time: 3:26 PM
 */
public class Fruit
{
    private String name;
    private String size;

    public Fruit(String name, String size)
    {
        this.name = name;
        this.size = size;
    }

    public String getName()
    {
        return name;
    }

    public String getSize()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
