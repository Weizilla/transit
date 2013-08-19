package com.weizilla.transit2.xml;

import com.weizilla.transit2.Prediction;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 7:24 PM
 */
public class TransitResultsParser {
    private static final String NS = null;

    public void parse() throws Exception
    {
        //TODO
        Serializer seralizer = new Persister();
        String input = "<prd>\n" +
                "\t\t\t\t\t<tmstmp>20130818 17:23</tmstmp>\n" +
                "\t\t\t\t\t<typ>A</typ>\n" +
                "\t\t\t\t\t<stpnm>Clark &amp; Schubert</stpnm>\n" +
                "\t\t\t\t\t<stpid>1916</stpid>\n" +
                "\t\t\t\t\t<vid>1859</vid>\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t\t \t\t\t\t\t\n" +
                "\t\t\t\t\t<dstp>6114</dstp>\n" +
                "\t\t\t\t\t<rt>36</rt>\n" +
                "\t\t\t\t\t<rtdir>Northbound</rtdir>\n" +
                "\t\t        \t<des>Devon/Clark</des>\n" +
                "\t\t        \t<prdtm>20130818 17:31</prdtm>\n" +
                "\t\t        \t\n" +
                "\t\t\t\t</prd>\t\t";
        Prediction prediction = seralizer.read(Prediction.class, input);
        System.out.println(prediction);
    }

    //TODO remove
    public static void main(String[] args)
    {
        try {
            new TransitResultsParser().parse();
        } catch (Exception e) {
            e.printStackTrace();  //TODO auto-generated
        }
    }
}
