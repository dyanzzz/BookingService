package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyundaimobil.bookingservice.app.Config;

public class FormBookingSukses extends AppCompatActivity {

    private Button buttonOk;
    TextView text_sukses, kode_order, deskripsi;
    String kdBooking;
    ImageView image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_booking_sukses);

        Intent intent   = getIntent();
        kdBooking       = intent.getStringExtra(Config.DISP_KD_BOOKING);

        text_sukses     = findViewById(R.id.text_sukses);
        kode_order      = findViewById(R.id.kode_order);
        deskripsi       = findViewById(R.id.deskripsi);
        buttonOk        = findViewById(R.id.buttonOk);
        image           = findViewById(R.id.imageView);

        if(kdBooking.equals("0")){
            image.setImageResource(R.drawable.booking_result_gagal);
            text_sukses.setText("Booking Failed");
            kode_order.setText("Register Booking Service Failed");
            deskripsi.setText("Silahkan melakukan booking ulang");
        }else{
            image.setImageResource(R.drawable.booking_result_sukses);
            text_sukses.setText("Booking Success");
            kode_order.setText(kdBooking);
            deskripsi.setText("Silahkan Datang sesuai jadwal booking dan periksa order di halaman history booking service");
        }

    }

    public void btn_ok(View v){
        if(Config.CEK_KONEKSI(FormBookingSukses.this)) {
            if (v == buttonOk) {
                onBackPressed();
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
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
