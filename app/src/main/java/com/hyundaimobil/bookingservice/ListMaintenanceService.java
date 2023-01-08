package com.hyundaimobil.bookingservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyundaimobil.bookingservice.adapter.ListMaintenanceServiceAdapter;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.data.ListMaintenanceServiceData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListMaintenanceService extends AppCompatActivity implements
        ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, status_booking;
    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ListMaintenanceServiceData> listMaintenance = new ArrayList<>();
    private static final String TAG = ListMaintenanceService.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    int no;
    ListMaintenanceServiceAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_maintenance_service);

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
        status_booking  = "";

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView            = findViewById(R.id.list_maintenance_service);
        listView.setOnItemClickListener(this);

        adapter = new ListMaintenanceServiceAdapter(ListMaintenanceService.this, listMaintenance);
        listView.setAdapter(adapter);
        listMaintenance.clear();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(ListMaintenanceService.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    listMaintenance.clear();
                    adapter.notifyDataSetChanged();
                    loading = ProgressDialog.show(ListMaintenanceService.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    getJSON_show();
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
        if (Config.CEK_KONEKSI(ListMaintenanceService.this)) {
            listMaintenance.clear();
            adapter.notifyDataSetChanged();
            loading = ProgressDialog.show(ListMaintenanceService.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            getJSON_show();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void getJSON_show() {
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_ALL_MAINTENANCE_SERVICE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);

                        if (response.length() > 0) {
                            // Parsing json
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ListMaintenanceServiceData ambil = new ListMaintenanceServiceData();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    ambil.setCoy(obj.getString(Config.DISP_LTS_COY));
                                    ambil.setLts(obj.getString(Config.DISP_LTS_KD));
                                    ambil.setName(obj.getString(Config.DISP_LTS_NAME));

                                    // adding news to news array
                                    listMaintenance.add(ambil);

                                    Log.e(TAG, "offSet ");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();

                        }
                        mSwipeRefreshLayout.setRefreshing(false);

                        loading.dismiss();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListMaintenanceService.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Config.CEK_KONEKSI(ListMaintenanceService.this)) {

            String kodeCoy  = listMaintenance.get(position).getCoy();
            String kodeLts  = listMaintenance.get(position).getLts();
            String kodeName = listMaintenance.get(position).getName();

            if(!kodeCoy.equals("")){
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Config.DISP_LTS_COY, kodeCoy);
                resultIntent.putExtra(Config.DISP_LTS_KD, kodeLts);
                resultIntent.putExtra(Config.DISP_LTS_NAME, kodeName);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        }else{
            Toast.makeText(ListMaintenanceService.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
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
}
