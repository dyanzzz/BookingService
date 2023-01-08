package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListDealerMaps extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapFragment mapFragment;
    GoogleMap gMap;
    GoogleApiClient mGoogleApiClient;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    LatLng center, latLng;
    String title, description, nview, naccess, kd_dealer, coy_dealer, id_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        Intent intent   = getIntent();
        id_form         = intent.getStringExtra(Config.TAG_ID);
        //id_form = getIntent().getExtras().getInt(Config.TAG_ID);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Search Dealer!!!", Toast.LENGTH_LONG).show();

            setContentView(R.layout.activity_list_dealer_maps);
            initMap();
        } else {
            // No Google Maps Layout
            Toast.makeText(this, "Not Connect!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
/*
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        assert manager != null;
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

            gMap.setMyLocationEnabled(true);
            buildGoogleApiClient();

            if(id_form.equals("1")){
                getMarkers();
            }else{
                getMarkers_test_drive();
            }

        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private HashMap<Marker, String> markerIdMapping = new HashMap<>();
    private HashMap<Marker, String> markerCoyMapping = new HashMap<>();
    private HashMap<Marker, String> markerAccessMapping = new HashMap<>();


    private void addMarker(LatLng latlng, final String title, final String deskripsi, final String kd_dealer, final String coy_dealer, final String get_nView, final String get_nAccess) {
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.snippet(deskripsi);
        if(get_nAccess.equals("1")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }else{
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        //gMap.addMarker(markerOptions);

        Marker marker = gMap.addMarker(markerOptions);
        markerIdMapping.put(marker, kd_dealer);
        markerCoyMapping.put(marker, coy_dealer);
        markerAccessMapping.put(marker, get_nAccess);


            //public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //ini dia
                    //String markerId = markerIdMapping.get(marker);
                    //Toast.makeText(getApplicationContext(), markerIdMapping.get(marker), Toast.LENGTH_SHORT).show();

                    if (Config.CEK_KONEKSI(ListDealerMaps.this)) {
                        if (markerAccessMapping.get(marker).equals("1")) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(Config.DISP_KD_DEALER, markerIdMapping.get(marker));
                            resultIntent.putExtra(Config.DISP_COY_DEALER, markerCoyMapping.get(marker));
                            resultIntent.putExtra(Config.DISP_NAMA_DEALER, marker.getTitle());
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }else{
                            Toast.makeText(ListDealerMaps.this, Config.ALERT_ONLY_VIEW, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showDialog(Config.TAMPIL_ERROR);
                    }

                }
            });




        /*
        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker position) {
                View v = getLayoutInflater().inflate(R.layout.info_window_dealer_map, null);
                LatLng latLng       = position.getPosition();
                String kd_dealer    = position.getId();
                String nama         = position.getTitle();
                String alamat       = position.getSnippet();

                //TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                //TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                //TextView kd_dealer      = (TextView) v.findViewById(R.id.kd_dealer);
                TextView nama_dealertv    = (TextView) v.findViewById(R.id.nama_dealer);
                TextView alamattv         = (TextView) v.findViewById(R.id.alamat);

                //tvLat.setText("Latitude1:" + latLng.latitude);
                //tvLng.setText("Longitude1:"+ latLng.longitude);
                nama_dealertv.setText(nama);
                alamattv.setText(alamat);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });
        */
    }

    // Fungsi get JSON marker
    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_DEALER_SVC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        coy_dealer  = jsonObject.getString(Config.MAPS_COY_DEALER);
                        kd_dealer   = jsonObject.getString(Config.MAPS_KD_DEALER);
                        title       = jsonObject.getString(Config.MAPS_NAMA_DEALER);
                        description = jsonObject.getString(Config.MAPS_ALAMAT_DEALER);
                        latLng      = new LatLng(Double.parseDouble(jsonObject.getString(Config.MAPS_LAT)),
                                Double.parseDouble(jsonObject.getString(Config.MAPS_LNG)));
                        nview       = jsonObject.getString(Config.MAPS_NVIEW);
                        naccess     = jsonObject.getString(Config.MAPS_NACCESS);

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng, title, description, kd_dealer, coy_dealer, nview, naccess);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(ListDealerMaps.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, Config.TAG_JSON_OBJ);
    }

    private void getMarkers_test_drive() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_DEALER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        coy_dealer  = jsonObject.getString(Config.MAPS_COY_DEALER);
                        kd_dealer   = jsonObject.getString(Config.MAPS_KD_DEALER);
                        title       = jsonObject.getString(Config.MAPS_NAMA_DEALER);
                        description = jsonObject.getString(Config.MAPS_ALAMAT_DEALER);
                        latLng      = new LatLng(Double.parseDouble(jsonObject.getString(Config.MAPS_LAT)),
                                Double.parseDouble(jsonObject.getString(Config.MAPS_LNG)));
                        nview       = jsonObject.getString(Config.MAPS_NVIEW);
                        naccess     = jsonObject.getString(Config.MAPS_NACCESS);

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng, title, description, kd_dealer, coy_dealer, nview, naccess);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(ListDealerMaps.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, Config.TAG_JSON_OBJ);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pilih_mode_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.mapTypeNormal:
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    LocationRequest mLocationRequest;
    Location mylocation;
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        checkPermissions();
        //mLocationRequest.setInterval(1000);


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
        int permissionLocation = ContextCompat.checkSelfPermission(ListDealerMaps.this,
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
        int permissionLocation = ContextCompat.checkSelfPermission(ListDealerMaps.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void getMyLocation(){
        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(ListDealerMaps.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation =                     LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    @SuppressLint("RestrictedApi") LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
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
                                            .checkSelfPermission(ListDealerMaps.this,
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(ListDealerMaps.this,
                                                Config.REQUEST_CHECK_SETTINGS_GPS);
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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
}
