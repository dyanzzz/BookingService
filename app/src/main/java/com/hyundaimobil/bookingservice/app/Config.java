package com.hyundaimobil.bookingservice.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by User HMI on 5/12/2017.
 */

@SuppressLint("Registered")
@SuppressWarnings("JniMissingFunction")
public class Config extends AppCompatActivity {

    public static native String server();
    public static native String usernameHttps();
    public static native String passwordHttps();
    public static native String login();
    public static native String logout();
    public static native String changePassword();
    public static native String chekcPaswd();

    public static native String searchDealerSvc();
    public static native String searchDealer();
    public static native String addBookingNow();
    public static native String getAllPromotions();
    public static native String getSelectedPromotions();
    public static native String getAccount();
    public static native String getAllBooking();
    public static native String getBookingDetail();
    public static native String searchDealerDetail();
    public static native String cancelBooking();
    public static native String getAllNews();
    public static native String getDetailNews();
    public static native String getServiceHistory();
    public static native String getServiceHistoryDetail();
    public static native String addRegisterTestDrive();
    public static native String sendMailEmergency();
    public static native String getAllTipeKendaraan();
    public static native String updateApps();
    public static native String registerAccount();
    public static native String getAllMaintenanceService();
    public static native String getChasisRegister();
    public static native String pilihUnit();
    public static native String unitDetail();
    public static native String forgotPassword();
    public static native String getDateLibur();
    public static native String getAllInbox();
    public static native String getInboxDetail();

    static {
        System.loadLibrary("ndkbookingservice");
    }

    //JSON
    public static final String
            TAG_JSON_ARRAY   = "result",
            TAG_JSON_OBJ = "json_obj_req";

    public static final int
            TAMPIL_ERROR = 1,
            CHOSE_DEALER = 2,
            CHOSE_KENDARAAN = 3,
            CHOSE_MAINTENANCE = 4,
            CHOSE_CHASIS = 5,
            CHOSE_UNIT = 6;

    public static final int
            REQUEST_CHECK_SETTINGS_GPS=0x1,
            REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    //server
    public static String Server                 = server();
    public static String usernameHttps          = usernameHttps();
    public static String passwordHttps          = passwordHttps();

    //url salesman activity
    public static final String LOGIN_URL                    = Server + login();
    public static final String URL_LOGOUT                   = Server + logout();
    public static final String URL_CHANGE_PASSWORD          = Server + changePassword();
    public static final String URL_CEK_PASSWORD_LAMA        = Server + chekcPaswd();

    public static final String URL_GET_ALL_DEALER_SVC       = Server + searchDealerSvc();
    public static final String URL_GET_ALL_DEALER           = Server + searchDealer();
    public static final String URL_ADD_BOOKING_NOW          = Server + addBookingNow();
    public static final String URL_GET_ALL_PROMOTIONS       = Server + getAllPromotions();
    public static final String URL_GET_SELECTED_PROMOTIONS  = Server + getSelectedPromotions();
    public static final String URL_GET_ACCOUNT              = Server + getAccount();
    public static final String URL_GET_ALL_BOOKING          = Server + getAllBooking();
    public static final String URL_GET_BOOKING_DETAIL       = Server + getBookingDetail();
    public static final String URL_GET_DEALER_DETAIL        = Server + searchDealerDetail();
    public static final String URL_CANCEL_BOOKING           = Server + cancelBooking();
    public static final String URL_ALL_NEWS                 = Server + getAllNews();
    public static final String URL_DETAIL_NEWS              = Server + getDetailNews();
    public static final String URL_GET_SERVICE_HISTORY      = Server + getServiceHistory();
    public static final String URL_GET_SERVICE_HISTORY_DET  = Server + getServiceHistoryDetail();
    public static final String URL_ADD_TEST_DRIVE           = Server + addRegisterTestDrive();
    public static final String URL_SEND_MAIL_EMERGENCY      = Server + sendMailEmergency();
    public static final String URL_GET_ALL_TIPE_KENDARAAN   = Server + getAllTipeKendaraan();
    public static final String URL_GET_UPDATE               = Server + updateApps();
    public static final String URL_ADD_REGISTER             = Server + registerAccount();
    public static final String URL_ALL_MAINTENANCE_SERVICE  = Server + getAllMaintenanceService();
    public static final String URL_GET_CHASIS_REGISTER      = Server + getChasisRegister();
    public static final String URL_PILIH_UNIT               = Server + pilihUnit();
    public static final String URL_GET_UNIT_DETAIL          = Server + unitDetail();
    public static final String URL_FORGOT_PASSWORD          = Server + forgotPassword();
    public static final String URL_GET_DATE_LIBUR           = Server + getDateLibur();
    public static final String URL_GET_ALL_INBOX            = Server + getAllInbox();
    public static final String URL_GET_INBOX_DETAIL         = Server + getInboxDetail();

    public static final String KEY_USERNAME                 = "username";
    public static final String KEY_PASSWORD                 = "password";
    public static final String KEY_FLAG_APPS                = "flag_apps";
    public static final String KEY_IP                       = "ip";
    public static final String KEY_VERSI                    = "versi";
    public static final String VALUE_FLAG_APPS              = "3";
    public static final String VALUE_VERSI                  = "1.0.21";
    public static final String UPDATE_AVAILABLE             = "UPDATE AVAILABLE";
    public static final String UPDATE_MESSAGE               = "Do you want to update now?";
    public static final String KEY_SEARCH                   = "search";
    public static final String KEY_STATUS_AKTIF             = "status_aktif";
    public static final String KEY_TOKEN                    = "token";

    //validasi allert
    public static final String ALERT_USERNAME               = "* Please Input Username";
    public static final String ALERT_PASSWORD               = "* Please Input Password";
    public static final String ALERT_PASSWORD_LAMA          = "* Please Input Current Password";
    public static final String ALERT_PASSWORD_BARU          = "* Please Input New Password and must be more than 3 characters in length";
    public static final String ALERT_PASSWORD_BARU2         = "* Please Input Re-Type New Password and must be more than 3 characters in length";
    public static final String ALERT_PASSWORD_BARU_SAMA     = "* Password must match";
    public static final String ALERT_TITLE_CONN_ERROR       = "Connection Error";
    public static final String ALERT_MESSAGE_CONN_ERROR     = "Unable to connect with the server. Check your internet connection and try again.";
    public static final String ALERT_OK_BUTTON              = "Ok";
    public static final String ALERT_RETRY_BUTTON           = "Retry";
    public static final String ALERT_EXIT_BUTTON            = "Exit";
    public static final String ALERT_NOT_FOUND              = "Data Not Found";
    public static final String ALERT_LOADING                = "Loading";
    public static final String ALERT_PLEASE_WAIT            = "Please Wait";
    public static final String ALERT_FAILED_TEST_DRIVE      = "Register Test Drive Failed and check connection";
    public static final String ALERT_FAILED_BOOKING_SERVICE = "Register Booking Service Failed and check connection";
    public static final String ALERT_FAILED_BOOKING_SERVICE_COUNTER = "Register Booking Service Failed and code counter not found";
    public static final String ALERT_INVALID_EMAIL_FORMAT   = "Invalid Email Format";
    public static final String ALERT_DATE_WRONG             = "Date on your phone is invalid";



    public static final String ALERT_CHASIS                 = "* Please Input Chasis";
    public static final String ALERT_NAME                   = "* Please Input Name";
    public static final String ALERT_PHONE                  = "* Please Input Phone";
    public static final String ALERT_EMAIL                  = "* Please Input Email";
    public static final String ALERT_NOTE                   = "* Please Input Note";
    public static final String ALERT_DATE                   = "* Please Input Date";
    public static final String ALERT_TIME                   = "* Please Input Time";
    public static final String ALERT_DEALER                 = "* Please Input Dealer";
    public static final String ALERT_UNIT                   = "* Please Input Unit";
    public static final String ALERT_REMARKS                = "* Please Input Remarks";
    public static final String ALERT_MAINTENANCE_SERVICE    = "* Please Input Maintenance Service";
    public static final String ALERT_VEHICLE                = "* Please Input Type of Vehicle";
    public static final String ALERT_EMAIL_NOT_REGISTER     = "Email not register";
    public static final String ALERT_ONLY_VIEW              = "Not Available for choose this dealer";


    public static final String NOTIF_LOGOUT                 = "Are you sure want to Logout?";
    public static final String NOTIF_EXIT                   = "Are you sure want to Exit?";
    public static final String NOTIF_UMUM                   = "Are you sure?";
    public static final String NOTIF_CONTACT_ADMIN          = "Please Contact Administrator.";
    public static final String ACCOUNT_ALREADY_REGISTER     = "Account already registered";
    public static final String HARUS_DIISI                  = "Form Input Cannot be Empty!!!";
    public static final String DISP_NOMOR                   = "nomor";

    //untuk unit
    public static final String DISP_UNIT_CHASIS             = "chasis";
    public static final String DISP_UNIT_ENGINE             = "engine";
    public static final String DISP_UNIT_POLICE             = "nopol";
    public static final String DISP_UNIT_CUSTOMER           = "customer";
    public static final String DISP_UNIT_TYPE               = "type";
    public static final String DISP_UNIT_MODEL              = "model";
    public static final String DISP_UNIT_COLOUR             = "colour";
    public static final String DISP_UNIT_KDTYPE             = "kdtype";
    public static final String DISP_UNIT_KDCOLOUR           = "kdcolour";

    //untuk customer
    public static final String DISP_CUST_NAME               = "cust_name";
    public static final String DISP_CUST_ADR1               = "cust_adr1";
    public static final String DISP_CUST_ADR2               = "cust_adr2";
    public static final String DISP_CUST_KOTA               = "cust_nmkt";
    public static final String DISP_CUST_PHONE1             = "cust_tlp1";
    public static final String DISP_CUST_PHONE2             = "cust_tlp2";
    public static final String DISP_CUST_MOBILE1            = "cust_hp1";
    public static final String DISP_CUST_MOBILE2            = "cust_hp2";
    public static final String DISP_CUST_EMAIL              = "cust_email";

    //untuk display list booking
    public static final String DISP_KD_BOOKING              = "kode_booking";
    public static final String DISP_DATE                    = "date";
    public static final String DISP_TIME                    = "time";
    public static final String DISP_REMARKS                 = "remarks";
    public static final String DISP_STATUS_BOOKING          = "status_booking";

    public static final String KEY_KD_COY                   = "wobckdcoy";
    public static final String KEY_KD_CAB                   = "wobckdcab";
    public static final String KEY_KD_CUSTOMER              = "wobckdcust";
    public static final String KEY_CHASIS                   = "wobcrangka";
    public static final String KEY_STATUS_BOOKING           = "status_booking";

    //untuk promotions
    public static final String TAG_ALL_KD_PROMOTIONS        = "kd_promotions";
    public static final String TAG_ALL_PROMOTIONS           = "image_promotions";
    public static final String TAG_ALL_NAMA_PROMOTIONS      = "nama_promotions";
    public static final String TAG_ALL_DESKRIPSI_PROMOTIONS = "deskripsi_promotions";
    public static final String TAG_ALL_LINK_PROMOTIONS      = "link_promotions";

    //save to database
    public static final String KEY_NAME                     = "name";
    public static final String KEY_PHONE                    = "phone";
    public static final String KEY_EMAIL                    = "email";
    public static final String KEY_NOTE                     = "note";
    public static final String KEY_DATE_BOOKING             = "date_booking";
    public static final String KEY_TIME_BOOKING             = "time_booking";
    public static final String KEY_DEALER_DESTINATION       = "kd_dealer_destination";
    public static final String KEY_COY_DEALER_DESTINATION   = "coy_dealer_destination";
    public static final String KEY_KD_VEHICLE               = "kd_vehicle";
    public static final String KEY_REMARKS                  = "remarks";
    public static final String KEY_MAINTENANCE_SERVICE      = "maintenance_service";
    public static final String KEY_TANGGAL_HARI_INI         = "hari_ini";
    public static final String KEY_BOOKING                  = "key_booking";

    public static final String KEY_ENGINE                   = "engine";
    public static final String KEY_NOPOL                    = "nopol";
    public static final String KEY_TYPE                     = "type";
    public static final String KEY_COLOR                    = "color";

    public static final String KEY_HP                       = "hp";
    public static final String KEY_SIM_ID                   = "sim_id";
    public static final String KEY_IMEI                     = "imei";
    public static final String KEY_BRAND                    = "brand";
    public static final String KEY_DEVICE                   = "device";
    public static final String KEY_MODEL                    = "model";
    public static final String KEY_ID_MODEL                 = "id_model";
    public static final String KEY_NAMA_PRODUK              = "nama_produk";
    public static final String KEY_API_SDK                  = "api_sdk";
    public static final String KEY_OS                       = "os";

    //untuk activity INBOX
    public static final String DISP_KD_INBOX                = "kode_inbox";
    public static final String DISP_TITLE                   = "title";
    public static final String DISP_MESSAGE                 = "message";
    public static final String DISP_URL_IMG_INBOX           = "img_inbox";
    public static final String DISP_URL_INBOX               = "link_inbox";








    //untuk display pilihan dealer
    public static final String KEY_USERCODE                 = "usercode";
    public static final String DISP_COY_DEALER              = "coy_dealer";
    public static final String DISP_KD_DEALER               = "kode_dealer";
    public static final String DISP_NAMA_DEALER             = "nama_dealer";
    public static final String DISP_ALAMAT_DEALER           = "alamat_dealer";
    public static final String DISP_KOTA_DEALER             = "kota";
    public static final String DISP_PROVINSI_DEALER         = "provinsi";
    public static final String DISP_TELP_DEALER             = "telp_dealer";

    //dipsplay maps
    public static final String MAPS_COY_DEALER              = "coy_dealer";
    public static final String MAPS_KD_DEALER               = "kode_dealer";
    public static final String MAPS_NAMA_DEALER             = "nama_dealer";
    public static final String MAPS_ALAMAT_DEALER           = "alamat_dealer";
    public static final String MAPS_KOTA_DEALER             = "kota";
    public static final String MAPS_PROVINSI_DEALER         = "provinsi";
    public static final String MAPS_TELP_DEALER             = "telp_dealer";
    public static final String MAPS_LAT                     = "maps_lat";
    public static final String MAPS_LNG                     = "maps_lng";
    public static final String MAPS_ADDRESS                 = "maps_address";
    public static final String MAPS_NVIEW                   = "maps_nview";
    public static final String MAPS_NACCESS                 = "maps_naccess";


    //untuk change password
    public static final String KEY_PASSWORD_LAMA            = "password_lama";
    public static final String KEY_PASSWORD_BARU            = "password_baru";

    //NEWS
    public static final String TAG_NO                       = "no";
    public static final String TAG_ID                       = "id";
    public static final String TAG_JUDUL                    = "judul";
    public static final String TAG_TGL                      = "tgl";
    public static final String TAG_ISI                      = "isi";
    public static final String TAG_GAMBAR                   = "gambar";
    public static final String TAG_LINK_NEWS                = "link_news";

    //display in data service untuk table
    public static final String DISP_KD_CHASIS               = "kode_chasis";
    public static final String DISP_KD_CUSTOMER             = "kode_customer";
    public static final String DISP_KD_COY                  = "kode_coy";
    public static final String DISP_KD_CAB                  = "kode_cab";
    public static final String DISP_KD_REPAIR               = "kode_repair";
    public static final String DISP_KD_NOTRANP              = "kode_notranp";
    public static final String DISP_KD_PARTNO               = "kode_partno";
    public static final String DISP_KD_NOKWB                = "kode_nokwb";
    public static final String DISP_KD_NOWO                 = "kode_nowo";

    //untuk detail service
    public static final String DISP_COMPANY                 = "company";
    public static final String DISP_WO                      = "rprcnowo";
    public static final String DISP_WO_DATE                 = "rprdtglwo";
    public static final String DISP_TRANS                   = "rprcnotranp";
    public static final String DISP_REPAIR_CODE             = "rprcpartno";
    public static final String DISP_REPAIR_MODE_CODE        = "rprckdtrn";
    public static final String DISP_DESCRIPTION             = "rprcnmrep";
    public static final String DISP_REPAIR_QTY              = "rprnqtyrep";
    public static final String DISP_INIT_PAID_BY_BEBAN      = "rprckdbeban";
    public static final String DISP_SALES_PRICE             = "rprnprice";
    public static final String DISP_DISCOUNT                = "rprndisc";
    public static final String DISP_TOTAL_AMOUNT            = "rprntotal";
    public static final String DISP_SA                      = "rprcnmsa";
    public static final String DISP_TECHNISM                = "rprcnmmkn";
    public static final String DISP_KM_IN                   = "rprnkmin";
    public static final String DISP_NOTE                    = "note";

    //tipe kendaraan
    public static final String DISP_KD_KENDARAAN            = "kd_kendaraan";
    public static final String DISP_NAMA_KENDARAAN          = "nama_kendaraan";
    public static final String DISP_IMAGE_KENDARAAN         = "image";
    public static final String DISP_TELP_CRM                = "0800-182-1407";  //021-728-97000

    //untuk update aplikasi
    public static final String TAG_LINK_UPDATE              = "link_update";
    public static final String TAG_VERSI_UPDATE             = "versi_update";
    public static final String TAG_MESSAGE_UPDATE           = "message_update";

    public static final String DISP_LTS_COY                 = "coy";
    public static final String DISP_LTS_KD                  = "lts";
    public static final String DISP_LTS_NAME                = "name";

    public static final boolean CEK_KONEKSI(Context cek){
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }


}
