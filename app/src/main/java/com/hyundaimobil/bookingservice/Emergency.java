package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.db.DatabaseHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Emergency extends AppCompatActivity implements LocationListener {

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis;
    protected LocationManager locationManager;
    TextView telp;
    ProgressDialog loading;
    protected Cursor cursor;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        databaseHelper = new DatabaseHelper(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        fullname = user.get(SessionManager.KEY_FULLNAME);
        usercode = user.get(SessionManager.KEY_USERCODE);
        nip = user.get(SessionManager.KEY_NIP);
        branch_code     = user.get(SessionManager.KEY_BRANCH_CODE);
        dept_code       = user.get(SessionManager.KEY_DEPT_CODE);
        company_code    = user.get(SessionManager.KEY_COMPANY_CODE);
        access_level    = user.get(SessionManager.KEY_ACCESS_LEVEL);
        salesman_code   = user.get(SessionManager.KEY_SALESMAN_CODE);
        supervisor_code = user.get(SessionManager.KEY_SUPERVISOR_CODE);
        dealer_code     = user.get(SessionManager.KEY_DEALER_CODE);
        login_number    = user.get(SessionManager.KEY_LOGIN_NUMBER);
        kd_customer     = user.get(SessionManager.KEY_KD_CUSTOMER);
        chasis          = user.get(SessionManager.KEY_CHASIS);
        telp            = findViewById(R.id.telp);
        telp.setText(Config.DISP_TELP_CRM);
    }



    //menu dashboard
    public void btn_call_hyundai(View view) {
        if (Config.CEK_KONEKSI(Emergency.this)) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Config.DISP_TELP_CRM));
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = intent.resolveActivity(packageManager);
            if (componentName != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No Application Support", Toast.LENGTH_LONG).show();
            }

/*
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_DIAL);
            //sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, "087771019854");
            //sendIntent.putExtra(Intent.EXTRA_TEXT, "087771019854");
            //sendIntent.setType("text/plain");
            //sendIntent.setType("message/rfc822");
            Intent.createChooser(sendIntent,"Share via");
            startActivity(sendIntent);
*/
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void btn_pick_up(View view) {
        if (Config.CEK_KONEKSI(Emergency.this)) {

            //Intent intent = new Intent(Emergency.this, PickupCustomerMaps.class);
            //startActivity(intent);

            //Creating an alert dialog to confirm logout
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(Config.NOTIF_UMUM);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    final Calendar c    = Calendar.getInstance();
                    int year            = c.get(Calendar.YEAR);
                    int month           = c.get(Calendar.MONTH);
                    int day             = c.get(Calendar.DAY_OF_MONTH);
                    final String tgl    = year + "-" + (++month) + "-" + day;
                    final String status = "3";
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

                        loading = ProgressDialog.show(Emergency.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                        getLocation();
                        //new GeocodeAsyncTask().execute();

                        //send SMS
                        //String messageToSend = "this is a message";
                        //String number = "087771019854";
                        //SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);

                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, can not click multiple this button. Try again tomorrow", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                    /*
                    loading = ProgressDialog.show(Emergency.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
                    getLocation();
                    */
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {}
            });

            //Showing the alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }





    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

        } catch(SecurityException e) {
            e.printStackTrace();
            loading.dismiss();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        /*
        Toast.makeText(Emergency.this, "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Toast.makeText(Emergency.this,
                    addresses.get(0).getAddressLine(0) +", "+
                            addresses.get(0).getAddressLine(1) +", "+
                            addresses.get(0).getAddressLine(2),
                    Toast.LENGTH_LONG).show();
            locationManager.removeUpdates(this);
            loading.dismiss();
        } catch(Exception e) {
            Toast.makeText(Emergency.this, e.getMessage(), Toast.LENGTH_LONG).show();
            loading.dismiss();
        }
        */


        try {
            final double latitude   = location.getLatitude();
            final double longtitude = location.getLongitude();

            Geocoder geocoder       = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            final String address    = addresses.get(0).getAddressLine(0) +", "+ addresses.get(0).getAddressLine(1) +", "+ addresses.get(0).getAddressLine(2);

            @SuppressLint("StaticFieldLeak")
            class action extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //loading = ProgressDialog.show(Emergency.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    //Toast.makeText(FormBooking.this, s, Toast.LENGTH_LONG).show();
                    onBackPressed();
                    Intent intent = new Intent(Emergency.this, EmergencySukses.class);
                    //intent.putExtra(Config.DISP_KD_BOOKING, s);
                    startActivity(intent);
                }

                @Override
                protected String doInBackground(Void... v) {

                    HashMap<String, String> params = new HashMap<>();
                    //from session
                    params.put(SessionManager.KEY_BRANCH_CODE, branch_code);
                    params.put(SessionManager.KEY_COMPANY_CODE, company_code);
                    params.put(SessionManager.KEY_KD_CUSTOMER, kd_customer);
                    params.put(SessionManager.KEY_CHASIS, chasis);
                    //from form input
                    params.put(Config.MAPS_LAT, String.valueOf(latitude));
                    params.put(Config.MAPS_LNG, String.valueOf(longtitude));
                    params.put(Config.MAPS_ADDRESS, address);

                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Config.URL_SEND_MAIL_EMERGENCY, params);
                }
            }

            action run = new action();
            run.execute();

            ForceCloseDebugger.handle(this);
            locationManager.removeUpdates(this);
        }catch (Exception e){
            Toast.makeText(Emergency.this, e.getMessage(), Toast.LENGTH_LONG).show();
            loading.dismiss();
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Emergency.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(Emergency.this, "GPS Enable", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case Config.TAMPIL_ERROR:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle(Config.ALERT_TITLE_CONN_ERROR);
                errorDialog.setMessage(Config.ALERT_MESSAGE_CONN_ERROR);
                errorDialog.setNeutralButton(Config.ALERT_OK_BUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                return errorDialog.create();
            default:
                break;
        }
        return null;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }



}
