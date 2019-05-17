package com.dimitriongoua.smsrouter.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dimitriongoua.smsrouter.model.Mess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;

public class Master {

    private static final String TAG = "SMSRouter@" + Master.class.getSimpleName();

    private Context context;

    public Master(Context context) {
        this.context = context;
        Fabric.with(context, new Crashlytics());
    }

    public void sendSMS(Mess message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(message.number, null, message.body, null, null);

            SessionManager session = new SessionManager(context);
            session.saveLastTime(message.timestamp);

            Toast.makeText(context, "Message envoyé au : " + message.number, Toast.LENGTH_SHORT).show();
            Master.i(TAG, "Message envoyé au : " + message.number);
        } catch (Exception e) {
            Master.e(TAG, "sendSMS() error : " + e.getMessage());
        }
    }

    public static void i(String tag, String message){
        LogSys.log(Log.INFO, tag, message);
        Crashlytics.log(Log.INFO, tag, message);
    }

    public static void e(String tag, String message){
        LogSys.log(Log.INFO, tag, message);
        Crashlytics.log(Log.ERROR, tag, message);
    }

    public static void d(String tag, String message){
        LogSys.log(Log.INFO, tag, message);
        Crashlytics.log(Log.DEBUG, tag, message);
    }

    public static void w(String tag, String message) {
        LogSys.log(Log.INFO, tag, message);
        Crashlytics.log(Log.WARN, tag, message);
    }
}
