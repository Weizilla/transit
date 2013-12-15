package com.weizilla.transit;

import android.app.Application;

/**
 * custom application to hold transit global vars
 *
 * @author Wei Yang
 *         Date: 12/14/13
 *         Time: 7:29 PM
 */
public class TransitApplication extends Application
{
    private String foo;

    public void setFoo(String foo)
    {
        this.foo = foo;
    }

    public String getFoo()
    {
        return foo;
    }
}
