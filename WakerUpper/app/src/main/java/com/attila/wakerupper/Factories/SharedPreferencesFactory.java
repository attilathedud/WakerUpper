package com.attila.wakerupper.Factories;

import android.content.Context;
import android.content.SharedPreferences;

import com.attila.wakerupper.R;

public class SharedPreferencesFactory {

    public static boolean writeInt( Context context, String key, int value ) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);

        return editor.commit();
    }

    public static int readInt( Context context, String key ) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);

        //todo: store default value in integer file
        //int defaultValue = context.getResources().getInteger(R.string.saved_high_score_default);

        return sharedPref.getInt(key, -1);
    }

    public static boolean writeLong( Context context, String key, long value ) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);

        return editor.commit();
    }

    public static long readLong( Context context, String key ) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);

        //todo: store default value in integer file
        //int defaultValue = context.getResources().getInteger(R.string.saved_high_score_default);

        return sharedPref.getLong(key, -1);
    }

    public static boolean writeBoolean( Context context, String key, boolean value ) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);

        return editor.commit();
    }

    public static boolean readBoolean( Context context, String key ) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_file_name), Context.MODE_PRIVATE);

        return sharedPref.getBoolean(key, false);
    }

}
