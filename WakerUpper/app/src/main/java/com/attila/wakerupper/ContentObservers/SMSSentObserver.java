package com.attila.wakerupper.ContentObservers;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.attila.wakerupper.Factories.ReceiverFactory;
import com.attila.wakerupper.Logging.DebugLogger;

public class SMSSentObserver extends ContentObserver {

    private Context context;

    private final int MESSAGE_SENT_TYPE = 2;

    public SMSSentObserver( Handler handler, Context context ) {
        super(handler);

        this.context = context;
    }

    public void onChange( boolean selfChange ) {
        //reading the sms db-table is undocumented and might change, but google has yet to introduce
        //another way to get sent sms's
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms"), null, null, null, null);

        if (cursor.moveToNext()) {
            String protocol = cursor.getString( cursor.getColumnIndex("protocol") );
            int type = cursor.getInt( cursor.getColumnIndex("type") );

            if ( protocol != null || type != MESSAGE_SENT_TYPE ) {
                return;
            }

            DebugLogger.Log("Message sent");
            ReceiverFactory.resetTextsReceived(context);
        }
    }
}