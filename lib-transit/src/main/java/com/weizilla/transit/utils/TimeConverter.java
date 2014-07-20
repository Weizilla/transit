package com.weizilla.transit.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class TimeConverter implements Converter<DateTime>
{
    private static final String PATTERN = "yyyyMMdd H:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(PATTERN);

    @Override
    public DateTime read(InputNode inputNode) throws Exception
    {
        String input = inputNode.getValue();
        return parse(input);
    }

    public static DateTime parse(String dateTime)
    {
        return FORMATTER.parseDateTime(dateTime).withZone(DateTimeZone.forID("America/Chicago"));
    }

    @Override
    public void write(OutputNode outputNode, DateTime dateTime) throws Exception
    {
        String output = print(dateTime);
        outputNode.setValue(output);
    }

    public static String print(DateTime dateTime)
    {
        return FORMATTER.print(dateTime);
    }
}
