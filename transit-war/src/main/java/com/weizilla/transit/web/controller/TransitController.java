package com.weizilla.transit.web.controller;

import com.google.common.base.Strings;
import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.web.config.BusControllerFactory;
import com.weizilla.transit.web.config.NoCtaApiKeyException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BusControllerFactory busControllerFactory;

    private BusController busController;

    @PostConstruct
    public void init()
    {
        busController = busControllerFactory.create();
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

    private static void checkForKey(String apiKey)
    {
        if (Strings.isNullOrEmpty(apiKey))
        {
            throw new NoCtaApiKeyException();
        }
    }
}
