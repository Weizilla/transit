package com.weizilla.transit2;

import java.util.List;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:41 PM
 */
public interface TransitDataProvider {
    public List<Prediction> getPredictions(List<Integer> stops, List<Integer> routes);
}
