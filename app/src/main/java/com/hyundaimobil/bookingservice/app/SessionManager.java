package com.hyundaimobil.bookingservice.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hyundaimobil.bookingservice.Login;
import com.hyundaimobil.bookingservice.MainActivity;

import java.util.HashMap;

/**
 * Created by User HMI on 5/12/2017.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // nama sharepreference
    private static final String PREF_NAME = "Sesi";

    // All Shared Preferences Keys
    private static final String IS_LOGIN            = "IsLoggedIn";
    public static final String KEY_USERCODE         = "usercode";
    public static final String KEY_NIP              = "nip";
    public static final String KEY_COMPANY_CODE     = "company_code";
    public static final String KEY_BRANCH_CODE      = "branch_code";
    public static final String KEY_DEPT_CODE        = "department_code";
    public static final String KEY_FULLNAME         = "fullname";
    public static final String KEY_ACCESS_LEVEL     = "access_level";
    public static final String KEY_SALESMAN_CODE    = "salesman_code";
    public static final String KEY_SUPERVISOR_CODE  = "supervisor_code";
    public static final String KEY_DEALER_CODE      = "dealer_code";
    public static final String KEY_LOGIN_NUMBER     = "login_number";
    public static final String KEY_KD_CUSTOMER      = "kd_customer";
    public static final String KEY_CHASIS           = "chasis";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String usercode, String nip, String company_code, String branch_code,
                                   String dept_code, String fullname, String access_level, String salesman_code,
                                   String supervisor_code, String dealer_code, String login_number,
                                   String kd_customer, String chasis){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERCODE, usercode);
        editor.putString(KEY_NIP, nip);
        editor.putString(KEY_COMPANY_CODE, company_code);
        editor.putString(KEY_BRANCH_CODE, branch_code);
        editor.putString(KEY_DEPT_CODE, dept_code);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_ACCESS_LEVEL, access_level);
        editor.putString(KEY_SALESMAN_CODE, salesman_code);
        editor.putString(KEY_SUPERVISOR_CODE, supervisor_code);
        editor.putString(KEY_DEALER_CODE, dealer_code);
        editor.putString(KEY_LOGIN_NUMBER, login_number);
        editor.putString(KEY_KD_CUSTOMER, kd_customer);
        editor.putString(KEY_CHASIS, chasis);

        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            //((Activity)_context).finish();
        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERCODE, pref.getString(KEY_USERCODE, null));
        user.put(KEY_NIP, pref.getString(KEY_NIP, null));
        user.put(KEY_COMPANY_CODE, pref.getString(KEY_COMPANY_CODE, null));
        user.put(KEY_BRANCH_CODE, pref.getString(KEY_BRANCH_CODE, null));
        user.put(KEY_DEPT_CODE, pref.getString(KEY_DEPT_CODE, null));
        user.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, null));
        user.put(KEY_ACCESS_LEVEL, pref.getString(KEY_ACCESS_LEVEL, null));
        user.put(KEY_SALESMAN_CODE, pref.getString(KEY_SALESMAN_CODE, null));
        user.put(KEY_SUPERVISOR_CODE, pref.getString(KEY_SUPERVISOR_CODE, null));
        user.put(KEY_DEALER_CODE, pref.getString(KEY_DEALER_CODE, null));
        user.put(KEY_LOGIN_NUMBER, pref.getString(KEY_LOGIN_NUMBER, null));
        user.put(KEY_KD_CUSTOMER, pref.getString(KEY_KD_CUSTOMER, null));
        user.put(KEY_CHASIS, pref.getString(KEY_CHASIS, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
