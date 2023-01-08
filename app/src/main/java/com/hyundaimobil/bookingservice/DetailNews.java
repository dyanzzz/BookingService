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
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class DetailNews extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis,
            id_news, linkNews;

    //NetworkImageView thumb_image;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView judul, tgl, isi;
    ImageView thumb_image;
    SwipeRefreshLayout swipe;

    private static final String TAG = DetailNews.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

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
        id_news         = intent.getStringExtra(Config.TAG_ID);

        thumb_image = findViewById(R.id.gambar_news);
        judul       = findViewById(R.id.judul_news);
        tgl         = findViewById(R.id.tgl_news);
        isi         = findViewById(R.id.isi_news);

        swipe       = findViewById(R.id.swipe_refresh_layout);
        swipe.setColorSchemeResources(R.color.colorAccent);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(DetailNews.this)) {
                    callDetailNews(id_news);
                    swipe.setRefreshing(false);
                } else {
                    swipe.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(DetailNews.this)) {
            callDetailNews(id_news);
            swipe.setRefreshing(false);
        } else {
            swipe.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
            //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }


    private void callDetailNews(final String id){
        @SuppressLint("StaticFieldLeak")
        class GetViewRun extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(DetailNews.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showNewsSelected(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.TAG_ID, id);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_DETAIL_NEWS, params);
            }
        }
        GetViewRun jalankan = new GetViewRun();
        jalankan.execute();
    }

    private void showNewsSelected(String json){
        try{
            JSONObject jsonObject       = new JSONObject(json);
            JSONArray result            = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c                = result.getJSONObject(0);

            String Judul    = c.getString(Config.TAG_JUDUL);
            String Gambar   = c.getString(Config.TAG_GAMBAR);
            String Tgl      = c.getString(Config.TAG_TGL);
            String Isi      = c.getString(Config.TAG_ISI);
            String Link_News = c.getString(Config.TAG_LINK_NEWS);

            Picasso.with(this).load(Gambar).into(thumb_image);
            judul.setText(Judul);
            tgl.setText(Tgl);
            isi.setText(Html.fromHtml(Isi));
            linkNews = Link_News;

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void callDetailNews2(final String id) {
        byte[] data_id  = id.getBytes(StandardCharsets.UTF_8);
        String id_news  = Base64.encodeToString(data_id, Base64.NO_WRAP);

        final ProgressDialog loading = ProgressDialog.show(DetailNews.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        StringRequest strReq = new StringRequest(Request.Method.GET, Config.URL_DETAIL_NEWS+"/"+id_news,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                swipe.setRefreshing(false);

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String Judul    = obj.getString(Config.TAG_JUDUL);
                        String Gambar   = obj.getString(Config.TAG_GAMBAR);
                        String Tgl      = obj.getString(Config.TAG_TGL);
                        String Isi      = obj.getString(Config.TAG_ISI);
                        String Link_News = obj.getString(Config.TAG_LINK_NEWS);


                        Picasso.with(DetailNews.this).load(Gambar).into(thumb_image);
                        judul.setText(Judul);
                        tgl.setText(Tgl);
                        isi.setText(Html.fromHtml(Isi));
                        linkNews = Link_News;
                        //title_page.setText(Judul);

                        //if (!Objects.equals(obj.getString(Config.TAG_GAMBAR), "")) {
                            //thumb_image.setImageUrl(Gambar, imageLoader);
                        //}
                    }

                    swipe.setRefreshing(false);
                    loading.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(DetailNews.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(DetailNews.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                //Toast.makeText(DetailNews.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(DetailNews.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
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


    //menu dashboard
    public void btn_linkNews(View view) {
        if(Config.CEK_KONEKSI(DetailNews.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkNews));
            startActivity(linkIntent);
        }else{
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


}
