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

public class FormTestDriveSukses extends AppCompatActivity {

    private Button buttonOk;
    String note;
    TextView deskripsi;
    ImageView image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_test_drive_sukses);

        Intent intent   = getIntent();
        note            = intent.getStringExtra(Config.DISP_NOTE);

        buttonOk        = findViewById(R.id.buttonOk);
        deskripsi       = findViewById(R.id.deskripsi);
        image           = findViewById(R.id.imageView);

        if(note.equals("0")){
            image.setImageResource(R.drawable.booking_result_gagal);
            deskripsi.setText("Register Test Drive Failed");
        }else{
            image.setImageResource(R.drawable.booking_result_sukses);
            deskripsi.setText("Silahkan Datang sesuai jadwal booking atau periksa order di halaman history booking service");
        }
    }

    public void btn_ok(View v){
        if(Config.CEK_KONEKSI(FormTestDriveSukses.this)) {
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
