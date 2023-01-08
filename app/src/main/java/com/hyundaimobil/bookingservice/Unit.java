package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

public class Unit extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, kodeChasis, kodeEngine, kodeCustomer;
    private TextView tvChasis,tvEngine, tvPolice, tvModel, tvColour;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

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

        Intent intent   = getIntent();
        kodeChasis      = intent.getStringExtra(Config.DISP_UNIT_CHASIS);
        kodeEngine      = intent.getStringExtra(Config.DISP_UNIT_ENGINE);
        kodeCustomer    = intent.getStringExtra(Config.DISP_KD_CUSTOMER);

        tvChasis        = findViewById(R.id.tvChasis);
        tvEngine        = findViewById(R.id.tvEngine);
        tvPolice        = findViewById(R.id.tvPolice);
        tvModel         = findViewById(R.id.tvModel);
        tvColour        = findViewById(R.id.tvColour);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(Unit.this)) {
                    getJSON_unit();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                    //Toast.makeText(Unit.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(Unit.this)) {
            getJSON_unit();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
            //Toast.makeText(Unit.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }


    private void getJSON_unit(){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Unit.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(Account.this, s, Toast.LENGTH_SHORT).show();
                showData_unit(s);
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(SessionManager.KEY_KD_CUSTOMER, kodeCustomer);
                params.put(SessionManager.KEY_CHASIS, kodeChasis);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_ACCOUNT, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    private void showData_unit(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result      = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c          = result.getJSONObject(0);
            String disp_chasis    = c.getString(Config.DISP_UNIT_CHASIS);
            String disp_engine    = c.getString(Config.DISP_UNIT_ENGINE);
            String disp_police    = c.getString(Config.DISP_UNIT_POLICE);
            String disp_model     = c.getString(Config.DISP_UNIT_MODEL);
            String disp_colour    = c.getString(Config.DISP_UNIT_COLOUR);

            tvChasis.setText(disp_chasis);
            tvEngine.setText(disp_engine);
            tvPolice.setText(disp_police);
            tvModel.setText(disp_model);
            tvColour.setText(disp_colour);

        } catch (JSONException e){
            e.printStackTrace();
            //Toast.makeText(Unit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Unit.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }
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
