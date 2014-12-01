package com.weizilla.transit.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceUtilsTest
{
    private static final String EXPECTED = "RESOURCE TEST TEXT"
        + System.lineSeparator() + "MULTI LINE";
    private static final String FILENAME = "lib/resource-test.txt";

    @Test
    public void readsFileAsStringWithFilename() throws Exception
    {
        String actual = ResourceUtils.readFile(FILENAME);
        assertEquals(EXPECTED, actual);
    }
}