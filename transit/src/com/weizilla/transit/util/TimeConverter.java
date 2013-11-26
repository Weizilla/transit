package com.weizilla.transit.util;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * class for converting date to and from string in format of "20130818 17:23"
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 5:21 PM
 */
public class TimeConverter implements Converter<Date>
{
    public static final String PATTERN = "yyyyMMdd H:mm";

    @Override
    public Date read(InputNode node) throws Exception
    {
        String dateStr = node.getValue();
        return parse(dateStr);
    }

    @Override
    public void write(OutputNode node, Date value) throws Exception
    {
        String dateStr = format(value);
        node.setValue(dateStr);
    }

    public static String format(Date date)
    {
        DateFormat df = new SimpleDateFormat(PATTERN);
        return df.format(date);
    }

    public static Date parse(String string)
    {
        DateFormat df = new SimpleDateFormat(PATTERN);
        df.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        try
        {
            return df.parse(string);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
