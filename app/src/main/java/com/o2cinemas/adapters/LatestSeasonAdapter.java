package com.o2cinemas.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user on 6/22/2016.
 */
public class LatestSeasonAdapter extends RecyclerView.Adapter<LatestSeasonAdapter.HomeViewHolder> {

    private Map<String, ArrayList<HomeMoivesBean>> listMap;
    private Context context;
    private ArrayList<String> titleList;

    public LatestSeasonAdapter(Map<String, ArrayList<HomeMoivesBean>> listMap, Context context, ArrayList<String> titleList) {
        this.listMap = listMap;
        this.context = context;
        this.titleList = titleList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mp4_adapter_row, parent, false);
        HomeViewHolder viewHolder = new HomeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, final int position) {
        holder.bindData(position);
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
                    ((MainActivity) context).goToGroupDetailsFragment(Constants.LATEST_CONDITIONS_TYPE, Constants.TV_SHOW_TYPE);

                }
            });

        }

        public void bindData(int position) {
            title = titleList.get(position);
            titleView.setText(title);

            if (position == 0) {
                subs.setVisibility(View.GONE);
            } else {
                subs.setVisibility(View.VISIBLE);
            }

            ArrayList<HomeMoivesBean> beanArrayList = new ArrayList<>();
            beanArrayList = listMap.get(title);
            if (beanArrayList.size() > 0 && titleList.get(position).contains("LATEST ADDITIONS")){
                HomeMoiveItemAdapter homeMoiveItemAdapter = new HomeMoiveItemAdapter(beanArrayList, context);
                moviesList.setAdapter(homeMoiveItemAdapter);
                adapterMainLayout.setVisibility(View.VISIBLE);
            }
            else {
                adapterMainLayout.setVisibility(View.GONE);
            }
            if (listMap.size() <= 3) {
                subs.setVisibility(View.GONE);
            }
        }
    }
}
