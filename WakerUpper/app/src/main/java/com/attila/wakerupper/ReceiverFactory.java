package com.attila.wakerupper;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class ReceiverFactory {

    public static boolean isHandlerAttached(Context context) {
        PackageManager pm  = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, SMSReceiver.class);
        return pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public static void bindHandler(Context context) {
        _internalBindUnbind(context, true);
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
}
