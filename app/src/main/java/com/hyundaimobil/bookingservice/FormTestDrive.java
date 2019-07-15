package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.db.DatabaseHelper;
import com.hyundaimobil.bookingservice.helper.LocaleHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.paperdb.Paper;

public class FormTestDrive extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private Button btnSave;
    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, JSON_STRING,
            date_libur, hari_ini, key_booking, dateHPlus,  dateHPlusOverTime, dateRange, sabtu, minggu,
            minHour, minMinutes, maxHour, maxMinutes, maxHourSat, maxMinutesSat;
    EditText input_date, input_time, input_dealer, input_kode_dealer, input_coy_dealer, input_vehicle, input_kode_vehicle,
            input_name, input_usercode, input_phone, input_email, input_note;
    TextView label_name, label_phone, label_email, label_note, label_vehicle, label_dealer,
            label_date, label_time, label_keterangan;
    ArrayList<String> array_libur = new ArrayList<>();

    protected Cursor cursor;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_test_drive);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        session = new SessionManager(getApplicationContext());
        //String statusLogin = String.valueOf(session.isLoggedIn());
        //session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        fullname            = user.get(SessionManager.KEY_FULLNAME);
        usercode            = user.get(SessionManager.KEY_USERCODE);
        nip                 = user.get(SessionManager.KEY_NIP);
        branch_code         = user.get(SessionManager.KEY_BRANCH_CODE);
        dept_code           = user.get(SessionManager.KEY_DEPT_CODE);
        company_code        = user.get(SessionManager.KEY_COMPANY_CODE);
        access_level        = user.get(SessionManager.KEY_ACCESS_LEVEL);
        salesman_code       = user.get(SessionManager.KEY_SALESMAN_CODE);
        supervisor_code     = user.get(SessionManager.KEY_SUPERVISOR_CODE);
        dealer_code         = user.get(SessionManager.KEY_DEALER_CODE);
        login_number        = user.get(SessionManager.KEY_LOGIN_NUMBER);
        kd_customer         = user.get(SessionManager.KEY_KD_CUSTOMER);
        chasis              = user.get(SessionManager.KEY_CHASIS);



        input_name          = findViewById(R.id.input_name);
        input_usercode      = findViewById(R.id.usercode);
        input_phone         = findViewById(R.id.input_phone);
        input_email         = findViewById(R.id.input_email);
        input_note          = findViewById(R.id.input_note);

        input_date          = findViewById(R.id.input_tgl);
        input_time          = findViewById(R.id.input_jam);
        input_dealer        = findViewById(R.id.input_dealer);
        input_kode_dealer   = findViewById(R.id.kode_dealer);
        input_coy_dealer    = findViewById(R.id.coy_dealer);
        input_vehicle       = findViewById(R.id.input_vehicle);
        input_kode_vehicle  = findViewById(R.id.kode_vehicle);

        label_name          = findViewById(R.id.label_name);
        label_phone         = findViewById(R.id.label_phone);
        label_email         = findViewById(R.id.label_email);
        label_note          = findViewById(R.id.label_note);
        label_vehicle       = findViewById(R.id.label_vehicle);
        label_dealer        = findViewById(R.id.label_dealer);
        label_date          = findViewById(R.id.label_date);
        label_time          = findViewById(R.id.label_time);
        label_keterangan    = findViewById(R.id.label_keterangan);

        input_name.setText(fullname);
        input_usercode.setText(usercode);

        input_note.setText("-");

        btnSave = findViewById(R.id.buttonSaveTestDrive);
        btnSave.setOnClickListener(this);

        input_vehicle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if (Config.CEK_KONEKSI(FormTestDrive.this)) {
                        Intent intent = new Intent(FormTestDrive.this, TipeKendaraan.class);
                        startActivityForResult(intent, Config.CHOSE_KENDARAAN);
                    } else {
                        showDialog(Config.TAMPIL_ERROR);
                    }
                }
            }
        });

        input_dealer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if (Config.CEK_KONEKSI(FormTestDrive.this)) {
                        Intent intent = new Intent(FormTestDrive.this, ListDealerMaps.class);
                        intent.putExtra(Config.TAG_ID, "2");
                        startActivityForResult(intent, Config.CHOSE_DEALER);
                    } else {
                        showDialog(Config.TAMPIL_ERROR);
                    }
                }
            }
        });

        input_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    //DialogFragment newFragment = new ControlDatePicker();
                    //newFragment.show(getFragmentManager(),"Date Picker");
                    if(input_dealer.getText().toString().trim().equals("")){
                        Toast.makeText(FormTestDrive.this, Config.ALERT_DEALER, Toast.LENGTH_LONG).show();
                    }else{
                        datePicker(dateRange, dateHPlus, dateHPlusOverTime, sabtu, minggu);
                    }
                }
            }
        });





        //for language
        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");
        updateView((String)Paper.book().read("language"));

        databaseHelper = new DatabaseHelper(this);
        final String status_app = "2";  //2. untuk bahasa
        db = databaseHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABEL_STATUS_KLIK+" WHERE "+
                DatabaseHelper.STATUS_APP+"='"+status_app+"'",null);
        cursor.moveToFirst();

        String a = cursor.getString(2);
        if(a.equals("4")){
            //Toast.makeText(getApplicationContext(), "Bahasa Indonesia", Toast.LENGTH_LONG).show();
            Paper.book().write("language", "en");
            updateView((String)Paper.book().read("language"));
        }else{
            //Toast.makeText(getApplicationContext(), "Bahasa Inggris", Toast.LENGTH_LONG).show();
            Paper.book().write("language", "id");
            updateView((String)Paper.book().read("language"));
        }


        Date date = new Date();
        hari_ini = sdf.format(date);

        key_booking = "2";

    }

    public void setDate(View v) {
        //DialogFragment newFragment = new ControlDatePicker();
        //newFragment.show(getFragmentManager(), "Date Picker");
        if(input_dealer.getText().toString().trim().equals("")){
            Toast.makeText(FormTestDrive.this, Config.ALERT_DEALER, Toast.LENGTH_LONG).show();
        }else{
            datePicker(dateRange, dateHPlus, dateHPlusOverTime, sabtu, minggu);
        }
    }

    public void setTime(View v) {
        //DialogFragment newFragment = new ControlTimePicker();
        //newFragment.show(getFragmentManager(), "Time Picker");

        //timePicker(true);
    }

    public void datePicker(String dateR, String dateHP, String dateHPO, String sabtu, String minggu){
        int dateRange           = Integer.parseInt(dateR);
        int dateHPlus           = Integer.parseInt(dateHP);
        int dateHplusOverTime   = Integer.parseInt(dateHPO);

        int hariSabtu          = Integer.parseInt(sabtu);
        int hariMinggu         = Integer.parseInt(minggu);

        final Calendar calendarMax = Calendar.getInstance();
        calendarMax.add(Calendar.DAY_OF_MONTH, dateRange);

        Calendar calendarToday = Calendar.getInstance();
        int year    = calendarToday.get(Calendar.YEAR);
        int month   = calendarToday.get(Calendar.MONTH);
        if(calendarToday.get(Calendar.HOUR_OF_DAY)>15) {
            calendarToday.add(Calendar.DAY_OF_MONTH, dateHplusOverTime);
        }else{
            calendarToday.add(Calendar.DAY_OF_MONTH, dateHPlus);
        }
        int day     = calendarToday.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(FormTestDrive.this, year, month, day);
        datePickerDialog.setMinDate(calendarToday);
        datePickerDialog.setMaxDate(calendarMax);

        //membuat hari minggu tidak dapat di pilih
        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();

        if((hariSabtu == 0) && (hariMinggu == 1)) {
            while( calendarToday.getTimeInMillis() <= calendarMax.getTimeInMillis() ) {
                //if( cAux.get( Calendar.DAY_OF_WEEK ) != 1 && cAux.get( Calendar.DAY_OF_WEEK ) != 7 ){
                if (calendarToday.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(calendarToday.getTimeInMillis());
                    daysList.add(c);
                }
                calendarToday.setTimeInMillis( calendarToday.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
            }
        }else if((hariSabtu == 1) && (hariMinggu == 0)){
            while( calendarToday.getTimeInMillis() <= calendarMax.getTimeInMillis() ){
                //if( cAux.get( Calendar.DAY_OF_WEEK ) != 1 && cAux.get( Calendar.DAY_OF_WEEK ) != 7 ){
                if( calendarToday.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY){
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis( calendarToday.getTimeInMillis() );
                    daysList.add( c );
                }
                calendarToday.setTimeInMillis( calendarToday.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
            }
        }else if((hariSabtu == 0) && (hariMinggu == 0)){
            while( calendarToday.getTimeInMillis() <= calendarMax.getTimeInMillis() ) {
                //if( cAux.get( Calendar.DAY_OF_WEEK ) != 1 && cAux.get( Calendar.DAY_OF_WEEK ) != 7 ){
                if ((calendarToday.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) && (calendarToday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(calendarToday.getTimeInMillis());
                    daysList.add(c);
                }
                calendarToday.setTimeInMillis( calendarToday.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
            }
        }


        daysArray = new Calendar[ daysList.size() ];
        for( int i = 0; i < daysArray.length; i++ ){
            daysArray[i] = daysList.get(i);
        }
        datePickerDialog.setSelectableDays( daysArray );

        //String[] holidays = {"20-04-2018","24-04-2018","26-04-2018"};
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = null;

        for (int i = 0;i < array_libur.size(); i++) {

            try {
                date = sdf.parse(array_libur.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendarToday = dateToCalendar(date);
            System.out.println(calendarToday.getTime());

            List<Calendar> dates = new ArrayList<>();
            dates.add(calendarToday);
            Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
            datePickerDialog.setDisabledDays(disabledDays1);
        }

        datePickerDialog.vibrate(true); //vibrate on choosing date?
        datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        boolean isSaturday  = false;
        int bulan           = (++monthOfYear);
        String day_string   = String.valueOf(dayOfMonth);
        String month_string = String.valueOf(bulan);
        if(dayOfMonth < 10){
            day_string = "0" + String.valueOf(dayOfMonth);
        }
        if(bulan < 10){
            month_string = "0" + String.valueOf(bulan);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear-1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        isSaturday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;

        Calendar selectedDate =  Calendar.getInstance();
        int mYear   = selectedDate.get(Calendar.YEAR);
        int mMonth  = selectedDate.get(Calendar.MONTH);
        int mDay    = selectedDate.get(Calendar.DAY_OF_MONTH);
        String D    = String.valueOf(mDay);

        if(String.valueOf(dayOfMonth).equals(D)){
            if(selectedDate.get(Calendar.HOUR_OF_DAY)>6 && selectedDate.get(Calendar.HOUR_OF_DAY)<15) {
                if(selectedDate.get(Calendar.HOUR_OF_DAY)==14 && selectedDate.get(Calendar.MINUTE)>30) {
                    Toast.makeText(FormTestDrive.this, "Test Drive at "+minHour+"."+minMinutes+" - "+maxHour+"."+maxMinutes, Toast.LENGTH_SHORT).show();
                    input_time.setText("");
                }else{
                    timePicker(true, isSaturday, minHour, minMinutes, maxHour, maxMinutes, maxHourSat, maxMinutesSat);
                }
            }else if(selectedDate.get(Calendar.HOUR_OF_DAY)>0 && selectedDate.get(Calendar.HOUR_OF_DAY)<7){
                timePicker(true, isSaturday, minHour, minMinutes, maxHour, maxMinutes, maxHourSat, maxMinutesSat);
            }else{
                Toast.makeText(FormTestDrive.this, "Test Drive at "+minHour+"."+minMinutes+" - "+maxHour+"."+maxMinutes, Toast.LENGTH_SHORT).show();
                input_time.setText("");
            }
        }else{
            timePicker(false, isSaturday, minHour, minMinutes, maxHour, maxMinutes, maxHourSat, maxMinutesSat);
        }

        input_date.setText("");
        input_date.setText(day_string + "." + month_string + "." + year);
    }

    public void timePicker(boolean isTodayDate, boolean isSaturday, String minH, String minM, String maxH, String maxM, String maxHS, String maxMS){
        int minHour     = Integer.parseInt(minH);
        int minMinutes  = Integer.parseInt(minM);
        int second      = 0;

        int maxHour     = Integer.parseInt(maxH);
        int maxMinutes  = Integer.parseInt(maxM);

        int maxHourSat      = Integer.parseInt(maxHS);
        int maxMinutesSat   = Integer.parseInt(maxMS);

        Calendar now = Calendar.getInstance();
        TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(
                FormTestDrive.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);

        if(isTodayDate){
            if(now.get(Calendar.HOUR_OF_DAY)>6 && now.get(Calendar.HOUR_OF_DAY)<14){
                if(now.get(Calendar.MINUTE)>=0 && now.get(Calendar.MINUTE)<61) {
                    minHour = now.get(Calendar.HOUR_OF_DAY) + 1;
                    minMinutes = Integer.parseInt(minM);
                }
            }else if(now.get(Calendar.HOUR_OF_DAY)>0 && now.get(Calendar.HOUR_OF_DAY)<7){
                minHour     = Integer.parseInt(minH);
                minMinutes  = Integer.parseInt(minM);
            }
        }

        if(isSaturday){
            maxHour     = maxHourSat;
            maxMinutes  = maxMinutesSat;
        }

        timepickerdialog.setMinTime(minHour, minMinutes, second);
        timepickerdialog.setMaxTime(maxHour, maxMinutes, second);

        timepickerdialog.setThemeDark(false); //Dark Theme?
        timepickerdialog.vibrate(false); //vibrate on choosing time?
        timepickerdialog.dismissOnPause(false); //dismiss the dialog onPause() called?
        //timepickerdialog.enableSeconds(true); //show seconds?

        /*
        //Handling cancel event
        timepickerdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(FormTestDrive.this, "Cancel choosing time", Toast.LENGTH_SHORT).show();
            }
        });
        */
        timepickerdialog.show(getFragmentManager(), "Timepickerdialog"); //show time picker dialog

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString   = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time         = hourString + ":" + minuteString + ":00";
        input_time.setText(time);
    }


    public void cariDealer(View v) {
        if (Config.CEK_KONEKSI(FormTestDrive.this)) {
            Intent intent = new Intent(FormTestDrive.this, ListDealerMaps.class);
            intent.putExtra(Config.TAG_ID, "2");
            startActivityForResult(intent, Config.CHOSE_DEALER);
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void pilihTipeKendaraan(View v) {
        if (Config.CEK_KONEKSI(FormTestDrive.this)) {
            Intent intent = new Intent(FormTestDrive.this, TipeKendaraan.class);
            startActivityForResult(intent, Config.CHOSE_KENDARAAN);
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Config.CHOSE_DEALER): {
                if (resultCode == Activity.RESULT_OK) {
                    String kode_dealer  = data.getStringExtra(Config.DISP_KD_DEALER);
                    String coy_dealer  = data.getStringExtra(Config.DISP_COY_DEALER);
                    String nama_dealer  = data.getStringExtra(Config.DISP_NAMA_DEALER);
                    EditText input_kode_dealer  = findViewById(R.id.kode_dealer);
                    EditText input_coy_dealer  = findViewById(R.id.coy_dealer);
                    EditText input_dealer       = findViewById(R.id.input_dealer);
                    input_kode_dealer.setText(kode_dealer);
                    input_coy_dealer.setText(coy_dealer);
                    input_dealer.setText(nama_dealer);

                    array_libur.clear();
                    getJSON_libur(kode_dealer, coy_dealer, key_booking);
                    input_date.setText("");
                    input_time.setText("");
                }
                break;
            }
            case (Config.CHOSE_KENDARAAN): {
                if (resultCode == Activity.RESULT_OK) {
                    String kode_kendaraan   = data.getStringExtra(Config.DISP_KD_KENDARAAN);
                    String nama_kendaraan   = data.getStringExtra(Config.DISP_NAMA_KENDARAAN);
                    EditText input_kendaraan        = findViewById(R.id.input_vehicle);
                    EditText input_kode_kendaraan   = findViewById(R.id.kode_vehicle);
                    input_kendaraan.setText(nama_kendaraan);
                    input_kode_kendaraan.setText(kode_kendaraan);
                }
                break;
            }
        }
    }

    public void onClick(View v) {
        if (Config.CEK_KONEKSI(FormTestDrive.this)) {
            if (v == btnSave) {
                btnSaveFunction();

            }
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void btnSaveFunction() {
        final String name       = input_name.getText().toString().trim();
        final String etUsercode = input_usercode.getText().toString().trim();
        final String phone      = input_phone.getText().toString().trim();
        final String email      = input_email.getText().toString().trim();
        final String note       = input_note.getText().toString().trim();
        final String date       = input_date.getText().toString().trim();
        final String time       = input_time.getText().toString().trim();
        final String dealer     = input_kode_dealer.getText().toString().trim();
        final String coy_dealer = input_coy_dealer.getText().toString().trim();
        final String vehicle    = input_kode_vehicle.getText().toString().trim();

        if (name.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_NAME, Toast.LENGTH_SHORT).show();
            input_name.requestFocus();

        } else if (phone.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_PHONE, Toast.LENGTH_SHORT).show();
            input_phone.requestFocus();

        } else if (email.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_EMAIL, Toast.LENGTH_SHORT).show();
            input_email.requestFocus();

        } else if (note.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_NOTE, Toast.LENGTH_SHORT).show();
            input_note.requestFocus();

        } else if (vehicle.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_VEHICLE, Toast.LENGTH_SHORT).show();
            input_kode_vehicle.requestFocus();

        } else if (dealer.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_DEALER, Toast.LENGTH_SHORT).show();
            input_kode_dealer.requestFocus();

        } else if (date.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_DATE, Toast.LENGTH_SHORT).show();
            input_date.requestFocus();

        } else if (time.equals("")) {
            Toast.makeText(FormTestDrive.this, Config.ALERT_TIME, Toast.LENGTH_SHORT).show();
            input_time.requestFocus();

        } else {
            @SuppressLint("StaticFieldLeak")
            class Add extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(FormTestDrive.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    //Toast.makeText(FormTestDrive.this, s, Toast.LENGTH_LONG).show();
                    switch (s) {
                        case "0":
                            Toast.makeText(FormTestDrive.this, Config.ALERT_FAILED_TEST_DRIVE, Toast.LENGTH_LONG).show();
                            break;
                        case "2":
                            Toast.makeText(FormTestDrive.this, Config.ALERT_DATE_WRONG, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            onBackPressed();
                            Intent intent = new Intent(FormTestDrive.this, FormTestDriveSukses.class);
                            intent.putExtra(Config.DISP_NOTE, s);
                            startActivity(intent);
                            break;
                    }


                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.KEY_USERCODE, etUsercode);
                    params.put(Config.KEY_NAME, name);
                    params.put(Config.KEY_PHONE, phone);
                    params.put(Config.KEY_EMAIL, email);
                    params.put(Config.KEY_NOTE, note);
                    params.put(Config.KEY_DATE_BOOKING, date);
                    params.put(Config.KEY_TIME_BOOKING, time);
                    params.put(Config.KEY_DEALER_DESTINATION, dealer);
                    params.put(Config.KEY_COY_DEALER_DESTINATION, coy_dealer);
                    params.put(Config.KEY_KD_VEHICLE, vehicle);
                    params.put(Config.KEY_TANGGAL_HARI_INI, hari_ini);

                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Config.URL_ADD_TEST_DRIVE, params);
                }
            }


            final Calendar c    = Calendar.getInstance();
            int year            = c.get(Calendar.YEAR);
            int month           = c.get(Calendar.MONTH);
            int day             = c.get(Calendar.DAY_OF_MONTH);
            final String tgl    = year + "-" + (++month) + "-" + day;
            final String status = "2";
            final String status_app = "1";

            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABEL_STATUS_KLIK+" WHERE "+
                    DatabaseHelper.TANGGAL+"='"+tgl+"' AND "+
                    DatabaseHelper.STATUS+"='"+status+"' AND "+
                    DatabaseHelper.STATUS_APP+"='"+status_app+"'",null);
            cursor.moveToFirst();

            if (cursor.getCount() < 5) {
                db = databaseHelper.getWritableDatabase();

                db.execSQL("INSERT INTO "+DatabaseHelper.TABEL_STATUS_KLIK+"("+
                        DatabaseHelper.TANGGAL+", "+DatabaseHelper.STATUS+", "+DatabaseHelper.STATUS_APP+") values('" +
                        tgl + "','" +
                        status + "','" +
                        status_app + "')");
                Log.d("Data", "onCreate: " + db);

                //Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();

                Add run = new Add();
                run.execute();


            } else {
                Toast.makeText(getApplicationContext(), "Sorry, already have a schedule test drive. Try again tomorrow", Toast.LENGTH_LONG).show();
            }

            ForceCloseDebugger.handle(this);
        }
    }


    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    //dialog disconect
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Config.TAMPIL_ERROR:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle(Config.ALERT_TITLE_CONN_ERROR);
                errorDialog.setMessage(Config.ALERT_MESSAGE_CONN_ERROR);
                errorDialog.setNeutralButton(Config.ALERT_OK_BUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return errorDialog.create();
            default:
                break;
        }
        return null;
    }



    //for language apps
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();

        label_name.setText(resources.getString(R.string.input_name));
        label_phone.setText(resources.getString(R.string.input_phone));
        label_email.setText(resources.getString(R.string.input_email));
        label_note.setText(resources.getString(R.string.input_note));
        label_vehicle.setText(resources.getString(R.string.input_vehicle));
        label_dealer.setText(resources.getString(R.string.input_dealer));
        label_date.setText(resources.getString(R.string.input_date));
        label_time.setText(resources.getString(R.string.input_time));
        label_keterangan.setText(resources.getString(R.string.keterangan_date_test_drive));
    }

    private void getJSON_libur(final String cab, final String coy, final String key_booking){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(FormTestDrive.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                showDataDateLibur();
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_DEALER_DESTINATION, cab);
                params.put(Config.KEY_COY_DEALER_DESTINATION, coy);
                params.put(Config.KEY_BOOKING, key_booking);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_DATE_LIBUR, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataDateLibur(){
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject(JSON_STRING);
            minHour         = jsonObject.getString("minHour");
            minMinutes      = jsonObject.getString("minMinutes");
            maxHour         = jsonObject.getString("maxHour");
            maxMinutes      = jsonObject.getString("maxMinutes");
            maxHourSat      = jsonObject.getString("maxHourSat");
            maxMinutesSat   = jsonObject.getString("maxMinutesSat");

            dateHPlus           = jsonObject.getString("dateHPlus");
            dateHPlusOverTime   = jsonObject.getString("dateHPlusOverTime");
            dateRange           = jsonObject.getString("dateRange");
            sabtu               = jsonObject.getString("sabtu");
            minggu              = jsonObject.getString("minggu");

            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                date_libur      = jo.getString(Config.DISP_DATE);
                array_libur.add(date_libur);
            }

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(FormTestDrive.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

}
