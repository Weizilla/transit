package com.weizilla.transit2;

import com.weizilla.transit2.data.Prediction;

import java.io.InputStream;
import java.util.List;

/**
 * TODO auto-generated header
 *
 * @author wei
 * Date: 8/18/13
 * Time: 5:41 PM
 */
public interface TransitDataProvider {
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes);
    public InputStream getRoutes();
}
