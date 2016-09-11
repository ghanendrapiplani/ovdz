package com.o2cinemas.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.o2cinemas.fragment.Connection_Error;
import com.o2cinemas.fragment.Gp3_Fragment;
import com.o2cinemas.fragment.Home_Fragment;
import com.o2cinemas.fragment.Mp4_Fragment;
import com.o2cinemas.fragment.TopDownloads_Fragment;
import com.o2cinemas.fragment.TvShow_Fragment;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.utilities.ConnectionDetector;

/**
 * Created by user on 5/19/2016.
 */
public class ViewPager_Adapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Home", "Mp4 Movies", "3gp Movies", "Tv shows", "Top download"};
    private int[] tabIcon = {R.drawable.home, R.drawable.mp4, R.drawable.mp3, R.drawable.tv, R.drawable.down};
    private Context context;
    private ConnectionDetector connectionDetector;

    public ViewPager_Adapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public View getTabView(int position) {

        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        ImageView tabimage = (ImageView) v.findViewById(R.id.tab_icon);
        TextView tab_title = (TextView) v.findViewById(R.id.tab_title);
        tab_title.setText(tabTitles[position]);
        tabimage.setImageResource(tabIcon[position]);

        return v;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        connectionDetector = new ConnectionDetector(context);
        switch (position) {
            case 0:
                    fragment = Home_Fragment.getNewInstance();
                break;
            case 1:
                    fragment = Mp4_Fragment.getNewInstance();
                break;
            case 2:
                    fragment = Gp3_Fragment.getNewInstance();
                break;

            case 3:
                    fragment = TvShow_Fragment.getNewInstance();
                break;

            case 4:
                    fragment = TopDownloads_Fragment.getNewInstance();
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }



}


