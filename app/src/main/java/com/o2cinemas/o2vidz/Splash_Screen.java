package com.o2cinemas.o2vidz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

public class Splash_Screen extends Activity {
    SharedPreferences appPref;
    boolean isFirstTime = true;

    private static int SPLASH_TIME_OUT = 2000;
    ImageView imageView;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
//        imageView= (ImageView) findViewById(R.id.splash_image);
        img = (ImageView) findViewById(R.id.img);
//        imageView.setBackgroundResource(R.drawable.splash_image);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(Splash_Screen.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);


        appPref = getSharedPreferences("isFirstTime", 0);
        isFirstTime = appPref.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            // Create explicit intent which will be used to call Our application
            // when some one clicked on short cut
            Intent shortcutIntent = new Intent(getApplicationContext(),
                    MainActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();

            // Create Implicit intent and assign Shortcut Application Name, Icon
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "O2Vidz");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(
                            getApplicationContext(), R.mipmap.ic_launcher));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            // Set preference to inform that we have created shortcut on
            // Homescreen
            SharedPreferences.Editor editor = appPref.edit();
            editor.putBoolean("isFirstTime", false);
            editor.commit();

        }
    }
    }

