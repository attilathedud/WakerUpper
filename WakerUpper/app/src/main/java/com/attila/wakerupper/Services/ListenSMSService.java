package com.attila.wakerupper.Services;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.attila.wakerupper.ContentObservers.SMSSentObserver;

public class ListenSMSService extends Service {

    ContentObserver smsSentObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();

        getContentResolver().unregisterContentObserver(smsSentObserver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        smsSentObserver = new SMSSentObserver( new Handler(), getBaseContext() );
        getContentResolver().registerContentObserver(
                Uri.parse("content://sms"), true, smsSentObserver );

        return START_STICKY;
    }
}
