package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.SessionManager;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.hyundaimobil.bookingservice.PromotionsGetAllImages.namaImage;

public class Promotions extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;
    public PromotionsGetAllImages getAllImages;

    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

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

        listView        = findViewById(R.id.list_promotions);
        listView.setOnItemClickListener(this);

        getJSON_showPromotions();
    }



    private void getJSON_showPromotions(){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<String, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Promotions.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(FollowupCustomer.this, s, Toast.LENGTH_SHORT).show();
                getAllImages = new PromotionsGetAllImages(s);
                showDataPromotions();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con   = (HttpURLConnection) url.openConnection();
                    StringBuilder sb        = new StringBuilder();
                    bufferedReader          = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;

                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json).append("\n");
                    }

                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(Config.URL_GET_ALL_PROMOTIONS);
    }

    private void showDataPromotions(){
        @SuppressLint("StaticFieldLeak")
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Promotions.this, "",Config.ALERT_PLEASE_WAIT,false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                //Toast.makeText(ImageListView.this,"Success",Toast.LENGTH_LONG).show();
                PromotionsCustomList customList = new PromotionsCustomList(Promotions.this,
                        PromotionsGetAllImages.imageURLs, PromotionsGetAllImages.bitmaps,
                        namaImage, PromotionsGetAllImages.deskripsiImage);

                listView.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAllImages.getAllImages();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        //String namaPromotion        = map.get(Config.TAG_ALL_NAMA_PROMOTIONS).toString();
        //String deskripsiPromotion   = map.get(Config.TAG_ALL_DESKRIPSI_PROMOTIONS).toString();


        //String deskripsiPromotion   = map.get(Config.TAG_ALL_DESKRIPSI_PROMOTIONS).toString();

        /*
        Intent intent = new Intent(Promotions.this, PromotionsDetail.class);
        intent.putExtra(Config.TAG_ALL_NAMA_PROMOTIONS, namaPromotion);
        intent.putExtra(Config.TAG_ALL_DESKRIPSI_PROMOTIONS, deskripsiPromotion);
        startActivity(intent);
        */

        //Toast.makeText(Promotions.this, "tes", Toast.LENGTH_LONG).show();
        //Toast.makeText(Promotions.this, customList.getItem(2), Toast.LENGTH_LONG).show();
        //Toast.makeText(Promotions.this, customList.getItem(3), Toast.LENGTH_LONG).show();
        //Toast.makeText(Promotions.this, customList.getItem(4), Toast.LENGTH_LONG).show();

    }
}

