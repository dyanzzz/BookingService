package com.hyundaimobil.bookingservice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import com.hyundaimobil.bookingservice.app.Config;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class SplashScreen extends AppCompatActivity {

    ProgressBar mProgressBar1;
    private SmoothProgressBar mGoogleNow;
    private SmoothProgressBar mPocketBar;

    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mProgressBar1 = findViewById(R.id.progressbar);
        //mPocketBar = findViewById(R.id.pocket);
        //mGoogleNow = findViewById(R.id.google_now);

        mProgressBar1.setIndeterminateDrawable(
                new SmoothProgressDrawable.Builder(
                        SplashScreen.this
                ).interpolator(new AccelerateInterpolator()).build()
        );
        /*
        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable)mPocketBar.getIndeterminateDrawable()).getStrokeWidth()
                )
        );
        */

        proses();


    }

    public void proses(){
        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    //mPocketBar.progressiveStart();
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };

        if (Config.CEK_KONEKSI(SplashScreen.this)) {
            myThread.start();
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case Config.TAMPIL_ERROR:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle(Config.ALERT_TITLE_CONN_ERROR);
                errorDialog.setMessage(Config.ALERT_MESSAGE_CONN_ERROR);
                errorDialog.setCancelable(false);
                errorDialog.setPositiveButton(Config.ALERT_EXIT_BUTTON,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SplashScreen.this.finish();
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
