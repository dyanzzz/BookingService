package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText nama, email, phone, chasis,
            engine, nopol, type, color;
    Button buttonRegister;

    String hp, simID, imei, brand, nama_device, model, id_model, nama_produk, versi_api_sdk, os;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ForceCloseDebugger.handle(this);

        nama    = findViewById(R.id.editTextNama);
        email   = findViewById(R.id.editTextEmail);
        phone   = findViewById(R.id.editTextPhone);
        chasis  = findViewById(R.id.editTextChasis);
        engine  = findViewById(R.id.editTextEngine);
        nopol   = findViewById(R.id.editTextNopol);
        type    = findViewById(R.id.editTextType);
        color   = findViewById(R.id.editTextColor);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
        chasis.setOnClickListener(this);

        chasis.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Config.CEK_KONEKSI(Register.this)) {
                        cariListChasis(v);
                    } else {
                        showDialog(Config.TAMPIL_ERROR);
                    }
                }
            }
        });




        /*
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        assert tMgr != null;
        */
        hp      = "";
        simID   = "";
        imei    = "";

        brand          = Build.BRAND;
        nama_device    = Build.DEVICE;
        model          = Build.MODEL;
        id_model       = Build.ID;
        nama_produk    = Build.PRODUCT;
        versi_api_sdk  = Build.VERSION.SDK;
        os             = Build.VERSION.RELEASE;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Config.CHOSE_CHASIS):
                if (resultCode == Activity.RESULT_OK) {
                    String chasis   = data.getStringExtra(Config.DISP_UNIT_CHASIS);
                    String engine   = data.getStringExtra(Config.DISP_UNIT_ENGINE);
                    String nopol    = data.getStringExtra(Config.DISP_UNIT_POLICE);
                    String type     = data.getStringExtra(Config.DISP_UNIT_KDTYPE);
                    String colour   = data.getStringExtra(Config.DISP_UNIT_KDCOLOUR);

                    EditText etChasis   = findViewById(R.id.editTextChasis);
                    EditText etEngine   = findViewById(R.id.editTextEngine);
                    EditText etNopol    = findViewById(R.id.editTextNopol);
                    EditText etType     = findViewById(R.id.editTextType);
                    EditText etColor    = findViewById(R.id.editTextColor);

                    etChasis.setText(chasis);
                    etEngine.setText(engine);
                    etNopol.setText(nopol);
                    etType.setText(type);
                    etColor.setText(colour);
                }
                break;

        }
    }

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

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void onClick(View v){
        if(Config.CEK_KONEKSI(Register.this)) {
            if (v == buttonRegister) {
                btnSaveFunction();
            }else if(v == chasis){
                cariListChasis(v);
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void cariListChasis(View v) {
        Intent intent = new Intent(Register.this, RegisterListChasis.class);
        intent.putExtra(Config.TAG_ID, "1");
        startActivityForResult(intent, Config.CHOSE_CHASIS);
    }

    private void btnSaveFunction() {

        final String input_nama     = nama.getText().toString().trim();
        final String input_email    = email.getText().toString().trim();
        final String input_phone    = phone.getText().toString().trim();
        final String input_chasis   = chasis.getText().toString().trim();
        final String input_engine   = engine.getText().toString().trim();
        final String input_nopol    = nopol.getText().toString().trim();
        final String input_type     = type.getText().toString().trim();
        final String input_color    = color.getText().toString().trim();

        if (input_nama.equals("")) {
            Toast.makeText(Register.this, Config.ALERT_NAME, Toast.LENGTH_SHORT).show();
            nama.requestFocus();

        } else if (input_email.equals("")) {
            Toast.makeText(Register.this, Config.ALERT_EMAIL, Toast.LENGTH_SHORT).show();
            email.requestFocus();

        } else if (input_phone.equals("")) {
            Toast.makeText(Register.this, Config.ALERT_PHONE, Toast.LENGTH_SHORT).show();
            phone.requestFocus();

        } else if (input_chasis.equals("")) {
            Toast.makeText(Register.this, Config.ALERT_CHASIS, Toast.LENGTH_SHORT).show();
            chasis.requestFocus();

        } else {


            @SuppressLint("StaticFieldLeak")
            class Addregister extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Register.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();

                    switch (s) {

                        case "0":
                            Toast.makeText(Register.this, Config.ACCOUNT_ALREADY_REGISTER, Toast.LENGTH_LONG).show();
                            break;

                        case "1":
                            Toast.makeText(Register.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                            break;

                        case "2":
                            Toast.makeText(Register.this, Config.ALERT_INVALID_EMAIL_FORMAT, Toast.LENGTH_LONG).show();
                            break;

                        default:
                            onBackPressed();
                            Intent intent = new Intent(Register.this, RegisterSukses.class);
                            //intent.putExtra(Config.DISP_KD_BOOKING, s);
                            startActivity(intent);
                            break;
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    //from form input
                    params.put(Config.KEY_NAME, input_nama);
                    params.put(Config.KEY_EMAIL, input_email);
                    params.put(Config.KEY_PHONE, input_phone);
                    params.put(Config.KEY_CHASIS, input_chasis);

                    params.put(Config.KEY_ENGINE, input_engine);
                    params.put(Config.KEY_NOPOL, input_nopol);
                    params.put(Config.KEY_TYPE, input_type);
                    params.put(Config.KEY_COLOR, input_color);

                    params.put(Config.KEY_HP, hp);
                    params.put(Config.KEY_SIM_ID, simID);
                    params.put(Config.KEY_IMEI, imei);
                    params.put(Config.MAPS_LAT, "");
                    params.put(Config.MAPS_LNG, "");
                    params.put(Config.KEY_BRAND, brand);
                    params.put(Config.KEY_DEVICE, nama_device);
                    params.put(Config.KEY_MODEL, model);
                    params.put(Config.KEY_ID_MODEL, id_model);
                    params.put(Config.KEY_NAMA_PRODUK, nama_produk);
                    params.put(Config.KEY_API_SDK, versi_api_sdk);
                    params.put(Config.KEY_OS, os);

                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Config.URL_ADD_REGISTER, params);
                }
            }

            Addregister run = new Addregister();
            run.execute();

            ForceCloseDebugger.handle(this);

        }
    }


}
