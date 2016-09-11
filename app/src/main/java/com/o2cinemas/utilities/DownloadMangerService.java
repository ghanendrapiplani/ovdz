package com.o2cinemas.utilities;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.DownloadItem;
import com.o2cinemas.o2vidz.R;

import java.util.ArrayList;

/**
 * Created by admin on 9/9/16.
 */
public class DownloadMangerService extends Service{

    ArrayList<DownloadItem> downloadItems;
    //TODO need to handle when device is restarted
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        downloadItems = new ArrayList<>();
        registerDownloadEvent();
        return Service.START_NOT_STICKY;
    }

    private void registerDownloadEvent() {
        if (this != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION);
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            this.registerReceiver(receiver, intentFilter);
        }
    }

    public void unRegisterDownloadEvent() {
        if (this != null) {
            this.unregisterReceiver(receiver);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                    long receivedID = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                    DownloadManager mgr = (DownloadManager)
                            context.getSystemService(Context.DOWNLOAD_SERVICE);

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(receivedID);
                    Cursor cur = mgr.query(query);
                    int index = cur.getColumnIndex(
                            DownloadManager.COLUMN_URI);
                    if(cur.moveToFirst()) {
                        String downloadedUrl = cur.getString(index);
                        if (downloadedUrl != null) {
                            removeDownloadItem(downloadedUrl);
                        }
                    }
                    cur.close();

                    downloadNextItem();
                } else if (action.equals(Constants.ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION)) {

                    String url = intent.getStringExtra(Constants.DOWNLOAD_URL_KEY);
                    String title = intent.getStringExtra(Constants.DOWNLOAD_TITLE_KEY);
                    Log.d("test", "add ot queue url: " + url);
                    if (url != null && title != null) {
                        DownloadItem item = new DownloadItem(title, url);
                        downloadItems.add(item);
                        Toast.makeText(DownloadMangerService.this, "This movie is added to queue. Please wait until downloading movies finish.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void removeDownloadItem(String url) {
        if (downloadItems != null && downloadItems.size() > 0) {
            for (DownloadItem downloadItem: downloadItems) {
                if (downloadItem.getUrl() != null && downloadItem.getUrl().equals(url)) {
                    downloadItems.remove(downloadItem);
                    break;
                }
            }
            Log.d("test", "downloaded item size: " + downloadItems.size());
        }
    }

    private void downloadNextItem() {
        if (downloadItems != null && downloadItems.size() > 0) {
            DownloadItem item = downloadItems.get(0);
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.getUrl()));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(item.getTitle())
                    .setDescription(getString(R.string.dwnld_Description))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir("/O2Vidz", item.getTitle());

            downloadManager.enqueue(request);
            downloadItems.remove(item);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterDownloadEvent();
    }
}
