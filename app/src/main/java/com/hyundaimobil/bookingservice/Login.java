package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.JSONParser;
import com.hyundaimobil.bookingservice.app.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button login;
    EditText username, password;
    String success, message, linkUpdate, IPaddress, ip, user, pass, flag_apps, versi;
    SessionManager session;
    RelativeLayout relativeLayout;
    Switch showHidePassword;
    TextView buttonRegister, buttonRegisterNote, buttonForgotPassword;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        String statusLogin = String.valueOf(session.isLoggedIn());

        if(Config.CEK_KONEKSI(Login.this)) {
            if (statusLogin.equals("true")) {
                Intent intent = new Intent(Login.this, LoginPassword.class);
                startActivity(intent);
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }

        showHidePassword    = findViewById(R.id.showHidePassword);
        login               = findViewById(R.id.buttonLogin);
        username            = findViewById(R.id.editTextUsername);
        password            = findViewById(R.id.editTextPassword);
        buttonRegister      = findViewById(R.id.buttonRegister);
        buttonRegisterNote  = findViewById(R.id.buttonRegisterNote);
        buttonForgotPassword  = findViewById(R.id.buttonForgotPassword);

        buttonRegister.setOnClickListener(this);
        buttonRegisterNote.setOnClickListener(this);
        buttonForgotPassword.setOnClickListener(this);

        relativeLayout      = findViewById(R.id.relativeLayout);

        showHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                password.setSelection(password.getText().length());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.CEK_KONEKSI(Login.this)) {
                    user        = username.getText().toString().trim();
                    pass        = password.getText().toString().trim();
                    flag_apps   = Config.VALUE_FLAG_APPS;
                    versi       = Config.VALUE_VERSI;

                    //NetwordDetect();
                    //if(IPaddress.length() > 15) {
                        ip = "127.0.0.1";
                    //}else{
                    // ip = IPaddress;
                    //}

                    if (user.length() > 0 && pass.length() > 0) {
                        new PostAsync().execute(user, pass, flag_apps, ip, versi);
                    } else if (user.length() == 0) {
                        Toast.makeText(getApplicationContext(), Config.ALERT_USERNAME, Toast.LENGTH_LONG).show();
                        //Snackbar.make(relativeLayout, Config.ALERT_USERNAME, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        username.requestFocus();
                    } else if (pass.length() == 0) {
                        //Snackbar.make(relativeLayout, Config.ALERT_PASSWORD, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Toast.makeText(getApplicationContext(), Config.ALERT_PASSWORD, Toast.LENGTH_LONG).show();
                        password.requestFocus();
                    }
                } else {
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });
    }



    //method POST
    @SuppressLint("StaticFieldLeak")
    private class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            //pDialog.setTitle(Config.ALERT_LOADING);
            pDialog.setMessage(Config.ALERT_PLEASE_WAIT);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USERNAME, args[0]);
                params.put(Config.KEY_PASSWORD, args[1]);
                params.put(Config.KEY_FLAG_APPS, args[2]);
                params.put(Config.KEY_IP, args[3]);
                params.put(Config.KEY_VERSI, args[4]);

                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(Config.LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }

            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(Login.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            success     = "0";
            message     = "";
            linkUpdate  = "";

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (json != null) {
                try {
                    success     = json.getString("success");
                    message     = json.getString("message");
                    linkUpdate  = json.getString("linkUpdate");
                    JSONArray hasil = json.getJSONArray("login");
                    switch (success) {
                        case "1":
                            for (int i = 0; i < hasil.length(); i++) {
                                JSONObject c = hasil.getJSONObject(i);

                                String usercode = c.getString("usercode").trim();
                                String nip = c.getString("nip").trim();
                                String company_code = c.getString("company_code").trim();
                                String branch_code = c.getString("branch_code").trim();
                                String department_code = c.getString("department_code").trim();
                                String fullname = c.getString("fullname").trim();
                                String access_level = c.getString("access_level").trim();
                                String salesman_code = c.getString("salesman_code").trim();
                                String supervisor_code = c.getString("supervisor_code").trim();
                                String dealer_code = c.getString("dealer_code").trim();
                                String login_number = c.getString("login_number").trim();
                                String kd_customer = c.getString("kd_customer").trim();
                                String chasis = c.getString("chasis").trim();
                                session.createLoginSession(usercode, nip, company_code, branch_code, department_code,
                                        fullname, access_level, salesman_code, supervisor_code, dealer_code,
                                        login_number, kd_customer, chasis);
                                Log.e("ok", " ambil data");
                            }
                            break;
                        case "3":
                            //Update Apps
                            Log.e("error", "Update Available");
                            update_popup();
                            break;
                        default:
                            Log.e("error", "tidak bisa ambil data 1");
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success.equals("1")) {
                Log.d("Success!", message);
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Log.d("Failure", message);
                if(message.equals("")) {
                    Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    //method GET
    @SuppressLint("StaticFieldLeak")
    class GetAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            //pDialog.setTitle(Config.ALERT_LOADING);
            pDialog.setMessage(Config.ALERT_PLEASE_WAIT);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USERNAME, args[0]);
                params.put(Config.KEY_PASSWORD, args[1]);
                params.put(Config.KEY_FLAG_APPS, args[2]);
                params.put(Config.KEY_IP, args[3]);
                params.put(Config.KEY_VERSI, args[4]);

                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(Config.LOGIN_URL, "GET", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            success = "0";
            message = "";
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (json != null) {
                //Toast.makeText(Login.this, json.toString(), Toast.LENGTH_LONG).show();

                try {
                    success = json.getString("success");
                    message = json.getString("message");
                    JSONArray hasil = json.getJSONArray("login");
                    if (success.equals("1")){
                        for (int i = 0; i < hasil.length(); i++){
                            JSONObject c = hasil.getJSONObject(i);

                            String usercode         = c.getString("usercode").trim();
                            String nip              = c.getString("nip").trim();
                            String company_code     = c.getString("company_code").trim();
                            String branch_code      = c.getString("branch_code").trim();
                            String department_code  = c.getString("department_code").trim();
                            String fullname         = c.getString("fullname").trim();
                            String access_level     = c.getString("access_level").trim();
                            String salesman_code    = c.getString("salesman_code").trim();
                            String supervisor_code  = c.getString("supervisor_code").trim();
                            String dealer_code      = c.getString("dealer_code").trim();
                            String login_number     = c.getString("login_number").trim();
                            String kd_customer      = c.getString("kd_customer").trim();
                            String chasis           = c.getString("chasis").trim();
                            session.createLoginSession(usercode, nip, company_code, branch_code, department_code,
                                    fullname, access_level, salesman_code, supervisor_code, dealer_code,
                                    login_number, kd_customer, chasis);
                            Log.e("ok", " ambil data");
                        }
                    } else {
                        Log.e("error", "tidak bisa ambil data 0");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success.equals("1")) {
                Log.d("Success!", message);
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Log.d("Failure", message);
            }
        }

    }

    private void btnRegister(){
        //Toast.makeText(Login.this, "Update available", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    private void btnForgotPassword(){
        //Toast.makeText(Login.this, "Update available", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Login.this, RegisterListChasis.class);
        intent.putExtra(Config.TAG_ID, "2");
        startActivity(intent);
    }

    public void onClick(View v){
        if(Config.CEK_KONEKSI(Login.this)) {
            if(v == buttonRegister){
                btnRegister();
            }else if(v == buttonRegisterNote){
                btnRegister();
            }else if(v == buttonForgotPassword){
                btnForgotPassword();
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }


    //update popup function
    private void update_popup(){
        //Creating an alert dialog to confirm
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUpdate));
                startActivity(browserIntent);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {}
        });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    //Check the internet connection.
    private void NetwordDetect() {
        boolean WIFI = false;
        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert CM != null;
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {
            //if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                //if (netInfo.isConnected()) {
                    //WIFI = true;
                //}
            //}
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (netInfo.isConnected()) {
                    MOBILE = true;
                }
            }
        }

        //if(WIFI == true){
            //IPaddress = GetDeviceipWiFiData();
            //Toast.makeText(getApplicationContext(), IPaddress, Toast.LENGTH_LONG).show();
        //}

        if(MOBILE){
            IPaddress = GetDeviceipMobileData();
            //Toast.makeText(getApplicationContext(), IPaddress, Toast.LENGTH_LONG).show();
        }
    }

    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    //public String GetDeviceipWiFiData(){
        //WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        //@SuppressWarnings("deprecation")
        //String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        //return ip;
    //}
}