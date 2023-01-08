package com.hyundaimobil.bookingservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBookingDetail extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis,
            kdBooking, kdDealer, coyDealer;
    TextView tvKdBooking, tvDealer, tvDate, tvTime, tvRemarks, tvNote, deskripsiStatusBooking;
    //ImageView statusBooking;
    //Button tombolCancel;

    MapFragment mapFragment;
    GoogleMap gMap;
    GoogleApiClient mGoogleApiClient;
    MarkerOptions markerOptions = new MarkerOptions();
    LatLng latLng;
    String title, description, kd_dealer;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_booking_detail);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        fullname        = user.get(SessionManager.KEY_FULLNAME);
        usercode        = user.get(SessionManager.KEY_USERCODE);
        nip             = user.get(SessionManager.KEY_NIP);
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

        Intent intent   = getIntent();
        kdBooking       = intent.getStringExtra(Config.DISP_KD_BOOKING);
        kdDealer        = intent.getStringExtra(Config.DISP_KD_DEALER);
        coyDealer       = intent.getStringExtra(Config.DISP_KD_COY);

        tvKdBooking                 = findViewById(R.id.kdBooking);
        tvDealer                    = findViewById(R.id.dealer);
        tvDate                      = findViewById(R.id.date);
        tvTime                      = findViewById(R.id.time);
        tvRemarks                   = findViewById(R.id.remarks);
        tvNote                      = findViewById(R.id.note);
        //statusBooking               = findViewById(R.id.statusBooking);
        deskripsiStatusBooking      = findViewById(R.id.deskripsiStatusBooking);
        //tombolCancel                = findViewById(R.id.buttonCancel);

        if(Config.CEK_KONEKSI(ListBookingDetail.this)) {
            getViewListBookingDetail();
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }

        if (googleServicesAvailable()) {
            initMap();
        } else {
            Toast.makeText(ListBookingDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void getViewListBookingDetail(){
        @SuppressLint("StaticFieldLeak")
        class GetView extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ListBookingDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showBookingDetail(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_BOOKING, kdBooking);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_BOOKING_DETAIL, params);
            }
        }
        GetView run = new GetView();
        run.execute();
    }

    @SuppressLint("SetTextI18n")
    private void showBookingDetail(String json){
        try{
            JSONObject jsonObject       = new JSONObject(json);
            JSONArray result            = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c                = result.getJSONObject(0);
            String kd_booking           = c.getString(Config.DISP_KD_BOOKING);
            String nama_dealer          = c.getString(Config.DISP_NAMA_DEALER);
            String date_booking         = c.getString(Config.DISP_DATE);
            String time_booking         = c.getString(Config.DISP_TIME);
            String remarks_booking      = c.getString(Config.DISP_REMARKS);
            String note_booking         = c.getString(Config.DISP_NOTE);
            String status_booking       = c.getString(Config.DISP_STATUS_BOOKING);

            tvKdBooking.setText(kd_booking);
            tvDealer.setText(nama_dealer);
            tvDate.setText(date_booking);
            tvTime.setText(time_booking);
            tvRemarks.setText(remarks_booking);
            tvNote.setText(note_booking);
            deskripsiStatusBooking.setText(status_booking);

            /*
            if(status_booking.equals("00")){
                statusBooking.setImageResource(R.mipmap.check_no);
                //statusBooking.setImageResource(R.mipmap.booking_date_pending);
                deskripsiStatusBooking.setText("PROGRESS");
                //tombolCancel.setVisibility(View.VISIBLE);
            }else{
                statusBooking.setImageResource(R.mipmap.check);
                //statusBooking.setImageResource(R.mipmap.booking_date_sukses);
                deskripsiStatusBooking.setText("COMPLETE");
                //tombolCancel.setVisibility(View.GONE);
            }
            */

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }
    }





    private void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        /*
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS Disabled, open settings and enable gps")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        */
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (gMap != null) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //gMap.setMyLocationEnabled(true);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();

            getMarkers();
        }
    }

    private void addMarker(LatLng latlng, final String title, final String deskripsi) {
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.snippet(deskripsi);

        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker = gMap.addMarker(markerOptions);
        marker.showInfoWindow();

        CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
        CameraUpdate zoom   = CameraUpdateFactory.zoomTo(15);
        gMap.moveCamera(center);
        gMap.animateCamera(zoom);

        //public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(getApplicationContext(), kd_dealer, Toast.LENGTH_SHORT).show();

                //if(Config.CEK_KONEKSI(ListBookingDetail.this)) {
                    //Intent resultIntent = new Intent();
                    //resultIntent.putExtra(Config.DISP_KD_DEALER, marker.getTitle());
                    //resultIntent.putExtra(Config.DISP_NAMA_DEALER, marker.getTitle());
                    //setResult(Activity.RESULT_OK, resultIntent);
                    //finish();
                //} else {
                    //showDialog(tampil_error);
                //}
            }
        });
    }

    // Fungsi get JSON marker
    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_GET_DEALER_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        kd_dealer   = jsonObject.getString(Config.MAPS_KD_DEALER);
                        title       = jsonObject.getString(Config.MAPS_NAMA_DEALER);
                        description = jsonObject.getString(Config.MAPS_ALAMAT_DEALER);
                        latLng      = new LatLng(Double.parseDouble(jsonObject.getString(Config.MAPS_LAT)),
                                Double.parseDouble(jsonObject.getString(Config.MAPS_LNG)));

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng, title, description);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(ListBookingDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(ListBookingDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                //Toast.makeText(ListBookingDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                params.put(Config.DISP_KD_DEALER, kdDealer);
                params.put(Config.DISP_KD_COY, coyDealer);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, Config.TAG_JSON_OBJ);
    }

    LocationRequest mLocationRequest;
    Location mylocation;
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(1000);

        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
        //}
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        checkPermissions();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(ListBookingDetail.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Config.REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(ListBookingDetail.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void getMyLocation(){
        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(ListBookingDetail.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    @SuppressLint("RestrictedApi") LocationRequest locationRequest = new LocationRequest();
                    //locationRequest.setInterval(3000);
                    //locationRequest.setFastestInterval(3000);
                    //locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(ListBookingDetail.this,
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(ListBookingDetail.this, Config.REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }








    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            gMap.animateCamera(update);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    //dialog disconect
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


    public void btn_cancelBooking(View view) {
        if(Config.CEK_KONEKSI(ListBookingDetail.this)) {
            //Toast.makeText(ListBookingDetail.this, tvKdBooking.getText(), Toast.LENGTH_SHORT).show();
            cancel_booking();
        }else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }


    private void cancel_booking(){
        @SuppressLint("StaticFieldLeak")
        class cancel_booking extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListBookingDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(ListBookingDetail.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from session
                params.put(Config.DISP_KD_BOOKING, kdBooking);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_CANCEL_BOOKING, params);
            }
        }

        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(Config.NOTIF_UMUM);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //cancel_booking start = new cancel_booking();
                //start.execute();
                //onBackPressed();
                Toast.makeText(ListBookingDetail.this, "YES", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(ListBookingDetail.this, "No", Toast.LENGTH_SHORT).show();
            }
        });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if(status_booking.equals("0")){
            //getMenuInflater().inflate(R.menu.item_remainder, menu);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            //case R.id.remainder_booking:
                //Toast.makeText(ListBookingDetail.this, "Reminder", Toast.LENGTH_SHORT).show();
                //return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
