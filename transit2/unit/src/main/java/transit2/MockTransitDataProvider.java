package transit2;

import com.weizilla.transit2.Prediction;
import com.weizilla.transit2.TransitDataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Returns sample xml
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:32 PM
 */
public class MockTransitDataProvider implements TransitDataProvider {

    @Override
    public List<Prediction> getPredictions(List<Integer> stops, List<Integer> routes) {
        return null;  //TODO auto-generated
    }

    private String readSampleFile(String filename)
    {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        String file = null;
        try {
            file = streamToString(in);
            System.out.println(file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();  //TODO auto-generated
        }
        return null;
    }

    private String streamToString(InputStream inputStream) throws IOException
    {
        //TODO replace with apache io
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null)
        {
            buffer.append(line);
        }

        return buffer.toString();
    }

}
