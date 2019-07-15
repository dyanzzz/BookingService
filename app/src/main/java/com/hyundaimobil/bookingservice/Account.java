package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class Account extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis;
    private TextView customerName, address1, address2, city, phone1, phone2, mobile1, mobile2, email;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
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
        login_number    = user.get(SessionManager.KEY_LOGIN_NUMBER);
        kd_customer     = user.get(SessionManager.KEY_KD_CUSTOMER);
        chasis          = user.get(SessionManager.KEY_CHASIS);

        //Toast.makeText(getApplicationContext(), kd_customer + "-" + chasis,Toast.LENGTH_SHORT).show();

        customerName  = findViewById(R.id.tvCustomerName);
        address1      = findViewById(R.id.tvAddress);
        address2      = findViewById(R.id.tvAddress2);
        city          = findViewById(R.id.tvCity);
        phone1        = findViewById(R.id.tvPhone);
        phone2        = findViewById(R.id.tvPhone2);
        mobile1       = findViewById(R.id.tvMobile);
        mobile2       = findViewById(R.id.tvMobile2);
        email         = findViewById(R.id.tvEmail);

        //nama    = (TextView) findViewById(R.id.nama);
        //nama.setText(fullname);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(Account.this)) {
                    getJSON_customer();

                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(Account.this)) {
            getJSON_customer();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
        }
    }


    private void getJSON_customer(){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Account.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(Account.this, s, Toast.LENGTH_SHORT).show();
                showData_customer(s);
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(SessionManager.KEY_KD_CUSTOMER, kd_customer);
                params.put(SessionManager.KEY_CHASIS, chasis);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_ACCOUNT, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    private void showData_customer(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result      = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c          = result.getJSONObject(0);
            String disp_name           = c.getString(Config.DISP_CUST_NAME);
            String disp_address1       = c.getString(Config.DISP_CUST_ADR1);
            String disp_address2       = c.getString(Config.DISP_CUST_ADR2);
            String disp_kota           = c.getString(Config.DISP_CUST_KOTA);
            String disp_phone1         = c.getString(Config.DISP_CUST_PHONE1);
            String disp_phone2         = c.getString(Config.DISP_CUST_PHONE2);
            String disp_mobile1        = c.getString(Config.DISP_CUST_MOBILE1);
            String disp_mobile2        = c.getString(Config.DISP_CUST_MOBILE2);
            String disp_email          = c.getString(Config.DISP_CUST_EMAIL);

            customerName.setText(disp_name);
            address1.setText(disp_address1);
            address2.setText(disp_address2);
            city.setText(disp_kota);
            phone1.setText(disp_phone1);
            phone2.setText(disp_phone2);
            mobile1.setText(disp_mobile1);
            mobile2.setText(disp_mobile2);
            email.setText(disp_email);

        } catch (JSONException e){
            e.printStackTrace();
            //Toast.makeText(Account.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Account.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    //controll memanggil item toolbar untuk menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.item_unit, menu);
        return true;
    }

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.unit:
                if(Config.CEK_KONEKSI(Account.this)) {
                    Intent intent = new Intent(Account.this, PilihUnit.class);
                    intent.putExtra(Config.TAG_ID, "1");
                    startActivity(intent);
                }else{
                    showDialog(Config.TAMPIL_ERROR);
                }
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
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
}
