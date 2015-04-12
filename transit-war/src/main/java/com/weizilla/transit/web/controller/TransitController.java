package com.weizilla.transit.web.controller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

@Controller
public class TransitController
{
    private static final Logger logger = LoggerFactory.getLogger(TransitController.class);

    @RequestMapping("/now")
    @ResponseBody
    public Map<String, String> now()
    {
        logger.info("Now");
        return Collections.singletonMap("now", new DateTime().toString());
    }
}
