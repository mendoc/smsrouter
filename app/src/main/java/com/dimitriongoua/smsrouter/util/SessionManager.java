package com.dimitriongoua.smsrouter.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SessionManager {

    private String TAG = "SMSRouter@" + SessionManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Sharedpref file name
    private static final String PREF_NAME = "smsrouter";

    // All Shared Preferences Keys
    private static final String KEY_LASTTIME  = "lasttime";
    private static final String KEY_SESS_TOKEN  = "sesstoken";


    public SessionManager(Context _context) {
        // Context
        Context context = _context;

        Fabric.with(context, new Crashlytics());

        // Shared pref mode
        int PRIVATE_MODE = 0;
        this.pref     = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor   = pref.edit();
        Master.i(TAG, "Initialisation de SessionManager");
    }

    public void saveLastTime(long lasttime){
        editor.putLong(KEY_LASTTIME, lasttime);
        editor.commit();
        Master.i(TAG, "Enregistrement de la date du dernier message");
    }

    public void saveSessionToken(String token){
        editor.putString(KEY_SESS_TOKEN, token);
        editor.commit();
        Master.i(TAG, "Enregistrement du jeton de session");
    }

    public long getLastTime() {
        Master.i(TAG, "Récupération de la date du dernier message en mémoire");
        return pref.getLong(KEY_LASTTIME, 0);
    }

    public String getSessionToken() {
        Master.i(TAG, "Récupération de la date du dernier message en mémoire");
        return pref.getString(KEY_SESS_TOKEN, "common");
    }
}
