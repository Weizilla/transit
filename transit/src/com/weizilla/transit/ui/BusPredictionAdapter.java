package com.weizilla.transit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Prediction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * adapter for a bus prediction list
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 4:24 PM
 */
public class BusPredictionAdapter extends ArrayAdapter<Prediction>
{
    private List<Prediction> predictions;
    private Date refTime;

    public BusPredictionAdapter(Context context, List<Prediction> predictions)
    {
        super(context, R.layout.bus_pred_item, predictions);
        this.predictions = predictions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bus_pred_item, parent, false);
        }
        else
        {
            view = convertView;
        }

        Prediction prediction = predictions.get(position);

        TextView uiRoute = (TextView) view.findViewById(R.id.uiBusPredRoute);
        uiRoute.setText(prediction.getRoute());

        TextView uiDest = (TextView) view.findViewById(R.id.uiBusPredDest);
        uiDest.setText(prediction.getDestination());

        //TODO need to get current time from transit provider
        // to account for time offset
        Date refTime = this.refTime != null ? this.refTime : getCurrentTime();
        long timeLeft = calculateTimeLeft(refTime, prediction.getPredictionTime());

        TextView uiPred = (TextView) view.findViewById(R.id.uiBusPredTimeLeft);
        uiPred.setText(String.valueOf(timeLeft));

        return view;
    }

    public void setRefTime(Date refTime)
    {
        this.refTime = refTime;
    }

    public static Date getCurrentTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        return calendar.getTime();
    }


    public static long calculateTimeLeft(Date refTime, Date predictionTime)
    {
        return TimeUnit.MILLISECONDS.toMinutes(predictionTime.getTime() - refTime.getTime());
    }


}
