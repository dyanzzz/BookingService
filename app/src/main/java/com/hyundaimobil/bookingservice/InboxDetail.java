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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InboxDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis,
            kdInbox, linkInbox;
    TextView tgl_inbox, title_inbox, message_inbox;
    ImageView img_inbox;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);

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
        kdInbox         = intent.getStringExtra(Config.DISP_KD_INBOX);

        img_inbox       = findViewById(R.id.img_inbox);
        tgl_inbox       = findViewById(R.id.tgl_inbox);
        title_inbox     = findViewById(R.id.title_inbox);
        message_inbox   = findViewById(R.id.message_inbox);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(InboxDetail.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    getViewInboxDetail();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                    //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(InboxDetail.this)) {
            getViewInboxDetail();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
            //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }

    private void getViewInboxDetail(){
        @SuppressLint("StaticFieldLeak")
        class GetViewStart extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(InboxDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showInboxSelected(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_INBOX, kdInbox);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_INBOX_DETAIL, params);
            }
        }
        GetViewStart jalankan = new GetViewStart();
        jalankan.execute();
    }

    private void showInboxSelected(String json){
        try{
            JSONObject jsonObject   = new JSONObject(json);
            JSONArray result        = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c            = result.getJSONObject(0);
            String kd               = c.getString(Config.DISP_KD_INBOX);
            String date             = c.getString(Config.DISP_DATE);
            String title            = c.getString(Config.DISP_TITLE);
            String message          = c.getString(Config.DISP_MESSAGE);
            String link_img         = c.getString(Config.DISP_URL_IMG_INBOX);
            String link_inbox       = c.getString(Config.DISP_URL_INBOX);

            Picasso.with(this).load(link_img).into(img_inbox);
            tgl_inbox.setText(date);
            title_inbox.setText(title);
            message_inbox.setText(Html.fromHtml(message));
            linkInbox = link_inbox;

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_linkInbox(View view) {
        if(Config.CEK_KONEKSI(InboxDetail.this)) {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkInbox));
            startActivity(linkIntent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    //dialog disconect
    @Override
    protected Dialog onCreateDialog(int id) {
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
