package com.weizilla.transit.web.utils;

import com.google.common.base.Charsets;
import org.springframework.http.MediaType;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TestUtils
{
    public static final MediaType APPLICATION_JSON_UTF_8 =
        new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), Charsets.UTF_8);

    private TestUtils()
    {
        // static util class
    }
}
