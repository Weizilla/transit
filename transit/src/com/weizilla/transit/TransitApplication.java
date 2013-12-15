package com.weizilla.transit;

import android.app.Application;
import com.weizilla.transit.dataproviders.TransitDataProvider;

/**
 * custom application to hold transit global vars
 *
 * @author Wei Yang
 *         Date: 12/14/13
 *         Time: 7:29 PM
 */
public class TransitApplication extends Application
{
    private TransitDataProvider transitDataProvider;

    public void setTransitDataProvider(TransitDataProvider transitDataProvider)
    {
        this.transitDataProvider = transitDataProvider;
    }

    public TransitDataProvider getTransitDataProvider()
    {
        return transitDataProvider;
    }
}