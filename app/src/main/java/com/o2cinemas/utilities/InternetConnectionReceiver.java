package com.o2cinemas.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;

/**
 * Created by user on 5/23/2016.
 */
public class InternetConnectionReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure it's an event we're listening for ...
        if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) &&
                !intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) &&
                !intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            return;
        }

        ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        if (cm == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.connection_error,null);
            view.setVisibility(View.VISIBLE);
        }

        // Now to check if we're actually connected
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            // Start the service to do our thing
            Intent pushIntent = new Intent(context, MainActivity.class);
            context.startService(pushIntent);

            Toast.makeText(context, "Start service", Toast.LENGTH_SHORT).show();
        }
    }
}
