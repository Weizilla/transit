package com.weizilla.transit.web.controller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TransitController
{
    private static final Logger logger = LoggerFactory.getLogger(TransitController.class);

    @RequestMapping("/")
    @ResponseBody
    public String index()
    {
        logger.info("Index site");
        return "API";
    }

    @RequestMapping("/now")
    @ResponseBody
    public String now()
    {
        logger.info("Now");
        return new DateTime().toString();
    }
}
