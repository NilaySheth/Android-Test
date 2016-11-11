package com.todolist.Config;

import android.content.Context;
import android.net.ConnectivityManager;

public class CommonMethods {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
