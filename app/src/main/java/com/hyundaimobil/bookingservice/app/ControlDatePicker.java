package com.hyundaimobil.bookingservice.app;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.hyundaimobil.bookingservice.R;

import java.util.Calendar;


/**
 * Created by User HMI on 7/6/2017.
 */

public class ControlDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    //private DatePickerDialog datePickerDialog;

    private int year, month, day, hour, minute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year    = c.get(Calendar.YEAR);
        int month   = c.get(Calendar.MONTH);
        int day     = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker dp = dpd.getDatePicker();
        dp.setMinDate(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH,20);
        dp.setMaxDate(c.getTimeInMillis());

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return dpd;
    }

    @SuppressLint("SetTextI18n")
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Do something with the date chosen by the user
        int bulan           = month+1;
        EditText input_date = (EditText) getActivity().findViewById(R.id.input_tgl);
        String day_string   = String.valueOf(day);
        String month_string = String.valueOf(bulan);

        //String stringOfDate = day + "/" + month + "/" + year;
        if(day < 10){
            day_string = "0" + String.valueOf(day);
        }
        if(bulan < 10){
            month_string = "0" + String.valueOf(bulan);
        }

        input_date.setText("");
        input_date.setText(input_date.getText() + day_string + "." + month_string + "." + String.valueOf(year));
    }
}
