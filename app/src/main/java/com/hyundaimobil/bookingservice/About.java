package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.hyundaimobil.bookingservice.app.Config;
import java.net.URL;
import java.net.URLConnection;

public class About extends AppCompatActivity {
    ImageButton facebook, twitter, instagram, youtube;
    TextView hmi, appName, versiUpdate, web;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/HyundaiSansHeadOffice-Medium.ttf");
        hmi         = findViewById(R.id.hmi);
        appName     = findViewById(R.id.appName);
        versiUpdate = findViewById(R.id.versiUpdate);
        web         = findViewById(R.id.webHyundai);
        //facebook    = findViewById(R.id.facebook);
        //twitter     = findViewById(R.id.twitter);
        //instagram     = findViewById(R.id.instagram);
        //youtube     = findViewById(R.id.youtube);

        versiUpdate.setText("Version "+ Config.VALUE_VERSI);
        hmi.setTypeface(custom_font);
        web.setTypeface(custom_font);
        //facebook.setTypeface(custom_font);
        //twitter.setTypeface(custom_font);
        versiUpdate.setTypeface(custom_font);
        appName.setTypeface(custom_font);


        //isConnectedToServer("115.85.64.151", 1);
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            Toast.makeText(About.this, "connected",Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            Toast.makeText(About.this, "Not connected",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void web(View view){
        if(Config.CEK_KONEKSI(About.this)){
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hyundaimobil.co.id"));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void facebook(View view) {
        if(Config.CEK_KONEKSI(About.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/hyundaimobil"));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void twitter(View view) {
        if(Config.CEK_KONEKSI(About.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/hyundaimobil"));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void instagram(View view) {
        if(Config.CEK_KONEKSI(About.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/hyundaiid/"));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void youtube(View view) {
        if(Config.CEK_KONEKSI(About.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCzcb4MYM_Hvt5HERoYLdu_g"));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
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

    //dialog disconect
    @Override
    protected Dialog onCreateDialog(int id) {
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
