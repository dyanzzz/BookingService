package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyundaimobil.bookingservice.app.Config;

public class ForgotPasswordSukses extends AppCompatActivity {

    Button buttonOk;
    TextView deskripsi;
    String chasis, email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_sukses);

        Intent intent   = getIntent();
        chasis          = intent.getStringExtra(Config.DISP_UNIT_CHASIS);
        email           = intent.getStringExtra(Config.KEY_EMAIL);

        buttonOk        = findViewById(R.id.buttonOk);
        deskripsi       = findViewById(R.id.deskripsi);

        deskripsi.setText("Please check email "+email+" to get password");
    }

    public void btn_ok(View v){
        if(Config.CEK_KONEKSI(ForgotPasswordSukses.this)) {
            if (v == buttonOk) {
                onBackPressed();
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }
}
