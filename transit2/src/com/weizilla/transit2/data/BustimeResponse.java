package com.weizilla.transit2.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * holds a list of bus tracker elements
 * User: wei
 * Date: 8/22/13
 * Time: 5:47 AM
 */
@Root(name="bustime-response")
public class BustimeResponse {

    @ElementList(inline = true)
    private List<Prediction> predictions;

    public List<Prediction> getPredictions() {
        return predictions;
    }
}
