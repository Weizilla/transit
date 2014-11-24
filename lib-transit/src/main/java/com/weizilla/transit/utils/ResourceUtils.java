package com.weizilla.transit.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public class ResourceUtils
{
    private ResourceUtils()
    {
        // do nothing
    }

    public static String readFile(String filename) throws IOException
    {
        URL url = Resources.getResource(filename);
        return Resources.toString(url, Charsets.UTF_8);
    }
}
