package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PromotionsDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis,
            kdPromosi, linkPromotions;

    TextView nama, deskripsi, title_page;
    ImageView img_promotion;
    SwipeRefreshLayout swipe;
    private static final String TAG = PromotionsDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions_detail);

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

        Intent intent   = getIntent();
        kdPromosi       = intent.getStringExtra(Config.TAG_ALL_KD_PROMOTIONS);

        img_promotion       = findViewById(R.id.img_promotion);
        nama                = findViewById(R.id.nama);
        deskripsi           = findViewById(R.id.deskripsi);
        title_page          = findViewById(R.id.title_page);

        swipe = findViewById(R.id.swipe_refresh_layout);
        swipe.setColorSchemeResources(R.color.colorAccent);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(PromotionsDetail.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    getViewPromotion();
                    swipe.setRefreshing(false);
                } else {
                    swipe.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                    //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(PromotionsDetail.this)) {
            getViewPromotion();
            swipe.setRefreshing(false);
        } else {
            swipe.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
            //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }


    private void getViewPromotion2() {
        byte[] data_id      = kdPromosi.getBytes(StandardCharsets.UTF_8);
        String id_promosi   = Base64.encodeToString(data_id, Base64.NO_WRAP);

        final ProgressDialog loading = ProgressDialog.show(PromotionsDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        StringRequest strReq = new StringRequest(Request.Method.GET, Config.URL_GET_SELECTED_PROMOTIONS+"/"+id_promosi,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        swipe.setRefreshing(false);

                        try {
                            JSONObject jsonObject       = new JSONObject(response);
                            JSONArray result            = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                            JSONObject c                = result.getJSONObject(0);
                            String kd_promotions        = c.getString(Config.TAG_ALL_KD_PROMOTIONS);
                            String nama_promotions      = c.getString(Config.TAG_ALL_NAMA_PROMOTIONS);
                            String deskripsi_promotions = c.getString(Config.TAG_ALL_DESKRIPSI_PROMOTIONS);
                            String link_img_promotions  = c.getString(Config.TAG_ALL_PROMOTIONS);
                            String link_promotions      = c.getString(Config.TAG_ALL_LINK_PROMOTIONS);

                            Picasso.with(PromotionsDetail.this).load(link_img_promotions).into(img_promotion);
                            nama.setText(nama_promotions);
                            deskripsi.setText(Html.fromHtml(deskripsi_promotions));
                            linkPromotions = link_promotions;

                            swipe.setRefreshing(false);
                            loading.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(DetailNews.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                //Toast.makeText(DetailNews.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        }) {
            /*@Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<>();
                params.put(Config.TAG_ID, id);
                return params;
            }*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                String creds = String.format("%s:%s",Config.usernameHttps, Config.passwordHttps);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


    private void getViewPromotion(){
        @SuppressLint("StaticFieldLeak")
        class GetViewPromotion extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(PromotionsDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showPromotionSelected(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.TAG_ALL_KD_PROMOTIONS, kdPromosi);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_SELECTED_PROMOTIONS, params);
            }
        }
        GetViewPromotion jalankan = new GetViewPromotion();
        jalankan.execute();
    }

    private void showPromotionSelected(String json){
        try{
            JSONObject jsonObject       = new JSONObject(json);
            JSONArray result            = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c                = result.getJSONObject(0);
            String kd_promotions        = c.getString(Config.TAG_ALL_KD_PROMOTIONS);
            String nama_promotions      = c.getString(Config.TAG_ALL_NAMA_PROMOTIONS);
            String deskripsi_promotions = c.getString(Config.TAG_ALL_DESKRIPSI_PROMOTIONS);
            String link_img_promotions  = c.getString(Config.TAG_ALL_PROMOTIONS);
            String link_promotions      = c.getString(Config.TAG_ALL_LINK_PROMOTIONS);

            Picasso.with(this).load(link_img_promotions).into(img_promotion);
            nama.setText(nama_promotions);
            deskripsi.setText(Html.fromHtml(deskripsi_promotions));
            linkPromotions = link_promotions;

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }
    }


    public void btn_linkPromotions(View view) {
        if(Config.CEK_KONEKSI(PromotionsDetail.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPromotions));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
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
