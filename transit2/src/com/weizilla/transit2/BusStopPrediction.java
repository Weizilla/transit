package com.weizilla.transit2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit2.data.BustimeResponse;
import com.weizilla.transit2.data.Prediction;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity for finding the predictions for a single bus stop
 *
 * @author Wei Yang
 * Date: 8/26/13
 * Time: 7:26 PM
 */
public class BusStopPrediction extends Activity {
    private TransitService transitService; //TODO
    private String ctaApiKey;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bus_prediction);

        this.ctaApiKey = getString(R.string.ctaAPI);

    }

    public void lookupPredictions(View view)
    {
        EditText busStopIdInput = (EditText) findViewById(R.id.uiBusStopIDInput);
        int busStopId = Integer.parseInt(busStopIdInput.getText().toString());

        List<String> predictions = retrievePredictions(busStopId);
        ArrayAdapter<String> predictionsAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, predictions);

        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiPredictionList);
        uiPredictionsDisplay.setAdapter(predictionsAdapter);
    }

    private List<String> retrievePredictions(int busStopId)
    {
        List<Integer> stops = Collections.singletonList(busStopId);

        List<Prediction> predictions = transitService.getPredictions(stops, null);


        List<String> results = new ArrayList<String>(predictions.size());
        for (Prediction prediction : predictions)
        {
            results.add(prediction.toString());
        }
        return results;
    }

}



class GetPredictionTask extends AsyncTask<String, Void, List<Prediction>>
{
    private Serializer serializer = new Persister();
    private String ctaApiKey;

    public GetPredictionTask(String ctaApiKey, List<Prediction> results)
    {
        this.ctaApiKey = ctaApiKey;
    }

    @Override
    protected List<Prediction> doInBackground(String... busStopIds)
    {
        try
        {
            String urlString = "http://www.ctabustracker.com/bustime/api/v1/getpredictions?key="
                    + ctaApiKey + "&stpid=" + busStopIds[0];

            return download(urlString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    @Override
    protected void onPostExecute(List<Prediction> results)
    {
        this.resultsView.setText(results);
    }

    private List<Prediction> download(String urlString) throws Exception {
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

            List<Prediction> results = parseResults(is);
            return results;
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    public List<Prediction> parseResults(InputStream inputStream) throws Exception
    {
        BustimeResponse response = serializer.read(BustimeResponse.class, inputStream);
        return response.getPredictions();
    }
}