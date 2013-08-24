package com.weizilla.spike.network;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkSpike extends Activity {
    private TextView messageView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.messageView = (TextView) findViewById(R.id.messageDisplay);

    }

    private boolean isConnected()
    {
        ConnectivityManager connManger = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManger.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void connect(View view)
    {
        if (! isConnected())
        {
            this.messageView.setText("Not connected");
            return;
        }

        String url = "http://httpbin.org/ip";
        new DownloadTask(messageView).execute(url);


    }
}

class DownloadTask extends AsyncTask<String, Void, String>
{
    private TextView resultsView;

    public DownloadTask(TextView resultsView)
    {
        this.resultsView = resultsView;
    }

    @Override
    protected String doInBackground(String... urls)
    {
        try
        {
            return download(urls[0]);
        }
        catch (IOException e)
        {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String results)
    {
        this.resultsView.setText(results);
    }

    private String download(String urlString) throws IOException {
        InputStream is = null;
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // starts query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DownloadTask", "Response code: " + response);
            is = conn.getInputStream();

            String content = convert(is);
            return content;
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    public String convert(InputStream inputStream) throws IOException
{
        //TODO replace with apache io
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null)
        {
            buffer.append(line);
        }

        return buffer.toString();
    }
}