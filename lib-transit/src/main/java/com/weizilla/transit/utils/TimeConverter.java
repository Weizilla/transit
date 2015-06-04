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
    private static final String PATTERN_SECONDS = "yyyyMMdd H:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(PATTERN);
    private static final DateTimeFormatter FORMATTER_SECONDS = DateTimeFormat.forPattern(PATTERN_SECONDS);

    @Override
    public DateTime read(InputNode inputNode) throws Exception
    {
        String input = inputNode.getValue();
        return parse(input);
    }

    public static DateTime parse(String dateTime)
    {
        DateTimeFormatter formatter = isWithSeconds(dateTime) ? FORMATTER_SECONDS : FORMATTER;
        return formatter.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.forID("America/Chicago"));
    }

    private static boolean isWithSeconds(String dateTime)
    {
        int found = 0;
        for (int i = dateTime.length() - 1; i >= 0; i--)
        {
            if (dateTime.charAt(i) == ':') {
                found++;
                if (found == 2)
                {
                    return true;
                }
            }
        }
        return false;
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
