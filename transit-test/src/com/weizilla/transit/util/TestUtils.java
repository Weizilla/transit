package com.weizilla.transit.util;

import android.app.Instrumentation;
import com.weizilla.transit.TransitApplication;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;

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

    public static void setMockTransitDataProvider(Instrumentation instrumentation)
    {
        TransitApplication app = (TransitApplication) instrumentation.getTargetContext().getApplicationContext();
        app.setTransitDataProvider(new MockTransitDataProvider());
    }
}
