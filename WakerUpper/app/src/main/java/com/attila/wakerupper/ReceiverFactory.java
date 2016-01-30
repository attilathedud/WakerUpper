package com.attila.wakerupper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class ReceiverFactory {

    public static boolean isHandlerAttached(Context context) {
        PackageManager pm  = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, SMSReceiver.class);
        return pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public static void bindHandler(Context context, int textAmount) {
        _internalBindUnbind(context, true);
        _internalSendTextAmount(context, textAmount);
    }

    public static void unbindHandler(Context context) {
        _internalBindUnbind(context, false);
    }

    private static void _internalBindUnbind( Context context, boolean enabled ) {
        PackageManager pm  = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, SMSReceiver.class);
        pm.setComponentEnabledSetting(componentName,
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static void _internalSendTextAmount( Context context, int textAmount ) {
        Intent i = new Intent(context.getString(R.string.update_text_service_key));
        i.putExtra(context.getString(R.string.text_amount_service_key), textAmount);
        context.sendBroadcast(i);
    }

}
