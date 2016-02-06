package com.attila.wakerupper.Logging;

import com.attila.wakerupper.BuildConfig;
import com.orhanobut.logger.Logger;

public class DebugLogger {

    public static void init() {
        Logger.init();
    }

    public static void Log( String message ) {
        if( BuildConfig.DEBUG )
            Logger.d(message);
    }

    public static void Log( Exception e, String message ) {
        if( BuildConfig.DEBUG )
            Logger.e(e, message);
    }
}
