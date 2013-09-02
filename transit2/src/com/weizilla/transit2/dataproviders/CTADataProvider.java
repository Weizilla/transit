package com.weizilla.transit2.dataproviders;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * provides data via cta web service
 *
 * @author wei
 * Date: 8/26/13
 * Time: 9:50 PM
 */
public class CTADataProvider implements TransitDataProvider
{
    private static final String TAG = "CTADataProvider";
    private String apiKey;

    public CTADataProvider(String apiKey)
    {
        this.apiKey = apiKey;
    }

    @Override
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes)
    {
        String stopsStr = TextUtils.join(",", stops);
        String urlString = "http://www.ctabustracker.com/bustime/api/v1/getpredictions?key=" + apiKey + "&stpid=" + stopsStr;
        if (routes != null && ! routes.isEmpty())
        {
            String routesStr = TextUtils.join(",", routes);
            urlString += routesStr;
        }

        try
        {
            return callCTAServer(urlString);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }

        //TODO better error handling
        return null;
    }

    @Override
    public InputStream getRoutes()
    {
        String urlString = "http://www.ctabustracker.com/bustime/api/v1/getroutes?key=" + apiKey;

        try
        {
            return callCTAServer(urlString);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }

        //TODO better error handling
        return null;
    }

    private InputStream callCTAServer(String urlString) throws IOException
    {
        InputStream is;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // starts query
        conn.connect();
        int response = conn.getResponseCode();
        Log.d(TAG, "Response code: " + response);

        is = conn.getInputStream();
        return is;
    }
}
