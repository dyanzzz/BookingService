package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SettingPassword extends AppCompatActivity implements View.OnClickListener{

    EditText etPasswordLama, etPasswordBaru, etPasswordBaru2;
    TextView etUsercode, etFullname;
    Button btnSaveNewPass;
    SessionManager session;
    String fullname, usercode, nip, branch_code, dept_code, company_code, access_level, salesman_code,
            supervisor_code, dealer_code;
    Switch showHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        session         = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        fullname        = user.get(SessionManager.KEY_FULLNAME);
        usercode        = user.get(SessionManager.KEY_USERCODE);
        nip             = user.get(SessionManager.KEY_NIP);
        branch_code     = user.get(SessionManager.KEY_BRANCH_CODE);
        dept_code       = user.get(SessionManager.KEY_DEPT_CODE);
        company_code    = user.get(SessionManager.KEY_COMPANY_CODE);
        access_level    = user.get(SessionManager.KEY_ACCESS_LEVEL);
        salesman_code   = user.get(SessionManager.KEY_SALESMAN_CODE);
        supervisor_code = user.get(SessionManager.KEY_SUPERVISOR_CODE);
        dealer_code     = user.get(SessionManager.KEY_DEALER_CODE);

        etUsercode      = findViewById(R.id.usercode);
        etFullname      = findViewById(R.id.fullname);
        etPasswordLama  = findViewById(R.id.passwordLama);
        etPasswordBaru  = findViewById(R.id.passwordBaru);
        etPasswordBaru2 = findViewById(R.id.passwordBaru2);

        showHidePassword    = findViewById(R.id.showHidePassword);
        btnSaveNewPass      = findViewById(R.id.buttonSaveNewPassword);
        btnSaveNewPass.setOnClickListener(this);

        showHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (!isChecked) {
                    etPasswordLama.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPasswordBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPasswordBaru2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPasswordLama.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPasswordBaru.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPasswordBaru2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                etPasswordLama.setSelection(etPasswordLama.getText().length());
                etPasswordBaru.setSelection(etPasswordBaru.getText().length());
                etPasswordBaru2.setSelection(etPasswordBaru2.getText().length());
            }
        });

        if(Config.CEK_KONEKSI(SettingPassword.this)) {
            etUsercode.setText(usercode);
            etFullname.setText(fullname);
        }else{
            //Snackbar.make(mainActivity, Config.ALERT_TITLE_CONN_ERROR, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    @Override
    public void onClick(View v) {
        if(Config.CEK_KONEKSI(SettingPassword.this)) {
            if (v == btnSaveNewPass) {
                btnSave();
            }
        }else{
            //Snackbar.make(mainActivity, Config.ALERT_TITLE_CONN_ERROR, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void btnSave(){
        final String passwordLama   = etPasswordLama.getText().toString().trim();
        final String passwordBaru   = etPasswordBaru.getText().toString().trim();
        final String passwordBaru2  = etPasswordBaru2.getText().toString().trim();

        if(passwordLama.equals("")) {
            Toast.makeText(SettingPassword.this, Config.ALERT_PASSWORD_LAMA, Toast.LENGTH_SHORT).show();
            etPasswordLama.requestFocus();
            //etPasswordLama.setError(Config.ALERT_PASSWORD_LAMA);
            //Snackbar.make(mainActivity, Config.ALERT_PASSWORD_LAMA, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else if((passwordBaru.equals("")) || (passwordBaru.length() < 4)) {
            Toast.makeText(SettingPassword.this, Config.ALERT_PASSWORD_BARU, Toast.LENGTH_SHORT).show();
            etPasswordBaru.requestFocus();
            //etPasswordBaru.setError(Config.ALERT_PASSWORD_BARU);
            //Snackbar.make(mainActivity, Config.ALERT_PASSWORD_BARU, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else if((passwordBaru2.equals("")) || (passwordBaru.length() < 4)) {
            Toast.makeText(SettingPassword.this, Config.ALERT_PASSWORD_BARU2, Toast.LENGTH_SHORT).show();
            etPasswordBaru2.requestFocus();
            //etPasswordBaru2.setError(Config.ALERT_PASSWORD_BARU2);
            //Snackbar.make(mainActivity, Config.ALERT_PASSWORD_BARU2, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else if(!passwordBaru2.equals(passwordBaru)) {
            Toast.makeText(SettingPassword.this, Config.ALERT_PASSWORD_BARU_SAMA, Toast.LENGTH_SHORT).show();
            etPasswordBaru2.requestFocus();
            //etPasswordBaru2.setError(Config.ALERT_PASSWORD_BARU_SAMA);
            //Snackbar.make(mainActivity, Config.ALERT_PASSWORD_BARU_SAMA, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            cekPasswordLama(passwordLama, passwordBaru);
        }
    }

    private void cekPasswordLama(final String passwordLama, final String passwordBaru){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SettingPassword.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(SettingPassword.this, s, Toast.LENGTH_SHORT).show();
                hasil_password_lama(s, passwordBaru);
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_PASSWORD_LAMA, passwordLama);
                params.put(Config.KEY_USERCODE, usercode);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_CEK_PASSWORD_LAMA, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void hasil_password_lama(String json, final String passwordBaru){
        try{
            JSONObject jsonObject   = new JSONObject(json);
            JSONArray result        = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c            = result.getJSONObject(0);

            String status           = c.getString("status");
            String message          = c.getString("message");

            if(status.equals("1")){

                @SuppressLint("StaticFieldLeak")
                class ChangePassword extends AsyncTask<Void, Void, String> {
                    ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(SettingPassword.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                        Toast.makeText(SettingPassword.this, s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        HashMap<String, String> params = new HashMap<>();
                        //from session
                        params.put(SessionManager.KEY_USERCODE, usercode);
                        params.put(SessionManager.KEY_FULLNAME, fullname);
                        //from form input
                        params.put(Config.KEY_PASSWORD_BARU, passwordBaru);

                        RequestHandler rh = new RequestHandler();
                        return rh.sendPostRequest(Config.URL_CHANGE_PASSWORD, params);
                    }
                }

                ChangePassword ae = new ChangePassword();
                ae.execute();

                onBackPressed();

                //Toast.makeText(getApplicationContext(), message + " -- " + passwordBaru, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingPassword.this, message, Toast.LENGTH_SHORT).show();
                //etPasswordLama.setError(message);
                etPasswordLama.requestFocus();
                //Snackbar.make(mainActivity, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(SettingPassword.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            //Toast.makeText(SettingPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            //Snackbar.make(mainActivity, e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
    }
}
