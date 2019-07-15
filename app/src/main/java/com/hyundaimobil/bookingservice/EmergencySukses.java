package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyundaimobil.bookingservice.app.Config;

public class EmergencySukses extends AppCompatActivity {

    static final int tampil_error = 1;
    private Button buttonOk;
    TextView deskripsi;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_sukses);

        buttonOk    = findViewById(R.id.buttonOk);
        deskripsi   = findViewById(R.id.deskripsi);

        deskripsi.setText("Jika call center kami belum menghubungi anda, segera hubungi "+Config.DISP_TELP_CRM);
    }

    public void btn_ok(View v){
        if(Config.CEK_KONEKSI(EmergencySukses.this)) {
            if (v == buttonOk) {
                onBackPressed();
            }
        }else{
            showDialog(tampil_error);
        }
    }

    //dialog disconect
    @Override
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case tampil_error:
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
