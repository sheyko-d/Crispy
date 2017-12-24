package com.dmytrosheiko.crispy.util;

import android.util.Log;

/**
 * Helper class.
 */
public class Util {

    private static final String LOG_TAG = "CrispyDebug";
    public static final String BASE_API_URL = "https://api.unsplash.com/";
    private static final int DEBUG_MAX_LENGTH = 500;

    /**
     * Adds a message to LogCat.
     */
    public static void Log(Object text) {
        Log.d(LOG_TAG, text + "");
    }

    /**
     * Split output log by lines
     *
     * @param tag  tag
     * @param data data to output
     */
    @SuppressWarnings("unused")
    public static void splitOutput(String tag, String data) {
        if (data == null)
            return;
        int i = 0;
        while (i < data.length()) {
            Log.d(tag, data.substring(i, i + DEBUG_MAX_LENGTH > data.length() ? data.length() : i
                    + DEBUG_MAX_LENGTH));
            i += DEBUG_MAX_LENGTH;
        }
    }
}
