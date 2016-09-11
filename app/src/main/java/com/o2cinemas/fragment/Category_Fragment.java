package com.o2cinemas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.o2cinemas.adapters.Sort_TvShow;
import com.o2cinemas.beans.SortTvShow_item;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.o2vidz.TvShow_Category;
import com.o2cinemas.utilities.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 6/27/2016.
 */
public class Category_Fragment extends Fragment {
    private TextView first, last, cnctn_text;
    private Button cnctn_button;
    private RecyclerView tvshowRecycler;
    private GridView sortGrid;
    private ConnectionDetector connectionDetector;
    private RelativeLayout cnctnAvailable, cnctnDisable, loadingLayout;
    private Toolbar toolbar;
    private String sortingText[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0-9"};
    private List<SortTvShow_item> sortTvShowItems;
    private LinearLayout btnLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tv_show__category, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cnctnAvailable = (RelativeLayout) getView().findViewById(R.id.cnctnAvailable_categoryTv);
        cnctnDisable = (RelativeLayout) getView().findViewById(R.id.cnctn_categoryTv);
        loadingLayout = (RelativeLayout) getView().findViewById(R.id.loading_categoryTv);
        cnctnAvailable.setVisibility(View.VISIBLE);
        cnctnDisable.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        findViews();

    }

    private void findViews() {
        View toolbar_view = getView().findViewById(R.id.toolbar_tvshowCategory);
        toolbar = (Toolbar) toolbar_view.findViewById(R.id.toolbar_home);
        toolbar.setVisibility(View.GONE);


        first = (TextView) getView().findViewById(R.id.first);
//        first.setOnClickListener(this);
        last = (TextView) getView().findViewById(R.id.last);
//        last.setOnClickListener(this);
        btnLayout = (LinearLayout) getView().findViewById(R.id.btnLayout);
        cnctnAvailable = (RelativeLayout) getView().findViewById(R.id.cnctnAvailable_categoryTv);
        cnctnDisable = (RelativeLayout) getView().findViewById(R.id.cnctn_categoryTv);
        loadingLayout = (RelativeLayout) getView().findViewById(R.id.loading_categoryTv);
        cnctn_text = (TextView) getView().findViewById(R.id.intenettext_tv);
        cnctn_button = (Button) getView().findViewById(R.id.button_discnctTvCategory);
        cnctn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
//                    String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
//                    String filter = getIntent().getStringExtra(Constants.TVSHOW_FILTER);
//                    getImagelist(type, filter, String.valueOf(start), String.valueOf(limit));
                } else {
//                    ConnectionDetector.showToast(TvShow_Category.this, getApplicationContext().getString(R.string.connection_toast));
                }

            }
        });

        tvshowRecycler = (RecyclerView) getView().findViewById(R.id.recycler_aplhaSort);
        sortGrid = (GridView) getView().findViewById(R.id.sort_category);
        tvshowRecycler.setHasFixedSize(true);
        tvshowRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        sortTvShowItems = new ArrayList<>();
        for (int i = 0; i < sortingText.length; i++) {
            SortTvShow_item sortItem = new SortTvShow_item(sortingText[i]);
            sortTvShowItems.add(sortItem);
            Sort_TvShow sort_tvShow = new Sort_TvShow(getActivity(), sortTvShowItems);
            sortGrid.setAdapter(sort_tvShow);
            sortGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filter = (sortTvShowItems.get(position).getAlpha());
//                    Log.e("aplhabet", ":-" + filter);
//                    Intent intent1 = new Intent(getActivity(), TvShow_Category.class);
//                    intent1.putExtra(Constants.SHOW_TYPE, "tvshows");
//                    intent1.putExtra(Constants.TVSHOW_FILTER, filter);
//                    startActivity(intent1);

                }
            });

        }
    }}
