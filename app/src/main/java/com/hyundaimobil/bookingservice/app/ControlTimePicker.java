package com.hyundaimobil.bookingservice.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hyundaimobil.bookingservice.R;

import java.util.Calendar;

/**
 * Created by User HMI on 7/6/2017.
 */

public class ControlTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c    = Calendar.getInstance();
        int hour            = c.get(Calendar.HOUR_OF_DAY);
        int minute          = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    @SuppressLint("SetTextI18n")
    public void onTimeSet(TimePicker view, int hour, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView input_time     = (TextView) getActivity().findViewById(R.id.input_jam);
        String hour_string      = String.valueOf(hour);
        String minute_string    = String.valueOf(minute);
        //int seconds             = Calendar.getInstance().get(Calendar.SECOND);
        //String seconds_string   = String.valueOf(seconds);

        //if(hour > 12){
            //hour_string = String.valueOf(hour - 12);
        //}

        if(hour < 10){
            hour_string = "0" + String.valueOf(hour);
        }

        if(minute < 10){
            minute_string = "0" + String.valueOf(minute);
        }

        view.setIs24HourView(true);
        input_time.setText("");
        input_time.setText(input_time.getText() + hour_string + ":" + minute_string + ":00");
    }

}
