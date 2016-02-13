package com.attila.wakerupper.Factories;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.attila.wakerupper.R;
import com.attila.wakerupper.Receivers.PhoneReceiver;
import com.attila.wakerupper.Receivers.SMSReceiver;

public class ReceiverFactory {

    public static boolean isHandlerAttached(Context context) {
        PackageManager pm  = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, SMSReceiver.class);
        return pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public static void bindHandler(Context context, boolean isTextMonitoringEnabled,
                                   boolean isPhoneMonitoringEnabled) {

        if( isTextMonitoringEnabled ) {
            _internalBindUnbind(context, SMSReceiver.class, true);
            resetTextsReceived(context);
        }

        if( isPhoneMonitoringEnabled ) {
            _internalBindUnbind(context, PhoneReceiver.class, true);
        }
    }

    public static void unbindHandler(Context context) {
        _internalBindUnbind(context, SMSReceiver.class, false);
        _internalBindUnbind(context, PhoneReceiver.class, false);
    }

    public static void resetTextsReceived( Context context ) {
        SharedPreferencesFactory.writeInt(context, context.getString(R.string.text_received_service_key), 0);
    }

    private static void _internalBindUnbind( Context context, Class receiver, boolean enabled ) {
        PackageManager pm  = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, receiver);
        pm.setComponentEnabledSetting(componentName,
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
