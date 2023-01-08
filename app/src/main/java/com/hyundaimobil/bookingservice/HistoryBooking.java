package com.hyundaimobil.bookingservice;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyundaimobil.bookingservice.adapter.ListBookingAdapter;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.data.ListBookingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryBooking extends AppCompatActivity implements
        ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, status_booking,
            kodeChasis, kodeEngine, kodeCustomer;
    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ListBookingData> listBooking = new ArrayList<>();
    private static final String TAG = HistoryBooking.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    public int offSet = 0;
    int no;
    ListBookingAdapter adapter;
    //ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking);

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
        status_booking  = "";

        Intent intent   = getIntent();
        kodeChasis      = intent.getStringExtra(Config.DISP_UNIT_CHASIS);
        kodeEngine      = intent.getStringExtra(Config.DISP_UNIT_ENGINE);
        kodeCustomer    = intent.getStringExtra(Config.DISP_KD_CUSTOMER);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView            = findViewById(R.id.list_booking);
        listView.setOnItemClickListener(this);

        adapter = new ListBookingAdapter(HistoryBooking.this, listBooking);
        listView.setAdapter(adapter);
        listBooking.clear();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(HistoryBooking.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    listBooking.clear();
                    adapter.notifyDataSetChanged();
                    offSet = 0;
                    //loading = ProgressDialog.show(HistoryBooking.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
                    getJSON_showBooking(offSet, 0);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                    //Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        });

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
                    final ProgressDialog loading2 = ProgressDialog.show(HistoryBooking.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    //mSwipeRefreshLayout.setRefreshing(true);
                    handler = new Handler();
                    runnable = new Runnable() {
                        public void run() {
                            if (Config.CEK_KONEKSI(HistoryBooking.this)) {
                                getJSON_showBooking(offSet, 1);
                                loading2.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                loading2.dismiss();
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

    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(HistoryBooking.this)) {
            listBooking.clear();
            adapter.notifyDataSetChanged();
            offSet = 0;
            //loading = ProgressDialog.show(HistoryBooking.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            getJSON_showBooking(offSet, 0);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
            //Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }

    private void getJSON_showBooking(int page, int inisialNotFound) {
        //mSwipeRefreshLayout.setRefreshing(true);

        final ProgressDialog loading = ProgressDialog.show(HistoryBooking.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_BOOKING + "/" + page + "/" + inisialNotFound,
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
                                    ListBookingData ambil = new ListBookingData();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    ambil.setIdBooking(obj.getString(Config.DISP_KD_BOOKING));
                                    ambil.setCoyDealer(obj.getString(Config.DISP_KD_COY));
                                    ambil.setIdDealer(obj.getString(Config.DISP_KD_DEALER));
                                    ambil.setNamaDealer(obj.getString(Config.DISP_NAMA_DEALER));
                                    ambil.setDate(obj.getString(Config.DISP_DATE));
                                    ambil.setTime(obj.getString(Config.DISP_TIME));
                                    ambil.setRemarks(obj.getString(Config.DISP_REMARKS));
                                    ambil.setNote(obj.getString(Config.DISP_NOTE));
                                    ambil.setStatusBooking(obj.getString(Config.KEY_STATUS_BOOKING));

                                    // adding news to news array
                                    listBooking.add(ambil);

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
                Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                params.put(Config.KEY_KD_CUSTOMER, kodeCustomer);
                params.put(Config.KEY_CHASIS, kodeChasis);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Config.CEK_KONEKSI(HistoryBooking.this)) {

            String kodeBooking           = listBooking.get(position).getIdBooking();
            String coyDealer             = listBooking.get(position).getCoyDealer();
            String kodeDealer            = listBooking.get(position).getIdDealer();

            if(!kodeBooking.equals("")){
                Intent intent = new Intent(HistoryBooking.this, ListBookingDetail.class);
                intent.putExtra(Config.DISP_KD_BOOKING, kodeBooking);
                intent.putExtra(Config.DISP_KD_COY, coyDealer);
                intent.putExtra(Config.DISP_KD_DEALER, kodeDealer);
                startActivity(intent);
            }

        }else{
            Toast.makeText(HistoryBooking.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
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
