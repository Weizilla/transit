package com.weizilla.transit2.dataproviders;

import java.io.InputStream;
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

    @Override
    public InputStream getPredictions(List<Integer> stops, List<Integer> routes) {
        throw new NoSuchMethodError(" not implemented");
    }

    @Override
    public InputStream getRoutes() {
        throw new NoSuchMethodError(" not implemented");
    }

}
