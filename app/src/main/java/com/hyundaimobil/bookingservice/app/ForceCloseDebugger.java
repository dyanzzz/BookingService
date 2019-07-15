package com.hyundaimobil.bookingservice.app;

import android.app.Activity;

import com.hyundaimobil.bookingservice.BuildConfig;

/**
 * Created by User HMI on 10/30/2017.
 */

public class ForceCloseDebugger {
    public static void handle(Activity context){
        if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            Thread.setDefaultUncaughtExceptionHandler(new ForceCloseException(context));
            String errorCaused  = context.getIntent().getStringExtra("bugs");
            System.out.println("FORCE CLOSE CAUSED BY : " + errorCaused);
        }else{
            Thread.setDefaultUncaughtExceptionHandler(new ForceCloseException(context));
            String errorCaused  = context.getIntent().getStringExtra("bugs");
            System.out.println("FORCE CLOSE CAUSED BY  : " + errorCaused);
        }
    }
}
