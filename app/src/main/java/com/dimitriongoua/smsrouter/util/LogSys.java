package com.dimitriongoua.smsrouter.util;

import android.util.Log;

import com.dimitriongoua.smsrouter.app.SMSRouter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogSys {

    public String tag;
    public String message;
    public int priority;
    public long timestamp;

    public LogSys() {
    }

    public LogSys(String tag, String message, int priority, long timestamp) {
        this.tag = tag;
        this.message = message;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return String.valueOf(timestamp);
    }

    public static void log(int priority, String tag, String message) {

        LogSys logSys = new LogSys(tag, message, priority, System.currentTimeMillis());

        String logTime = logSys.getTimestamp();
        SMSRouter app = SMSRouter.getInstance();
        String clientID = app.getAppID();
        //String session = app.getSessionToken();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("logs").child(clientID).child(logTime).setValue(logSys);
    }

    public static void i(String tag, String message) {

        LogSys logSys = new LogSys(tag, message, Log.INFO, System.currentTimeMillis());

        String logTime = logSys.getTimestamp();
        String clientID = SMSRouter.getInstance().getAppID();
        String session = "" + SMSRouter.SESSIONID;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("logs").child(clientID).child("sessions").child(session).child(logTime).setValue(logSys);
    }

    public static void e(String tag, String message) {
    }

    public static void d(String tag, String message) {
    }

    public static void w(String tag, String message) {
    }
}
