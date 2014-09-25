package com.weizilla.transit.groups;

public class Group
{
    private int id;
    private String name;

    public Group(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Group group = (Group) o;

        if (id != group.id)
        {
            return false;
        }
        if (name != null ? !name.equals(group.name) : group.name != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Group{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
