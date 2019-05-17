package com.dimitriongoua.smsrouter.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.dimitriongoua.smsrouter.R;
import com.dimitriongoua.smsrouter.app.SMSRouter;
import com.dimitriongoua.smsrouter.service.ListenerService;
import com.dimitriongoua.smsrouter.util.LogSys;
import com.dimitriongoua.smsrouter.util.Master;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SMSRouter@" + MainActivity.class.getSimpleName();

    // Requesting permission to SEND SMS
    private boolean permissionToSendSMS = false;
    private String [] permissions = {Manifest.permission.SEND_SMS};
    private static final int REQUEST_SEND_SMS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Crashlytics.setUserIdentifier(SMSRouter.getInstance().getAppID());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Master.w(TAG, "Pas de permission pour envoyer de SMS");
            ActivityCompat.requestPermissions(this, permissions, REQUEST_SEND_SMS);
        } else {
            Master.i(TAG, "Possibilité d'envoi de SMS");
        }

        if (!isListenServiceRunning()) startService(new Intent(this, ListenerService.class));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionToSendSMS  = true;
                    Master.i(TAG, "Permission pour envoyer des SMS accordée");
                }
            }
        }

        if (!permissionToSendSMS) {
            Master.w(TAG, "Permission pour envoyer des SMS refusée");
        }

        if (permissionToSendSMS)
            startService(new Intent(this, ListenerService.class));
    }

    public boolean isListenServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ListenerService.class.getName().equals(service.service.getClassName())) {
                Master.d(TAG, "Le service est actif.");
                return true;
            }
        }
        Master.d(TAG, "Le service est arrêté.");
        return false;
    }
}
