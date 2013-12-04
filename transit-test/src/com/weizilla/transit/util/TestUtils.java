package com.weizilla.transit.util;

/**
 * generic test utilities
 *
 * @author wei
 *         Date: 12/4/13
 *         Time: 7:54 AM
 */
public class TestUtils
{
    private TestUtils()
    {
        // empty
    }

    public static void sleep(int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e)
        {
            // ignore
        }
    }
}
