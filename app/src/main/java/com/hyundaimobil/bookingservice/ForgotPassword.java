package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {

    EditText editTextemail;
    Button buttonForgotPassword;
    String chasis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Intent intent   = getIntent();
        chasis          = intent.getStringExtra(Config.DISP_UNIT_CHASIS);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        buttonForgotPassword    = findViewById(R.id.buttonForgotPassword);
        editTextemail           = findViewById(R.id.editTextemail);
    }

    public void onClick(View v) {
        if (Config.CEK_KONEKSI(ForgotPassword.this)) {
            if (v == buttonForgotPassword) {
                btnForgotPass();
            }
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void btnForgotPass() {
        final String email = editTextemail.getText().toString().trim();

        if (email.equals("")) {
            Toast.makeText(ForgotPassword.this, Config.ALERT_EMAIL, Toast.LENGTH_SHORT).show();
        }else{

            @SuppressLint("StaticFieldLeak")
            class SendForgotPassword extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute(){
                    super.onPreExecute();
                    loading = ProgressDialog.show(ForgotPassword.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();

                    switch (s) {

                        case "0":
                            Toast.makeText(ForgotPassword.this, Config.ALERT_EMAIL_NOT_REGISTER, Toast.LENGTH_SHORT).show();
                            break;

                        case "1":
                            Toast.makeText(ForgotPassword.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            onBackPressed();
                            Intent intent = new Intent(ForgotPassword.this, ForgotPasswordSukses.class);
                            intent.putExtra(Config.KEY_EMAIL, email);
                            intent.putExtra(Config.DISP_UNIT_CHASIS, chasis);
                            startActivity(intent);
                            break;
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.KEY_EMAIL, email);
                    params.put(Config.DISP_UNIT_CHASIS, chasis);
                    RequestHandler rh = new RequestHandler();
                    return rh.sendPostRequest(Config.URL_FORGOT_PASSWORD, params);
                }
            }

            SendForgotPassword run = new SendForgotPassword();
            run.execute();

            ForceCloseDebugger.handle(this);

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

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
