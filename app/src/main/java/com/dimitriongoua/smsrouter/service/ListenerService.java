package com.dimitriongoua.smsrouter.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dimitriongoua.smsrouter.app.SMSRouter;
import com.dimitriongoua.smsrouter.model.Mess;
import com.dimitriongoua.smsrouter.util.Master;
import com.dimitriongoua.smsrouter.util.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;

public class ListenerService extends Service {

    private static final String TAG = "SMSRouter@" + ListenerService.class.getSimpleName();

    private DatabaseReference mFirebaseDatabase;
    private SessionManager session;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        session = new SessionManager(this);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("message");

        Toast.makeText(this, "Lancement du service", Toast.LENGTH_SHORT).show();

        Master.d(TAG, "Lancement du service");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Mess mess = dataSnapshot.getValue(Mess.class);

                // On vérifie le message est null
                if (mess == null) {
                    Master.d(TAG, "Aucun message à envoyer");
                    return;
                }

                // On vérifie si l'identifiant du smartphone est renseigné
                if (mess.device == null) {
                    Master.d(TAG, "Identifiant non renseigné");
                    return;
                } else {
                    if (!mess.device.equals(SMSRouter.getInstance().getAppID())) {
                        Master.d(TAG, "Message ignoré");
                        return;
                    }
                }

                Master.i(TAG, "Nouvelle requête détectée !");

                if (session.getLastTime() != mess.timestamp) {
                    new Master(ListenerService.this).sendSMS(mess);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Master.e(TAG, "Impossible de lire le message : " + error.getMessage());
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Destruction du service", Toast.LENGTH_SHORT).show();
        Master.d(TAG, "Destruction du service");
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
