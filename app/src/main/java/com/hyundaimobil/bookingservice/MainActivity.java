package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hyundaimobil.bookingservice.adapter.NewsAdapter;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.app.Config;
import com.hyundaimobil.bookingservice.app.ForceCloseDebugger;
import com.hyundaimobil.bookingservice.app.RequestHandler;
import com.hyundaimobil.bookingservice.app.SessionManager;
import com.hyundaimobil.bookingservice.data.NewsData;
import com.hyundaimobil.bookingservice.db.DatabaseHelper;
import com.hyundaimobil.bookingservice.helper.BottomNavigationViewHelper;
import com.hyundaimobil.bookingservice.helper.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements
        BaseSliderView.OnSliderClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        ViewPagerEx.OnPageChangeListener{

    NavigationView navigation_view;
    private DrawerLayout drawerLayout;
    TextView name, versiApp;
    SessionManager session;
    String fullname, company_code, usercode, nip, branch_code, dept_code, access_level,
            salesman_code, supervisor_code, dealer_code, login_number, kd_customer, chasis, userco;

    SliderLayout sliderLayout;
    HashMap<String, String> HashMapForURL;
    String JSON_STRING, tombol_link_update, versi_link_update, message_link_update, status_aktif_akun;

    ListView list;
    SwipeRefreshLayout swipe;
    List<NewsData> newsList = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    private int offSet = 0;
    int no;
    NewsAdapter adapter;
    Handler handler;
    Runnable runnable;

    BottomNavigationView bottomNavigationView;
    ProgressDialog loading;

    protected Cursor cursor;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    String brand, nama_device, model, id_model, nama_produk, versi_api_sdk, os;

    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.item_toolbar);
        toolbar.setLogo(R.mipmap.hyundai_h);
        toolbar.setLogoDescription(R.string.app_name);

        ForceCloseDebugger.handle(this);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        /*
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        */

        session = new SessionManager(getApplicationContext());
        String statusLogin = String.valueOf(session.isLoggedIn());


        drawerLayout = findViewById(R.id.drawer);
        navigation_view = findViewById(R.id.navigation_view);

        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, navigation_view, false);
        navigation_view.addHeaderView(headerView);
        navigation_view.inflateMenu(R.menu.item_navigasi);





        databaseHelper = new DatabaseHelper(this);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final String tgl = year + "-" + (++month) + "-" + day;
        final String status = "4";      //4. untuk inggris, 5. untuk indonesia
        final String status_app = "2";  //2. untuk bahasa

        db = databaseHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABEL_STATUS_KLIK + " WHERE " +
                DatabaseHelper.STATUS_APP + "='" + status_app + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            db = databaseHelper.getWritableDatabase();

            db.execSQL("INSERT INTO " + DatabaseHelper.TABEL_STATUS_KLIK + "(" +
                    DatabaseHelper.TANGGAL + ", " + DatabaseHelper.STATUS + ", " + DatabaseHelper.STATUS_APP + ") values('" +
                    tgl + "','" +
                    status + "','" +
                    status_app + "')");
            Log.d("Data", "onCreate: " + db);

            //Toast.makeText(getApplicationContext(), "Baru input", Toast.LENGTH_LONG).show();
        }

        String a = cursor.getString(2);
        /*
        if (a.equals("4")) {
            //Toast.makeText(getApplicationContext(), "Bahasa Indonesia", Toast.LENGTH_LONG).show();
            navigation_view.getMenu().findItem(R.id.language_in).setVisible(true);
            navigation_view.getMenu().findItem(R.id.language_en).setVisible(false);
        } else {
            //Toast.makeText(getApplicationContext(), "Bahasa Inggris", Toast.LENGTH_LONG).show();
            navigation_view.getMenu().findItem(R.id.language_in).setVisible(false);
            navigation_view.getMenu().findItem(R.id.language_en).setVisible(true);
        }*/


        HashMap<String, String> user = session.getUserDetails();
        fullname = user.get(SessionManager.KEY_FULLNAME);
        usercode = user.get(SessionManager.KEY_USERCODE);
        nip = user.get(SessionManager.KEY_NIP);
        branch_code = user.get(SessionManager.KEY_BRANCH_CODE);
        dept_code = user.get(SessionManager.KEY_DEPT_CODE);
        company_code = user.get(SessionManager.KEY_COMPANY_CODE);
        access_level = user.get(SessionManager.KEY_ACCESS_LEVEL);
        salesman_code = user.get(SessionManager.KEY_SALESMAN_CODE);
        supervisor_code = user.get(SessionManager.KEY_SUPERVISOR_CODE);
        dealer_code = user.get(SessionManager.KEY_DEALER_CODE);
        login_number = user.get(SessionManager.KEY_LOGIN_NUMBER);
        kd_customer = user.get(SessionManager.KEY_KD_CUSTOMER);
        chasis = user.get(SessionManager.KEY_CHASIS);

        versiApp = findViewById(R.id.versiApp);
        versiApp.setText("Version: " + Config.VALUE_VERSI);

        if (statusLogin.equals("false")) {
            userco = "";
            navigation_view.getMenu().findItem(R.id.account).setVisible(false);
            navigation_view.getMenu().findItem(R.id.settingPassword).setVisible(false);
            navigation_view.getMenu().findItem(R.id.logout).setVisible(false);
            navigation_view.getMenu().findItem(R.id.login).setVisible(true);
        } else {
            userco = usercode;
            navigation_view.getMenu().findItem(R.id.account).setVisible(true);
            navigation_view.getMenu().findItem(R.id.settingPassword).setVisible(true);
            navigation_view.getMenu().findItem(R.id.logout).setVisible(true);
            navigation_view.getMenu().findItem(R.id.login).setVisible(false);
        }
        name = headerView.findViewById(R.id.name);
        name.setText(fullname);

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()) {
                    case R.id.account:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {
                                if (status_aktif_akun.equals("1")) {
                                    Intent account = new Intent(MainActivity.this, Account.class);
                                    startActivity(account);
                                } else {
                                    status_nonaktif_popup();
                                }
                            } else {
                                update_popup();
                            }
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.inbox:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            Intent link = new Intent(MainActivity.this, Inbox.class);
                            startActivity(link);
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.unit:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {
                                if (status_aktif_akun.equals("1")) {
                                    Intent unit = new Intent(MainActivity.this, Unit.class);
                                    startActivity(unit);
                                } else {
                                    status_nonaktif_popup();
                                }
                            } else {
                                update_popup();
                            }
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.settingPassword:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {
                                if (status_aktif_akun.equals("1")) {
                                    Intent settingPassword = new Intent(MainActivity.this, SettingPassword.class);
                                    startActivity(settingPassword);
                                } else {
                                    status_nonaktif_popup();
                                }
                            } else {
                                update_popup();
                            }
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.about:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            Intent about = new Intent(MainActivity.this, About.class);
                            startActivity(about);
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.logout:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            logout();
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.login:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {
                                Intent login = new Intent(MainActivity.this, Login.class);
                                startActivity(login);
                            } else {
                                update_popup();
                            }
                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;
/*
                    case R.id.language_en:
                        //to english
                        loading_sebentar();

                        Paper.book().write("language", "en");
                        updateView((String) Paper.book().read("language"));
                        navigation_view.getMenu().findItem(R.id.language_in).setVisible(true);
                        navigation_view.getMenu().findItem(R.id.language_en).setVisible(false);

                        cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABEL_STATUS_KLIK + " WHERE " +
                                DatabaseHelper.STATUS_APP + "='" + status_app + "'", null);
                        cursor.moveToFirst();

                        db = databaseHelper.getWritableDatabase();

                        db.execSQL("UPDATE " + DatabaseHelper.TABEL_STATUS_KLIK + " SET " +
                                DatabaseHelper.STATUS + "=" + status + " WHERE " +
                                DatabaseHelper.STATUS_APP + "=" + status_app);

                        Log.d("Data", "onCreate: " + db);

                        return true;
                    case R.id.language_in:
                        //to indonesia
                        loading_sebentar();

                        Paper.book().write("language", "id");
                        updateView((String) Paper.book().read("language"));
                        navigation_view.getMenu().findItem(R.id.language_in).setVisible(false);
                        navigation_view.getMenu().findItem(R.id.language_en).setVisible(true);

                        final String status = "5";      //4. untuk inggris, 5. untuk indonesia
                        cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABEL_STATUS_KLIK + " WHERE " +
                                DatabaseHelper.STATUS_APP + "='" + status_app + "'", null);
                        cursor.moveToFirst();

                        db = databaseHelper.getWritableDatabase();

                        db.execSQL("UPDATE " + DatabaseHelper.TABEL_STATUS_KLIK + " SET " +
                                DatabaseHelper.STATUS + "=" + status + " WHERE " +
                                DatabaseHelper.STATUS_APP + "=" + status_app);
                        Log.d("Data", "onCreate: " + db);

                        return true;
*/
                    default:
                        Toast.makeText(MainActivity.this, Config.NOTIF_CONTACT_ADMIN, Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });


        sliderLayout = findViewById(R.id.slider);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(MainActivity.this);

        swipe = findViewById(R.id.swipe_refresh_layout);
        list = findViewById(R.id.list_news);
        newsList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Config.CEK_KONEKSI(MainActivity.this)) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(MainActivity.this, DetailNews.class);
                    intent.putExtra(Config.TAG_ID, newsList.get(position).getId());
                    startActivity(intent);
                } else {
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });

        adapter = new NewsAdapter(MainActivity.this, newsList);
        list.setAdapter(adapter);
        swipe.setColorSchemeResources(R.color.colorAccent);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(MainActivity.this)) {
                    newsList.clear();
                    adapter.notifyDataSetChanged();
                    offSet = 0;

                    callNews(offSet);
                    getJSON_showImg();

                    getJSON_update();

                    swipe.setRefreshing(false);
                } else {
                    swipe.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                }
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    final ProgressDialog loading2 = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            if (Config.CEK_KONEKSI(MainActivity.this)) {
                                callNews(offSet);
                                loading2.dismiss();
                            } else {
                                loading2.dismiss();
                                swipe.setRefreshing(false);
                                showDialog(Config.TAMPIL_ERROR);
                            }

                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }

        });


        bottomNavigationView = findViewById(R.id.btn_nav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.booking_service:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {

                                String statusLogin = String.valueOf(session.isLoggedIn());
                                if (statusLogin.equals("false")) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);

                                } else {
                                    if (status_aktif_akun.equals("1")) {
                                        Intent intent = new Intent(MainActivity.this, FormBooking.class);
                                        startActivity(intent);
                                    } else {
                                        status_nonaktif_popup();
                                    }
                                }

                            } else {
                                //Toast.makeText(MainActivity.this, Config.UPDATE_AVAILABLE, Toast.LENGTH_SHORT).show();
                                update_popup();
                            }

                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        break;
                    case R.id.service_history:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {

                                String statusLogin = String.valueOf(session.isLoggedIn());
                                if (statusLogin.equals("false")) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                } else {
                                    if (status_aktif_akun.equals("1")) {
                                        Intent intent = new Intent(MainActivity.this, PilihUnit.class);
                                        intent.putExtra(Config.TAG_ID, "3");
                                        startActivity(intent);

                                    } else {
                                        status_nonaktif_popup();
                                    }
                                }

                            } else {
                                //Toast.makeText(MainActivity.this, Config.UPDATE_AVAILABLE, Toast.LENGTH_SHORT).show();
                                update_popup();
                            }

                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        break;
                    case R.id.emergency:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {

                                String statusLogin = String.valueOf(session.isLoggedIn());
                                if (statusLogin.equals("false")) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                } else {
                                    if (status_aktif_akun.equals("1")) {
                                        Intent intent = new Intent(MainActivity.this, Emergency.class);
                                        startActivity(intent);
                                    } else {
                                        status_nonaktif_popup();
                                    }
                                }

                            } else {
                                //Toast.makeText(MainActivity.this, Config.UPDATE_AVAILABLE, Toast.LENGTH_SHORT).show();
                                update_popup();
                            }

                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        break;
                    case R.id.test_drive:
                        if (Config.CEK_KONEKSI(MainActivity.this)) {
                            if (versi_link_update.equals(Config.VALUE_VERSI)) {
                                String statusLogin = String.valueOf(session.isLoggedIn());
                                if (statusLogin.equals("true")) {
                                    if (status_aktif_akun.equals("1")) {

                                        Intent intent = new Intent(MainActivity.this, FormTestDrive.class);
                                        startActivity(intent);

                                    } else {
                                        status_nonaktif_popup();
                                    }
                                } else {
                                    Intent intent = new Intent(MainActivity.this, FormTestDrive.class);
                                    startActivity(intent);
                                }
                            } else {
                                //Toast.makeText(MainActivity.this, Config.UPDATE_AVAILABLE, Toast.LENGTH_SHORT).show();
                                update_popup();
                            }

                        } else {
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        break;
                }

                return true;
            }
        });
        bottomNavigationView.setItemIconTintList(null);

        //if (!checkPermission(wantPermission)) {
            //requestPermission(wantPermission);
        //} else {
           // Log.d(TAG, "Phone number: " + getPhone());

       // }

        //Toast.makeText(MainActivity.this, getPhone(), Toast.LENGTH_LONG).show();

        //TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //hp      = tMgr.getLine1Number();
        //simID   = tMgr.getSimSerialNumber();
        //imei    = tMgr.getDeviceId();
        //Toast.makeText(MainActivity.this, tMgr.getLine1Number(), Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this, tMgr.getSimSerialNumber(), Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this, tMgr.getDeviceId(), Toast.LENGTH_LONG).show();

        brand          = Build.BRAND;
        nama_device    = Build.DEVICE;
        model          = Build.MODEL;
        id_model       = Build.ID;
        nama_produk    = Build.PRODUCT;
        versi_api_sdk  = Build.VERSION.SDK;
        os             = Build.VERSION.RELEASE;


        //for language
        Paper.init(this);

        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language", "en");
        updateView((String) Paper.book().read("language"));


    }














    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(MainActivity.this)) {
            sliderLayout.removeAllSliders();
            newsList.clear();
            adapter.notifyDataSetChanged();
            offSet = 0;
            //loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);

            callNews(offSet);
            getJSON_showImg();

            getJSON_update();

            swipe.setRefreshing(false);
        } else {
            swipe.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void callNews(int page) {
        final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        //swipe.setRefreshing(true);

        // Creating volley request obj
        //JsonArrayRequest arrReq = new JsonArrayRequest(Config.URL_ALL_NEWS + "/" + page,
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_ALL_NEWS + "/" + page,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);

                        if (response.length() > 0) {
                            // Parsing json
                            //for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    NewsData news = new NewsData();

                                    no = obj.getInt(Config.TAG_NO);
                                    news.setId(obj.getString(Config.TAG_ID));
                                    news.setJudul(obj.getString(Config.TAG_JUDUL));

                                    if (!Objects.equals(obj.getString(Config.TAG_GAMBAR), "")) {
                                        news.setGambar(obj.getString(Config.TAG_GAMBAR));
                                    }

                                    news.setDatetime(obj.getString(Config.TAG_TGL));
                                    news.setIsi(obj.getString(Config.TAG_ISI));

                                    // adding news to news array
                                    newsList.add(news);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.e(TAG, "offSet " + offSet + " - " + no);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                            //}
                        }
                        swipe.setRefreshing(false);
                        loading.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                loading.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if (Config.CEK_KONEKSI(MainActivity.this)) {
            String kdPromosi = slider.getBundle().get("idPromosi") + "";
            Intent intent = new Intent(MainActivity.this, PromotionsDetail.class);
            intent.putExtra(Config.TAG_ALL_KD_PROMOTIONS, kdPromosi);
            startActivity(intent);
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void getJSON_showImg() {
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                showDataImg();
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //params.put(Config.KEY_KD_COY, coy);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_ALL_PROMOTIONS, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataImg() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String kd_img = jo.getString(Config.TAG_ALL_KD_PROMOTIONS);
                String nama_img = jo.getString(Config.TAG_ALL_NAMA_PROMOTIONS);
                String link_img = jo.getString(Config.TAG_ALL_PROMOTIONS);

                //String a = nama_img.substring(0, 1);
                HashMapForURL = new HashMap<>();
                HashMapForURL.put(nama_img, link_img);

                TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                textSliderView
                        .description(nama_img)
                        .image(HashMapForURL.get(nama_img))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", nama_img);
                textSliderView.getBundle().putString("idPromosi", kd_img);
                sliderLayout.addSlider(textSliderView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //loading.dismiss();
            Toast.makeText(MainActivity.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }

    }

    private void getJSON_showImg2() {
        final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        // Creating volley request obj
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_PROMOTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);

                        if (response.length() > 0) {
                            // Parsing json
                            //for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String kd_img = obj.getString(Config.TAG_ALL_KD_PROMOTIONS);
                                    String nama_img = obj.getString(Config.TAG_ALL_NAMA_PROMOTIONS);
                                    String link_img = obj.getString(Config.TAG_ALL_PROMOTIONS);

                                    HashMapForURL = new HashMap<>();
                                    HashMapForURL.put(nama_img, link_img);

                                    TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                                    textSliderView
                                            .description(nama_img)
                                            .image(HashMapForURL.get(nama_img))
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(MainActivity.this);
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("extra", nama_img);
                                    textSliderView.getBundle().putString("idPromosi", kd_img);
                                    sliderLayout.addSlider(textSliderView);

                                    Log.e(TAG, "offSet " + offSet + " - " + no);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                        swipe.setRefreshing(false);
                        loading.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                loading.dismiss();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                String creds = String.format("%s:%s",Config.usernameHttps, Config.passwordHttps);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }




    //controll memanggil item toolbar untuk menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.item_toolbar, menu);
        return true;
    }

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.list_toolbar:
                drawerLayout.openDrawer(Gravity.END);
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

    //Logout function
    private void logout() {

        @SuppressLint("StaticFieldLeak")
        class logout extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                session.logoutUser();
                finish();
                //Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from session
                params.put(SessionManager.KEY_USERCODE, usercode);
                params.put(SessionManager.KEY_LOGIN_NUMBER, login_number);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_LOGOUT, params);
            }
        }

        //Creating an alert dialog to confirm logout
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(Config.NOTIF_LOGOUT);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                logout log = new logout();
                log.execute();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Welcome " + fullname, Toast.LENGTH_SHORT).show();
            }
        });

        //Showing the alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //update popup function
    private void update_popup() {
        //Creating an alert dialog to confirm
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        //alertDialogBuilder.setTitle(Config.UPDATE_AVAILABLE);
        alertDialogBuilder.setMessage(message_link_update);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tombol_link_update));
                startActivity(browserIntent);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        //Showing the alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void status_nonaktif_popup() {
        //Creating an alert dialog to confirm
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sorry, your account is inactive");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        //Showing the alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(Config.NOTIF_EXIT);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //onBackPressed();
                //finish();
                //MainActivity.this.finish();
                //onDestroy();
                moveTaskToBack(true);
                //onStop();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        //Showing the alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    //for language apps
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @SuppressLint("SetTextI18n")
    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();

        navigation_view.getMenu().findItem(R.id.account).setTitle(resources.getString(R.string.account));
        navigation_view.getMenu().findItem(R.id.inbox).setTitle(resources.getString(R.string.inbox));
        navigation_view.getMenu().findItem(R.id.settingPassword).setTitle(resources.getString(R.string.settingPassword));
        navigation_view.getMenu().findItem(R.id.about).setTitle(resources.getString(R.string.about));
        navigation_view.getMenu().findItem(R.id.logout).setTitle(resources.getString(R.string.logout));
        navigation_view.getMenu().findItem(R.id.login).setTitle(resources.getString(R.string.login));

        versiApp.setText(resources.getString(R.string.versiApp) + " " + Config.VALUE_VERSI);
    }

    private void loading_sebentar() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        //progressDialog.setTitle(Config.ALERT_LOADING);
        progressDialog.setMessage(Config.ALERT_PLEASE_WAIT);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }






    private void getJSON_update() {
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                showDataUpdate();
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_VERSI, Config.VALUE_VERSI);
                params.put(SessionManager.KEY_USERCODE, userco);
                params.put(Config.KEY_FLAG_APPS, Config.VALUE_FLAG_APPS);

                //params.put(Config.KEY_HP, "");
                //params.put(Config.KEY_SIM_ID, "");
                //params.put(Config.KEY_IMEI, "");
                //params.put(Config.MAPS_LAT, String.valueOf(latitude));
                //params.put(Config.MAPS_LNG, String.valueOf(longtitude));
                params.put(Config.KEY_BRAND, brand);
                params.put(Config.KEY_DEVICE, nama_device);
                params.put(Config.KEY_MODEL, model);
                params.put(Config.KEY_ID_MODEL, id_model);
                params.put(Config.KEY_NAMA_PRODUK, nama_produk);
                params.put(Config.KEY_API_SDK, versi_api_sdk);
                params.put(Config.KEY_OS, os);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_UPDATE, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataUpdate() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String link_update = jo.getString(Config.TAG_LINK_UPDATE);
                String versi_update = jo.getString(Config.TAG_VERSI_UPDATE);
                String message_update = jo.getString(Config.TAG_MESSAGE_UPDATE);
                String status_aktif = jo.getString(Config.KEY_STATUS_AKTIF);

                tombol_link_update = link_update;
                versi_link_update = versi_update;
                message_link_update = message_update;
                status_aktif_akun = status_aktif;

                if (!Objects.equals(versi_link_update, Config.VALUE_VERSI)) {
                    update_popup();
                }

                if (status_aktif_akun.equals("0")) {
                    status_nonaktif_popup();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }

    }

    private void getJSON_update2() {

        /*byte[] data_value_versi     = Config.VALUE_VERSI.getBytes(StandardCharsets.UTF_8);
        byte[] data_flag_apps       = Config.VALUE_FLAG_APPS.getBytes(StandardCharsets.UTF_8);
        byte[] data_userco          = userco.getBytes(StandardCharsets.UTF_8);
        byte[] data_brand           = brand.getBytes(StandardCharsets.UTF_8);
        byte[] data_nama_device     = nama_device.getBytes(StandardCharsets.UTF_8);
        byte[] data_model           = model.getBytes(StandardCharsets.UTF_8);
        byte[] data_id_model        = id_model.getBytes(StandardCharsets.UTF_8);
        byte[] data_nama_produk     = nama_produk.getBytes(StandardCharsets.UTF_8);
        byte[] data_versi_api_sdk   = versi_api_sdk.getBytes(StandardCharsets.UTF_8);
        byte[] data_os              = os.getBytes(StandardCharsets.UTF_8);

        String value_versi          = Base64.encodeToString(data_value_versi, Base64.NO_WRAP);
        String flag_apps            = Base64.encodeToString(data_flag_apps, Base64.NO_WRAP);
        String userco               = Base64.encodeToString(data_userco, Base64.NO_WRAP);
        String brand                = Base64.encodeToString(data_brand, Base64.NO_WRAP);
        String nama_device          = Base64.encodeToString(data_nama_device, Base64.NO_WRAP);
        String model                = Base64.encodeToString(data_model, Base64.NO_WRAP);
        String id_model             = Base64.encodeToString(data_id_model, Base64.NO_WRAP);
        String nama_produk          = Base64.encodeToString(data_nama_produk, Base64.NO_WRAP);
        String versi_api_sdk        = Base64.encodeToString(data_versi_api_sdk, Base64.NO_WRAP);
        String os                   = Base64.encodeToString(data_os, Base64.NO_WRAP);*/

        final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        // Creating volley request obj
        //Toast.makeText(MainActivity.this, Config.URL_GET_UPDATE+"/"+value_versi+"/"+ userco +"/"+ flag_apps+"/"+ brand+"/"+nama_device+"/"+model+"/"+id_model+"/"+nama_produk+"/"+versi_api_sdk+"/"+os, Toast.LENGTH_SHORT).show();
        //StringRequest arrReq = new StringRequest(Request.Method.GET, Config.URL_GET_UPDATE+"/"+value_versi+"/"+userco+"/"+flag_apps
        //+"/"+ brand+"/"+nama_device+"/"+model+"/"+id_model+"/"+nama_produk+"/"+versi_api_sdk+"/"+os,
        StringRequest arrReq = new StringRequest(Request.Method.GET, Config.URL_GET_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);

                        if (response.length() > 0) {
                            // Parsing json
                            //for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String link_update = obj.getString(Config.TAG_LINK_UPDATE);
                                    String versi_update = obj.getString(Config.TAG_VERSI_UPDATE);
                                    String message_update = obj.getString(Config.TAG_MESSAGE_UPDATE);
                                    String status_aktif = obj.getString(Config.KEY_STATUS_AKTIF);

                                    tombol_link_update = link_update;
                                    versi_link_update = versi_update;
                                    message_link_update = message_update;
                                    status_aktif_akun = status_aktif;

                                    if (!Objects.equals(versi_link_update, Config.VALUE_VERSI)) {
                                        update_popup();
                                    }

                                    if (status_aktif_akun.equals("0")) {
                                        status_nonaktif_popup();
                                    }

                                    Log.e(TAG, "offSet " + offSet + " - " + no + " | " + versi_link_update);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                        swipe.setRefreshing(false);
                        loading.dismiss();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                        VolleyLog.e(TAG, "Error: " + error.getMessage());
                        swipe.setRefreshing(false);
                        loading.dismiss();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                params.put(Config.KEY_VERSI, Config.VALUE_VERSI);
                params.put(SessionManager.KEY_USERCODE, userco);
                params.put(Config.KEY_FLAG_APPS, Config.VALUE_FLAG_APPS);

                //params.put(Config.KEY_HP, "");
                //params.put(Config.KEY_SIM_ID, "");
                //params.put(Config.KEY_IMEI, "");
                //params.put(Config.MAPS_LAT, String.valueOf(latitude));
                //params.put(Config.MAPS_LNG, String.valueOf(longtitude));
                params.put(Config.KEY_BRAND, brand);
                params.put(Config.KEY_DEVICE, nama_device);
                params.put(Config.KEY_MODEL, model);
                params.put(Config.KEY_ID_MODEL, id_model);
                params.put(Config.KEY_NAMA_PRODUK, nama_produk);
                params.put(Config.KEY_API_SDK, versi_api_sdk);
                params.put(Config.KEY_OS, os);

                return params;
            }
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                String creds = String.format("%s:%s",Config.usernameHttps, Config.passwordHttps);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;

            }*/
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }


}
