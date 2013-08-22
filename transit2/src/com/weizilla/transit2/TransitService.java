package com.weizilla.transit2;

import android.util.Log;
import com.weizilla.transit2.data.BustimeResponse;
import com.weizilla.transit2.data.Prediction;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:40 PM
 */
public class TransitService {
    private static final String TAG = "TransitService";
    private TransitDataProvider dataProvider;

    public List<Prediction> getPredictions(List<Integer> stops, List<Integer> routes)
    {
        InputStream inputStream = dataProvider.getPredictions(stops, routes);
        Serializer seralizer = new Persister();
        try {
            BustimeResponse response = seralizer.read(BustimeResponse.class, inputStream);
            return response.getPredictions();
        } catch (Exception e) {
            //TODO find logging framework that works in junit and android
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public void setDataProvider(TransitDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
