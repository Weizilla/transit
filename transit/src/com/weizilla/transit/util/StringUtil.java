package com.weizilla.transit.util;

/**
 * various string util functions
 *
 * @author wei
 *         Date: 11/28/13
 *         Time: 2:29 PM
 */
public class StringUtil
{
    private StringUtil()
    {
        // empty
    }

    public static int compare(String lhs, String rhs)
    {
        if (lhs == null && rhs == null)
        {
            return 0;
        }
        else if (lhs == null)
        {
            return -1;
        }
        else if (rhs == null)
        {
            return 1;
        }
        else
        {
            return lhs.compareTo(rhs);
        }
    }
}
