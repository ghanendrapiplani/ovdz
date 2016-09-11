package com.o2cinemas.utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;

/**
 * Created by user on 5/20/2016.
 */
public class HomeLuanher {

    private Activity activity;
    SharedPreferences appPref;
    boolean isFirstTime = true;

    public HomeLuanher(Activity activity) {
        this.activity = activity;
        addShortcut();
        removeShortcut();
    }



    private void addShortcut() {

        Intent shortcutIntent = new Intent(activity, MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "O2Vidz");
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(activity, R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        activity.sendBroadcast(addIntent);
    }

    private void removeShortcut() {

        Intent shortcutIntent = new Intent(activity, MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "O2Vidz");

        addIntent
                .setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        activity.sendBroadcast(addIntent);
    }
}
