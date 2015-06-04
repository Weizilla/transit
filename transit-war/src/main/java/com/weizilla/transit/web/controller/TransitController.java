package com.weizilla.transit.web.controller;

import com.google.common.collect.ImmutableMap;
import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.web.config.BusControllerFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;

@Controller
public class TransitController
{
    private static final Logger logger = LoggerFactory.getLogger(TransitController.class);
    private BusControllerFactory busControllerFactory;
    private BusController busController;

    @Autowired
    public TransitController(BusControllerFactory busControllerFactory)
    {
        this.busControllerFactory = busControllerFactory;
    }

    @PostConstruct
    public void init()
    {
        busController = busControllerFactory.create();
    }

    @RequestMapping("/now")
    @ResponseBody
    public Map<String, Long> now()
    {
        logger.info("Now");
        return ImmutableMap.<String, Long>builder()
            .put("cta", busController.getCurrentTime().getMillis())
            .put("server", new DateTime().getMillis())
            .build();
    }

    @RequestMapping("/routes")
    @ResponseBody
    public Collection<Route> routes()
    {
        return busController.getRoutes();
    }
}
