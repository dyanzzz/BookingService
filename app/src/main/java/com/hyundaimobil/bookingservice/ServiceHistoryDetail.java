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

public class ServiceHistoryDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    String chasis, kd_rep, kd_cab, kd_coy, kd_notranp, kd_partno, kd_nokwb, kd_nowo;
    TextView tvCompanyBranch, tvWoNo, tvWoDate, tvTrans,tvRepairCode,tvRepairModeCode, tvDescription, tvRepairQty,
            tvInitPaidByBeban, tvSalesPrice, tvTotalAmount, tvSaName, tvTechnism, tvKmIn, tvnote;

    SwipeRefreshLayout mSwipeRefreshLayout;
    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history_detail);

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

        Intent intent   = getIntent();
        kd_notranp      = intent.getStringExtra(Config.DISP_KD_NOTRANP);
        kd_partno       = intent.getStringExtra(Config.DISP_KD_PARTNO);
        kd_nokwb        = intent.getStringExtra(Config.DISP_KD_NOKWB);
        kd_nowo         = intent.getStringExtra(Config.DISP_KD_NOWO);

        tvCompanyBranch     = findViewById(R.id.tvCompany_branch);
        tvWoNo              = findViewById(R.id.tvWoNo);
        tvWoDate            = findViewById(R.id.tvWoDate);
        tvRepairModeCode    = findViewById(R.id.tvRepairModeCode);
        tvDescription       = findViewById(R.id.tvDescription);
        tvSaName            = findViewById(R.id.tvSaName);
        tvTechnism          = findViewById(R.id.tvTechnism);
        tvKmIn              = findViewById(R.id.tvKmIn);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(Config.CEK_KONEKSI(ServiceHistoryDetail.this)) {
                    getViewServiceHistoryDetail();
                }else{
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if(Config.CEK_KONEKSI(ServiceHistoryDetail.this)) {
            getViewServiceHistoryDetail();
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
        }
    }


    private void getViewServiceHistoryDetail(){
        @SuppressLint("StaticFieldLeak")
        class GetView extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ServiceHistoryDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showServiceHistoryDetail(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_NOTRANP, kd_notranp);
                params.put(Config.DISP_KD_PARTNO, kd_partno);
                params.put(Config.DISP_KD_NOKWB, kd_nokwb);
                params.put(Config.DISP_KD_NOWO, kd_nowo);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_SERVICE_HISTORY_DET, params);
            }
        }
        GetView run = new GetView();
        run.execute();
    }

    private void showServiceHistoryDetail(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result        = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c            = result.getJSONObject(0);
            String dealer          = c.getString(Config.DISP_NAMA_DEALER);
            String no_wo            = c.getString(Config.DISP_WO);
            String date_wo          = c.getString(Config.DISP_WO_DATE);

            String repair_mode_code = c.getString(Config.DISP_REPAIR_MODE_CODE);
            String description      = c.getString(Config.DISP_DESCRIPTION);

            String sa               = c.getString(Config.DISP_SA);
            String technism         = c.getString(Config.DISP_TECHNISM);
            String km_in            = c.getString(Config.DISP_KM_IN);

            tvCompanyBranch.setText(dealer);
            tvWoNo.setText(no_wo);
            tvWoDate.setText(date_wo);

            tvRepairModeCode.setText(repair_mode_code);
            tvDescription.setText(description);

            tvSaName.setText(sa);
            tvTechnism.setText(technism);
            tvKmIn.setText(km_in);

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
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
