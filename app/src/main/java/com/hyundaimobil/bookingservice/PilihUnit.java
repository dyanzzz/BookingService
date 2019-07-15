package com.hyundaimobil.bookingservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.hyundaimobil.bookingservice.adapter.ListPilihUnitAdapter;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.data.ListPilihUnitData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PilihUnit extends AppCompatActivity implements
        ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ListPilihUnitData> listPilihUnit = new ArrayList<>();
    private static final String TAG = PilihUnit.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    int no;
    ListPilihUnitAdapter adapter;
    ProgressDialog loading;

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, tag_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_unit);

        ForceCloseDebugger.handle(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent   = getIntent();
        tag_id          = intent.getStringExtra(Config.TAG_ID);
        //Toast.makeText(PilihUnit.this, tag_id, Toast.LENGTH_LONG).show();

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

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView            = findViewById(R.id.list_pilih_unit);
        listView.setOnItemClickListener(this);

        adapter = new ListPilihUnitAdapter(PilihUnit.this, listPilihUnit);
        listView.setAdapter(adapter);
        listPilihUnit.clear();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(PilihUnit.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    listPilihUnit.clear();
                    adapter.notifyDataSetChanged();
                    loading = ProgressDialog.show(PilihUnit.this, "", Config.ALERT_PLEASE_WAIT, false, false);
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
        if (Config.CEK_KONEKSI(PilihUnit.this)) {
            listPilihUnit.clear();
            adapter.notifyDataSetChanged();
            loading = ProgressDialog.show(PilihUnit.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            getJSON_show();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
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



    private void getJSON_show(){
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_PILIH_UNIT,
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
                                    ListPilihUnitData ambil = new ListPilihUnitData();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    ambil.setChasis(obj.getString(Config.DISP_UNIT_CHASIS));
                                    ambil.setEngine(obj.getString(Config.DISP_UNIT_ENGINE));
                                    ambil.setNopol(obj.getString(Config.DISP_UNIT_POLICE));
                                    ambil.setType(obj.getString(Config.DISP_UNIT_TYPE));
                                    ambil.setColor(obj.getString(Config.DISP_UNIT_COLOUR));
                                    ambil.setKdColor(obj.getString(Config.DISP_UNIT_KDCOLOUR));
                                    ambil.setKdType(obj.getString(Config.DISP_UNIT_KDTYPE));
                                    ambil.setKdCustomer(obj.getString(Config.DISP_KD_CUSTOMER));

                                    // adding news to news array
                                    listPilihUnit.add(ambil);

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
                Toast.makeText(PilihUnit.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                params.put(SessionManager.KEY_KD_CUSTOMER, kd_customer);
                params.put(SessionManager.KEY_CHASIS, chasis);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Config.CEK_KONEKSI(PilihUnit.this)) {

            String kodeChasis   = listPilihUnit.get(position).getChasis();
            String kodeEngine   = listPilihUnit.get(position).getEngine();
            String kodeType     = listPilihUnit.get(position).getKdType();
            String nopol        = listPilihUnit.get(position).getNopol();
            String kdCustomer   = listPilihUnit.get(position).getKdCustomer();
            if (!kodeType.equals("0")) {
                switch (tag_id) {
                    case "1": {
                        //unit detail
                        Intent intent = new Intent(PilihUnit.this, Unit.class);
                        intent.putExtra(Config.DISP_UNIT_CHASIS, kodeChasis);
                        intent.putExtra(Config.DISP_UNIT_ENGINE, kodeEngine);
                        intent.putExtra(Config.DISP_KD_CUSTOMER, kdCustomer);
                        startActivity(intent);

                        break;
                    }
                    case "2": {
                        //return form booking service
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(Config.DISP_UNIT_POLICE, nopol);
                        resultIntent.putExtra(Config.DISP_UNIT_CHASIS, kodeChasis);
                        resultIntent.putExtra(Config.DISP_UNIT_ENGINE, kodeEngine);
                        resultIntent.putExtra(Config.DISP_KD_CUSTOMER, kdCustomer);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                        break;
                    }
                    case "3": {
                        //service history
                        Intent intent = new Intent(PilihUnit.this, ServiceHistory.class);
                        intent.putExtra(Config.DISP_UNIT_CHASIS, kodeChasis);
                        intent.putExtra(Config.DISP_UNIT_ENGINE, kodeEngine);
                        intent.putExtra(Config.DISP_KD_CUSTOMER, kdCustomer);
                        startActivity(intent);

                        break;
                    }
                    case "4": {
                        //history booking
                        Intent intent = new Intent(PilihUnit.this, HistoryBooking.class);
                        intent.putExtra(Config.DISP_UNIT_CHASIS, kodeChasis);
                        intent.putExtra(Config.DISP_UNIT_ENGINE, kodeEngine);
                        intent.putExtra(Config.DISP_KD_CUSTOMER, kdCustomer);
                        startActivity(intent);

                        break;
                    }
                }
            }

        }else{
            Toast.makeText(PilihUnit.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }
}
