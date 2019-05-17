package com.dimitriongoua.smsrouter.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.dimitriongoua.smsrouter.util.Master;
import com.dimitriongoua.smsrouter.util.SessionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;

public class SMSRouter extends Application {

    private static final String TAG = "SMSRouter@App";

    private static SMSRouter instance;
    public static long SESSIONID;
    private final String appIDFilename = "smsrouterid";
    private final String sessTokenFilename = "sessiontoken";

    public SMSRouter() {
        super();
        SESSIONID = System.currentTimeMillis();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Fabric.with(this, new Crashlytics());

        Master.i(TAG, "Identifiant de l'application : " + getAppID());

    }

    public static synchronized SMSRouter getInstance(){
        return (instance != null) ? instance : new SMSRouter();
    }

    public String getSessionToken(){
        String sessionToken = null;

        File file = new File(getFilesDir(), sessTokenFilename);

        if (file.exists()){
            FileInputStream inputStream;
            try {
                inputStream = openFileInput(sessTokenFilename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                if (reader.ready()) sessionToken = reader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sessionToken = UUID.randomUUID().toString();
            saveSessionToken(sessionToken);
        }

        Master.e(TAG, "getting session token : " + sessionToken);

        return sessionToken;
    }

    public void saveSessionToken(String sessionToken){

        sessionToken = UUID.randomUUID().toString();
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(sessTokenFilename, Context.MODE_PRIVATE);
            outputStream.write(sessionToken.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Master.e(TAG, "session token saved : " + sessionToken);
    }

    public String getAppID(){

        String appID = null;

        File file = new File(getFilesDir(), appIDFilename);

        if (file.exists()){
            FileInputStream inputStream;
            try {
                inputStream = openFileInput(appIDFilename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                if (reader.ready()) appID = reader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            appID = UUID.randomUUID().toString();
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(appIDFilename, Context.MODE_PRIVATE);
                outputStream.write(appID.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "appID : " + appID);
        return appID;
    }

}
