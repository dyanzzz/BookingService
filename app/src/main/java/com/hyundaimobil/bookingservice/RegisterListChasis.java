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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyundaimobil.bookingservice.adapter.RegisterListChasisAdapter;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.data.RegisterListChasisData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterListChasis extends AppCompatActivity implements View.OnClickListener,
        ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<RegisterListChasisData> listChasis = new ArrayList<>();
    private static final String TAG = RegisterListChasis.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    int no;
    RegisterListChasisAdapter adapter;
    ProgressDialog loading;

    EditText editTextChasis;
    Button buttonSearchChasis;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_list_chasis);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent   = getIntent();
        id              = intent.getStringExtra(Config.TAG_ID);

        editTextChasis      = findViewById(R.id.editTextChasis);
        buttonSearchChasis  = findViewById(R.id.buttonSearchChasis);
        buttonSearchChasis.setOnClickListener(this);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView            = findViewById(R.id.list_chasis);
        listView.setOnItemClickListener(this);

        adapter = new RegisterListChasisAdapter(RegisterListChasis.this, listChasis);
        listView.setAdapter(adapter);
        listChasis.clear();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(RegisterListChasis.this)) {
                    listChasis.clear();
                    adapter.notifyDataSetChanged();
                    loading = ProgressDialog.show(RegisterListChasis.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    getJSON_show();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });
    }

    public void onClick(View v){
        if(Config.CEK_KONEKSI(RegisterListChasis.this)) {
            if (v == buttonSearchChasis) {
                buttonSearchChasisFunction();
            }
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void buttonSearchChasisFunction(){
        final String chasis     = editTextChasis.getText().toString();

        if (chasis.equals("")){
            Toast.makeText(RegisterListChasis.this, Config.ALERT_CHASIS, Toast.LENGTH_SHORT).show();
            //editTextChasis.requestFocus();

        } else {
            if (Config.CEK_KONEKSI(RegisterListChasis.this)) {
                loading = ProgressDialog.show(RegisterListChasis.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                listChasis.clear();
                adapter.notifyDataSetChanged();
                getJSON_show();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                showDialog(Config.TAMPIL_ERROR);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(RegisterListChasis.this)) {
            listChasis.clear();
            adapter.notifyDataSetChanged();
            loading = ProgressDialog.show(RegisterListChasis.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            getJSON_show();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void getJSON_show() {
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_CHASIS_REGISTER,
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
                                    RegisterListChasisData ambil = new RegisterListChasisData();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    //ambil.setName(obj.getString(Config.DISP_CUST_NAME));
                                    ambil.setChasis(obj.getString(Config.DISP_UNIT_CHASIS));
                                    ambil.setEngine(obj.getString(Config.DISP_UNIT_ENGINE));
                                    ambil.setNopol(obj.getString(Config.DISP_UNIT_POLICE));
                                    ambil.setType(obj.getString(Config.DISP_UNIT_TYPE));
                                    ambil.setColor(obj.getString(Config.DISP_UNIT_COLOUR));
                                    ambil.setKdType(obj.getString(Config.DISP_UNIT_KDTYPE));
                                    ambil.setKdColor(obj.getString(Config.DISP_UNIT_KDCOLOUR));

                                    // adding news to news array
                                    listChasis.add(ambil);

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
                Toast.makeText(RegisterListChasis.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                params.put(Config.KEY_SEARCH, editTextChasis.getText().toString());
                params.put(Config.TAG_ID, id);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id_long) {
        if(Config.CEK_KONEKSI(RegisterListChasis.this)) {

            //String kodeName     = listChasis.get(position).getName();
            String kodeChasis   = listChasis.get(position).getChasis();
            String kodeEngine   = listChasis.get(position).getEngine();
            String kodeNopol    = listChasis.get(position).getNopol();
            String kodeType     = listChasis.get(position).getKdType();
            String kodeColor    = listChasis.get(position).getKdColor();

            if(!kodeEngine.equals("")){
                if(id.equals("1")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Config.DISP_UNIT_CHASIS, kodeChasis);
                    resultIntent.putExtra(Config.DISP_UNIT_ENGINE, kodeEngine);
                    resultIntent.putExtra(Config.DISP_UNIT_POLICE, kodeNopol);
                    resultIntent.putExtra(Config.DISP_UNIT_KDTYPE, kodeType);
                    resultIntent.putExtra(Config.DISP_UNIT_KDCOLOUR, kodeColor);

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }else{
                    Intent intent = new Intent(RegisterListChasis.this, ForgotPassword.class);
                    intent.putExtra(Config.DISP_UNIT_CHASIS, kodeChasis);
                    startActivity(intent);
                }
            }

        }else{
            Toast.makeText(RegisterListChasis.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
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
