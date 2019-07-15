package com.hyundaimobil.bookingservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hyundaimobil.bookingservice.app.Config;

public class RegisterSukses extends AppCompatActivity {

    private Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sukses);

        buttonOk        = findViewById(R.id.buttonOk);


    }

    public void btn_ok(View v){
        if(Config.CEK_KONEKSI(RegisterSukses.this)) {
            if (v == buttonOk) {
                onBackPressed();
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }
}
