package com.o2cinemas.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.o2cinemas.o2vidz.R;

/**
 * Created by user on 5/19/2016.
 */
public class TopDownloads_Fragment extends Fragment {

    boolean isGotData;
    public static TopDownloads_Fragment topDownloads_fragment;

    public static TopDownloads_Fragment getNewInstance() {
        if (topDownloads_fragment == null) {
            topDownloads_fragment = new TopDownloads_Fragment();
        }

        return topDownloads_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getData() {
        if (!isGotData) {
            // get data

            isGotData = true;
        }
    }
}
