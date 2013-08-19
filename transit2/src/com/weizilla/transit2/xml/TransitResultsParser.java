package com.weizilla.transit2.xml;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.List;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 7:24 PM
 */
public class TransitResultsParser {
    private static final String NS = null;

    public List parse(InputStream in) throws Exception
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            in.close();
        }
    }
}
