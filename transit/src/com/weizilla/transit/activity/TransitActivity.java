package com.weizilla.transit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.weizilla.transit.TransitApplication;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;

/**
 * Holds common activity stuff for all transit activities
 *
 * @author Wei Yang
 *         Date: 12/15/13
 *         Time: 12:54 PM
 */
public class TransitActivity extends Activity
{
    protected TransitService transitService;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        TransitApplication transitApplication = (TransitApplication) getApplication();
        TransitDataProvider transitDataProvider = transitApplication.getTransitDataProvider();
        if (transitDataProvider == null)
        {
            transitDataProvider = new CTADataProvider(this);
        }
        transitService = new TransitService(transitDataProvider);
    }

    protected void setProgressBar(int id)
    {
        progressBar = (ProgressBar) findViewById(id);
    }

    protected void showProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void dismissProgress()
    {
        progressBar.setVisibility(View.GONE);
    }
}
