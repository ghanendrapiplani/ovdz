package com.o2cinemas.utilities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by root on 2/12/15.
 */
public class Utility {
    Context context;

    public static boolean isValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }


    public static void volleyErrorHandling(VolleyError error, Context context) {
        if (error instanceof NetworkError) {
            Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, "AuthFailure Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(context, "NoConnection Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(context, "Timeout Error", Toast.LENGTH_LONG).show();
        }
    }




    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getDownloadingItem(Context context) {
        int num = 0;
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();

        Cursor cur = mgr.query(query);
        int index = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);

        if(cur.moveToFirst()) {
            do {
                int status = cur.getInt(index);
                if (status == DownloadManager.STATUS_RUNNING) {
                    num ++;
                }
            }while (cur.moveToNext());
        }
        cur.close();
        return num;
    }

    public static boolean shouldAddToDownloadQueue(Context context) {
        int numOfTasks = SharedPreferenceUtil.getDownloadTaskNum(context);
        if (getDownloadingItem(context) >= numOfTasks) {
            return true;
        }
        return false;
    }
}
