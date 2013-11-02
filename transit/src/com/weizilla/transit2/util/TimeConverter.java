package com.weizilla.transit2.util;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class for converting date to and from string in format of "20130818 17:23"
 *
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 5:21 PM
 */
public class TimeConverter implements Converter<Date>
{
    public static final String PATTERN = "yyyyMMdd H:mm";
    private DateFormat df = new SimpleDateFormat(PATTERN);

    @Override
    public Date read(InputNode node) throws Exception
    {
        String dateStr = node.getValue();
        return df.parse(dateStr);
    }

    @Override
    public void write(OutputNode node, Date value) throws Exception
    {
        String dateStr = df.format(value);
        node.setValue(dateStr);
    }
}
