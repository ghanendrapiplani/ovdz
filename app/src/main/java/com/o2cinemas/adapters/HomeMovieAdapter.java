package com.o2cinemas.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.fragment.Gp3_Fragment;
import com.o2cinemas.fragment.Home_Fragment;
import com.o2cinemas.fragment.Mp4_Fragment;
import com.o2cinemas.o2vidz.Detail_Page;
import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.o2vidz.RecyclerItemClickListner;
import com.o2cinemas.o2vidz.TvShow_Category;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user on 5/18/2016.
 */
public class HomeMovieAdapter extends RecyclerView.Adapter<HomeMovieAdapter.HomeViewHolder> {

    private Map<String, ArrayList<HomeMoivesBean>> listMap;
    private Context context;
    private ArrayList<String> titleList;
    private Fragment currentFragment;

    public HomeMovieAdapter(Fragment currentFragment, Map<String, ArrayList<HomeMoivesBean>> listMap, Context context, ArrayList<String> titleList) {
        this.listMap = listMap;
        this.context = context;
        this.titleList = titleList;
        this.currentFragment = currentFragment;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mp4_adapter_row, parent, false);
        HomeViewHolder viewHolder = new HomeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, final int position) {

        String title = titleList.get(position);
        holder.titleView.setText(title);
        holder.setTitle(title);

        if (position == 0) {
            holder.subs.setVisibility(View.GONE);
        } else {
            holder.subs.setVisibility(View.VISIBLE);
        }

        ArrayList<HomeMoivesBean> beanArrayList = new ArrayList<>();
        beanArrayList = listMap.get(title);

        if (beanArrayList.size() > 0) {
            HomeMoiveItemAdapter itemAdapter = new HomeMoiveItemAdapter(beanArrayList, context);
            holder.moviesList.setAdapter(itemAdapter);
            holder.adapterMainLayout.setVisibility(View.VISIBLE);
        } else {
            holder.adapterMainLayout.setVisibility(View.GONE);
        }

        if (listMap.size() <= 3) {
            holder.subs.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listMap.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView, viewMoreBtn;
        private RecyclerView moviesList;
        private RelativeLayout adapterMainLayout;
        private ImageView subs;
        private String title;

        public HomeViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.text_Mp4Bollywood);
            viewMoreBtn = (TextView) itemView.findViewById(R.id.view_Mp4Bollywood);
            moviesList = (RecyclerView) itemView.findViewById(R.id.gridView);
            subs = (ImageView) itemView.findViewById(R.id.subscribe_Mp4Bollywood);
            moviesList.setHasFixedSize(true);
            moviesList.setLayoutManager(new GridLayoutManager(context, 3));
            adapterMainLayout = (RelativeLayout) itemView.findViewById(R.id.adapterMainLayout);

            viewMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentFragment != null && !(currentFragment instanceof Home_Fragment)) {
                        if (title != null) {
                            String movieType = "";
                            if (currentFragment instanceof  Mp4_Fragment) {
                                movieType = Constants.MP_4_TYPE;
                            } else {
                                movieType = Constants.GP3_TYPE;
                            }
                            ((MainActivity) context).goToGroupDetailsFragment(title, movieType);
                        }
                    }else {
                        ((MainActivity) context).getCurrentPositionofFragment(getAdapterPosition());
                    }
                }
            });

        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}


