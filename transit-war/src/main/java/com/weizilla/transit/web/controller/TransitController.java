package com.weizilla.transit.web.controller;

import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.source.DataSource;
import com.weizilla.transit.source.stream.InputStreamProvider;
import com.weizilla.transit.source.stream.StreamingDataSource;
import com.weizilla.transit.source.stream.http.HttpInputStreamProvider;
import com.weizilla.transit.source.stream.http.HttpReader;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
public class TransitController
{
    private static final Logger logger = LoggerFactory.getLogger(TransitController.class);

    protected BusController busController;

    @PostConstruct
    public void init()
    {
        String apiKey = System.getProperty("CTA_API_KEY");
        HttpReader httpReader = new HttpReader(new HttpReader.HttpURLConnectionFactory());
        InputStreamProvider inputStreamProvider = new HttpInputStreamProvider(httpReader, apiKey);
        DataSource dataSource = new StreamingDataSource(inputStreamProvider);
        busController = new BusController(dataSource, null, null, null);
    }

    @RequestMapping("/now")
    @ResponseBody
    public Map<String, String> now()
    {
        logger.info("Now");
        return Collections.singletonMap("now", new DateTime().toString());
    }

    @RequestMapping("/routes")
    @ResponseBody
    public Collection<Route> routes()
    {
        return busController.getRoutes();
    }

    public void setBusController(BusController busController)
    {
        this.busController = busController;
    }
}
