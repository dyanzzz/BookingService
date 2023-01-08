package com.hyundaimobil.bookingservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hyundaimobil.bookingservice.data.ListBookingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBookingProgress extends Fragment implements
        ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    ListView listView;
    String coy, cab, customer_code, chasis, status_booking;
    SwipeRefreshLayout mSwipeRefreshLayout;


    List<ListBookingData> listBookingProgress = new ArrayList<ListBookingData>();
    private static final String TAG = ListBookingProgress.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    public int offSet = 0;
    int no;
    ListBookingAdapter adapter;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_booking_progress, container, false);

        String showCoy              = "0";
        String showCab              = "0";
        String showCustomer_Kode    = "0";
        String showChasis           = "0";
/*
        String showCoy              = activity.getCoy();
        String showCab              = activity.getCab();
        String showCustomer_Kode    = activity.getCustomer_Kode();
        String showChasis           = activity.getChasis();
        */
        coy             = showCoy;
        cab             = showCab;
        customer_code   = showCustomer_Kode;
        chasis          = showChasis;
        status_booking  = "0";

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) v.findViewById(R.id.list_booking_progress);
        listView.setOnItemClickListener(this);

        adapter = new ListBookingAdapter(getActivity(), listBookingProgress);
        listView.setAdapter(adapter);
        listBookingProgress.clear();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(getActivity())) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    listBookingProgress.clear();
                    adapter.notifyDataSetChanged();
                    getJSON_showBooking(0, 0);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
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
                    final ProgressDialog loading2 = ProgressDialog.show(getActivity(), Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
                    //mSwipeRefreshLayout.setRefreshing(true);
                    handler = new Handler();
                    runnable = new Runnable() {
                        public void run() {
                            if (Config.CEK_KONEKSI(getActivity())) {
                                getJSON_showBooking(offSet, 1);
                                loading2.dismiss();
                            } else {
                                loading2.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }

        });

        /*
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Config.CEK_KONEKSI(getActivity())) {
                    getJSON_showBooking();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        getJSON_showBooking();
        */
        return v;
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(getActivity())) {
            listBookingProgress.clear();
            adapter.notifyDataSetChanged();
            getJSON_showBooking(0, 0);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }




    private void getJSON_showBooking(int page, int inisialNotFound) {
        //mSwipeRefreshLayout.setRefreshing(true);

        final ProgressDialog loading = ProgressDialog.show(getActivity(), Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_BOOKING + "/" + page + "/" + inisialNotFound,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    //JSONObject obj = response.getJSONObject(i);
                                    ListBookingData ambil = new ListBookingData();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    ambil.setIdBooking(obj.getString(Config.DISP_KD_BOOKING));
                                    ambil.setIdDealer(obj.getString(Config.DISP_KD_DEALER));
                                    ambil.setNamaDealer(obj.getString(Config.DISP_NAMA_DEALER));
                                    ambil.setDate(obj.getString(Config.DISP_DATE));
                                    ambil.setTime(obj.getString(Config.DISP_TIME));
                                    ambil.setRemarks(obj.getString(Config.DISP_REMARKS));

                                    // adding news to news array
                                    listBookingProgress.add(ambil);

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
                Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Config.KEY_KD_COY, coy);
                params.put(Config.KEY_KD_CAB, cab);
                params.put(Config.KEY_KD_CUSTOMER, customer_code);
                params.put(Config.KEY_CHASIS, chasis);
                params.put(Config.KEY_STATUS_BOOKING, status_booking);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }



/*
    private void getJSON_showBooking2(){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(FollowupCustomer.this, s, Toast.LENGTH_SHORT).show();
                showDataBooking();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_KD_COY, coy);
                params.put(Config.KEY_KD_CAB, cab);
                params.put(Config.KEY_KD_CUSTOMER, customer_code);
                params.put(Config.KEY_CHASIS, chasis);
                params.put(Config.KEY_STATUS_BOOKING, status_booking);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_BOOKING, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataBooking(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String kodeBooking  = jo.getString(Config.DISP_KD_BOOKING);
                String nomor        = jo.getString(Config.DISP_NOMOR);
                String kodeDealer   = jo.getString(Config.DISP_KD_DEALER);
                String namaDealer   = jo.getString(Config.DISP_NAMA_DEALER);
                String date         = jo.getString(Config.DISP_DATE);
                String time         = jo.getString(Config.DISP_TIME);
                String remarks      = jo.getString(Config.DISP_REMARKS);

                HashMap<String, String> viewBooking = new HashMap<>();
                viewBooking.put(Config.DISP_KD_BOOKING, kodeBooking);
                viewBooking.put(Config.DISP_NOMOR, nomor);
                viewBooking.put(Config.DISP_KD_DEALER, kodeDealer);
                viewBooking.put(Config.DISP_NAMA_DEALER, namaDealer);
                viewBooking.put(Config.DISP_DATE, date);
                viewBooking.put(Config.DISP_TIME, time);
                viewBooking.put(Config.DISP_REMARKS, remarks);
                list.add(viewBooking);
            }
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }

        ListAdapter adapter = new SimpleAdapter(
                getActivity(), list, R.layout.list_item_booking,
                new String[]{Config.DISP_NOMOR, Config.DISP_NAMA_DEALER, Config.DISP_DATE,
                        Config.DISP_TIME, Config.DISP_REMARKS},
                new int[]{R.id.nomor, R.id.namaDealer, R.id.date,
                        R.id.time, R.id.remarks});
        listView.setAdapter(adapter);
    }
*/


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Config.CEK_KONEKSI(getActivity())) {

            String kodeBooking           = listBookingProgress.get(position).getIdBooking();
            String kodeDealer           = listBookingProgress.get(position).getIdDealer();

            //HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
            //String kodeBooking = map.get(Config.DISP_KD_BOOKING).toString();
            //String kodeDealer = map.get(Config.DISP_KD_DEALER).toString();

            Intent intent = new Intent(getActivity(), ListBookingDetail.class);
            intent.putExtra(Config.DISP_KD_BOOKING, kodeBooking);
            intent.putExtra(Config.DISP_KD_DEALER, kodeDealer);
            intent.putExtra(Config.DISP_STATUS_BOOKING, status_booking);
            startActivity(intent);
            //Toast.makeText(getActivity(), kodeDealer, Toast.LENGTH_LONG).show();
        }else{
            //showDialog(tampil_error);
            Toast.makeText(getActivity(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }
}
