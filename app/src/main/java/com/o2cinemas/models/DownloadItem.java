package com.o2cinemas.models;

/**
 * Created by admin on 9/9/16.
 */
public class DownloadItem {
    String title;
    String url;
    public DownloadItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
