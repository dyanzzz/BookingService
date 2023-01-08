package com.hyundaimobil.bookingservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;

public class PickupCustomerMaps extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapFragment mapFragment;
    GoogleMap gMap;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    LatLng center, latLng;
    String title, description, kd_dealer;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Where are you?", Toast.LENGTH_LONG).show();

            setContentView(R.layout.activity_pickup_customer_maps);
            initMap();
        } else {
            // No Google Maps Layout
            Toast.makeText(this, "Not Connect!!!", Toast.LENGTH_LONG).show();
        }

    }

    private void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
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
            // Mengarahkan ke alun-alun Demak
            //center = new LatLng(-6.894796, 110.638413);
            //cameraPosition = new CameraPosition.Builder().target(center).zoom(10).build();
            //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
            tandainPeta();
            buildGoogleApiClient();


        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
        mGoogleApiClient.connect();
    }



    private void tandainPeta() {


/*
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    // int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    // Here we are finding , whatever we want our marker to show when clicked
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/


/*
        // Setting a custom info window adapter for the google map
        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                View v = getLayoutInflater().inflate(R.layout.info_window_pickup_customer_map, null);
                LatLng latLng = arg0.getPosition();
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                tvLat.setText("Latitude1:" + latLng.latitude);
                tvLng.setText("Longitude1:"+ latLng.longitude);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });
*/

        //markerOptions.position(latlng);
        //markerOptions.title(title);
        //markerOptions.snippet(deskripsi);
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
        //gMap.addMarker(markerOptions);
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(Config.CEK_KONEKSI(PickupCustomerMaps.this)) {
                    Toast.makeText(getApplicationContext(), "tes klik info window marker", Toast.LENGTH_SHORT).show();
                    //Intent resultIntent = new Intent();
                    //resultIntent.putExtra(Config.DISP_KD_DEALER, marker.getTitle());
                    //resultIntent.putExtra(Config.DISP_NAMA_DEALER, marker.getTitle());
                    //setResult(Activity.RESULT_OK, resultIntent);
                    //finish();
                } else {
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });

        //update marker
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {
                if (marker != null) {
                    marker.remove();
                }

                String latid = String.valueOf(latlng.latitude);
                String longtid = String.valueOf(latlng.longitude);
                Toast.makeText(PickupCustomerMaps.this, "Lat : "+latid+" Long : "+longtid, Toast.LENGTH_SHORT).show();

                cameraPosition = new CameraPosition.Builder().target(latlng).zoom(18).build();
                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                marker = gMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("I'am here")
                        .snippet("nama jalan/alamat")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        /*
                        .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_laumcher))
                            .title("Starting Location")
                            .draggable(false));
                        */
                //System.out.println(latlng);

                marker.showInfoWindow();
            }
        });

    }

    private void addMarker(LatLng latlng, final String title, final String deskripsi) {
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.snippet(deskripsi);

        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        gMap.addMarker(markerOptions);
        //public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //ini dia
                //Toast.makeText(getApplicationContext(), kd_dealer, Toast.LENGTH_SHORT).show();

                if(Config.CEK_KONEKSI(PickupCustomerMaps.this)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Config.DISP_KD_DEALER, marker.getTitle());
                    resultIntent.putExtra(Config.DISP_NAMA_DEALER, marker.getTitle());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });


        //update marker
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {
                if (marker != null) {
                    marker.remove();
                }

                marker = gMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("tes")
                        .snippet("latlng")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                System.out.println(latlng);
            }
        });

    }

    LocationRequest mLocationRequest;
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if(marker != null){
            marker.remove();
        }
        if(location == null){
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            markerOptions.position(ll);
            markerOptions.title("Current Location");
            markerOptions.snippet("nama jalan/alamat");
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            marker = gMap.addMarker(markerOptions);
            marker.showInfoWindow();

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
