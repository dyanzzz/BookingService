package com.hyundaimobil.bookingservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User HMI on 1/12/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME       = "booking_service.db";
    private static final int DATABASE_VERSION       = 1;

    public static final String TABEL_STATUS_KLIK    = "status_klik_booking_service";

    public static final String ID  		    = "id";
    public static final String TANGGAL      = "tgl";
    public static final String STATUS 	    = "status";
    public static final String STATUS_APP   = "status_app";
    //1. booking service hanya 1x 1hari/user
    //2. test drive hanya 1x 1hari/user
    //3. emergency hanya 3x 1hari/user


    public static final String CREATE_TABEL_STATUS_KLIK = "CREATE TABLE "+TABEL_STATUS_KLIK+"("
            +ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TANGGAL+" DATE NULL, "
            +STATUS+" INTEGER NULL, "
            +STATUS_APP+" INTEGER NULL "
            +")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Data", "onCreate: " + CREATE_TABEL_STATUS_KLIK);
        db.execSQL(CREATE_TABEL_STATUS_KLIK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
