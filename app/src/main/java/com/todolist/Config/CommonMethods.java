package com.todolist.Config;

import android.content.Context;
import android.net.ConnectivityManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.todolist.R;

/**
 * Created by NilayS on 11/10/2016.
 */
public class CommonMethods {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
