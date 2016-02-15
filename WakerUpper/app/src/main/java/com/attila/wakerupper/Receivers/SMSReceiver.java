package com.attila.wakerupper.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;

import com.attila.wakerupper.Factories.SharedPreferencesFactory;
import com.attila.wakerupper.R;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if( bundle == null )
            return;

        //can't use getMessagesFromIntent due to api level
        Object[] messages = (Object[]) bundle.get("pdus");
        if( messages == null )
            return;

        int _textsReceived = SharedPreferencesFactory.readInt(context, context.getString(R.string.text_received_service_key));

        _textsReceived += messages.length;

        SharedPreferencesFactory.writeInt(context, context.getString(R.string.text_received_service_key), _textsReceived);

        int _textAmount = SharedPreferencesFactory.readInt(context, context.getString(R.string.text_amount_service_key));

        //DebugLogger.Log("textsReceived: " + _textsReceived + "\ntextAmount: " + _textAmount);

        if( _textsReceived >= _textAmount ) {
            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);

            // Max out volume on phone
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        }

    }
}
