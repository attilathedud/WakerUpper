package com.attila.wakerupper.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;

import com.attila.wakerupper.R;

public class SMSReceiver extends BroadcastReceiver {

    private int _textAmount;
    private int _textsReceived;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if( action == null )
            return;

        if(action.equals(context.getString(R.string.update_text_service_key))){
            _textAmount = intent.getExtras().getInt(context.getString(R.string.text_amount_service_key));
        }
        else {
            Bundle bundle = intent.getExtras();
            if( bundle == null )
                return;

            //todo: use getMessagesFromIntent (Intent intent)
            Object messages[] = (Object[]) bundle.get("pdus");
            if( messages == null )
                return;

            _textsReceived += messages.length;

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
}
