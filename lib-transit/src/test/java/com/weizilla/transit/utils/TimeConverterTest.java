package com.weizilla.transit.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeConverterTest
{
    private static final String DATETIME_STRING = "20140702 18:03";
    private static final DateTime DATETIME_JODA = new DateTime(2014, 7, 2, 18, 3, 0, DateTimeZone.forID("America/Chicago"));
    private TimeConverter converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new TimeConverter();
    }

    @Test
    public void readsStringToDateTime() throws Exception
    {
        InputNode inputNode = mock(InputNode.class);
        when(inputNode.getValue()).thenReturn(DATETIME_STRING);

        DateTime actual = converter.read(inputNode);
        assertEquals(DATETIME_JODA, actual);
    }

    @Test
    public void convertsStringToDateTime() throws Exception
    {
        DateTime actual = TimeConverter.parse(DATETIME_STRING);
        assertEquals(DATETIME_JODA, actual);
    }

    @Test
    public void writesDateTimeToString() throws Exception
    {
        OutputNode outputNode = mock(OutputNode.class);
        converter.write(outputNode, DATETIME_JODA);

        verify(outputNode).setValue(DATETIME_STRING);
    }

    @Test
    public void convertsDateTimeToString() throws Exception
    {
        String actual = TimeConverter.print(DATETIME_JODA);
        assertEquals(DATETIME_STRING, actual);
    }
}