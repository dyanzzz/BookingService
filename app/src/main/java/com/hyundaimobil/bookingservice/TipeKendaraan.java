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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.hyundaimobil.bookingservice.adapter.ListTipeKendaraanAdapter;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.data.ListTipeKendaraanData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TipeKendaraan extends AppCompatActivity implements
        ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, status_booking;

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ListTipeKendaraanData> listTipeKendaraan = new ArrayList<>();
    private static final String TAG = TipeKendaraan.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    public int offSet = 0;
    int no;
    ListTipeKendaraanAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipe_kendaraan);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        //session.checkLogin();
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
        listView = findViewById(R.id.tipe_kendaraan);
        listView.setOnItemClickListener(this);

        adapter = new ListTipeKendaraanAdapter(TipeKendaraan.this, listTipeKendaraan);
        listView.setAdapter(adapter);
        listTipeKendaraan.clear();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(TipeKendaraan.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    listTipeKendaraan.clear();
                    adapter.notifyDataSetChanged();
                    offSet = 0;
                    loading = ProgressDialog.show(TipeKendaraan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    getJSON_showTipeKendaraan(offSet, 0);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                    //Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        });
        /*
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
                    loading = ProgressDialog.show(TipeKendaraan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    //mSwipeRefreshLayout.setRefreshing(true);
                    handler = new Handler();
                    runnable = new Runnable() {
                        public void run() {
                            if (Config.CEK_KONEKSI(TipeKendaraan.this)) {
                                getJSON_showTipeKendaraan(offSet, 1);
                                //loading.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                loading.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                                showDialog(Config.TAMPIL_ERROR);
                                //Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }

        });
        */
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(TipeKendaraan.this)) {
            listTipeKendaraan.clear();
            adapter.notifyDataSetChanged();
            offSet = 0;
            loading = ProgressDialog.show(TipeKendaraan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            getJSON_showTipeKendaraan(offSet, 0);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
            //Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }


    private void getJSON_showTipeKendaraan(int page, int inisialNotFound) {
        //mSwipeRefreshLayout.setRefreshing(true);
        //loading = ProgressDialog.show(HistoryBooking.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_TIPE_KENDARAAN + "/" + page + "/" + inisialNotFound,
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
                                    ListTipeKendaraanData ambil = new ListTipeKendaraanData();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    ambil.setKdKendaraan(obj.getString(Config.DISP_KD_KENDARAAN));
                                    ambil.setNamaKendaraan(obj.getString(Config.DISP_NAMA_KENDARAAN));
                                    ambil.setImage(obj.getString(Config.DISP_IMAGE_KENDARAAN));

                                    // adding news to news array
                                    listTipeKendaraan.add(ambil);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.e(TAG, "offSet " + offSet);
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
                Toast.makeText(TipeKendaraan.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        });
        /*
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put(Config.KEY_KD_COY, company_code);
                //params.put(Config.KEY_KD_CAB, branch_code);
                //params.put(Config.KEY_KD_CUSTOMER, kd_customer);
                //params.put(Config.KEY_CHASIS, chasis);
                return params;
            }
        };
        */

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Config.CEK_KONEKSI(TipeKendaraan.this)) {

            String kdKendaraan      = listTipeKendaraan.get(position).getKdKendaraan();
            String namaKendaraan    = listTipeKendaraan.get(position).getNamaKendaraan();

            if(!kdKendaraan.equals("")){
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Config.DISP_KD_KENDARAAN, kdKendaraan);
                resultIntent.putExtra(Config.DISP_NAMA_KENDARAAN, namaKendaraan);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        }else{
            Toast.makeText(TipeKendaraan.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
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
