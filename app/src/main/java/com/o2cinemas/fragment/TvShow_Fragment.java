package com.o2cinemas.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.o2cinemas.adapters.EpisodeAdapter;
import com.o2cinemas.adapters.LatestSeasonAdapter;
import com.o2cinemas.adapters.Sort_TvShow;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.beans.SortTvShow_item;
import com.o2cinemas.beans.TvShow_DetailBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.Detail_Page;
import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.o2vidz.TvShow_Category;
import com.o2cinemas.o2vidz.TvShow_SeasonDetail;
import com.o2cinemas.parser.AppParser;
import com.o2cinemas.parser.StatusBean;
import com.o2cinemas.parser.TvShow_Parser;
import com.o2cinemas.utilities.ConnectionDetector;
import com.o2cinemas.utilities.Utility;
import com.o2cinemas.volley.AppController;
import com.o2cinemas.volley.CustomJSONObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/19/2016.
 */
public class TvShow_Fragment extends Fragment implements View.OnClickListener {
    private ConnectionDetector connectionDetector;
    private SwipeRefreshLayout pullToRefreshHome;
    private RecyclerView recyclerView, alphaGrid, recyclerView2;
    private boolean isLayoutRefresh = false;
    private TextView connection_error, sort_text;
    private Button retry;
    private Context context;
    private RelativeLayout cnctn_layout, loading_layout, conctn_disable;
    private GridView alpha_grid;
    private LinearLayout sorting;
    private List<SortTvShow_item> sortTvShowItems;
    private String sortingText[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0-9"};
    private LinearLayout connection_layout;
    private ArrayList<HomeMoivesBean> beanArrayList;
    private AutoCompleteTextView search;
    private String title;

    boolean isGotData;
    public static TvShow_Fragment tvShow_fragment;

    public static TvShow_Fragment getNewInstance() {
        if (tvShow_fragment == null) {
            tvShow_fragment = new TvShow_Fragment();
        }

        return tvShow_fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tvshow_fragment, null);
        isGotData = false;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        connectionDetector = new ConnectionDetector(getActivity());
        findViews();

        View toolbar_view = (View) ((MainActivity) getActivity()).findViewById(R.id.appbar);
        Toolbar toolbar = (Toolbar) toolbar_view.findViewById(R.id.toolbar_home);
        search = (AutoCompleteTextView) toolbar_view.findViewById(R.id.searchIcon);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getShowSearchResult();
            }
        });
    }

    public void getData() {
        if (!isGotData) {
            if (connectionDetector.isConnectingToInternet()) {
                getTvShowList();
                cnctn_layout.setVisibility(View.GONE);
                loading_layout.setVisibility(View.VISIBLE);
                connection_layout.setVisibility(View.VISIBLE);
            } else {
                ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));
                cnctn_layout.setVisibility(View.VISIBLE);
                connection_layout.setVisibility(View.GONE);
                loading_layout.setVisibility(View.GONE);
            }

            isGotData = true;
        }

    }
    private void getShowSearchResult() {

        Activity activity = getActivity();
        if (activity != null && ((MainActivity)activity).getCurrentTabPosition() == Constants.TV_SHOW_TAB_POSITION) {
            cnctn_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            title = search.getText().toString().trim();


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.putOpt("TvShows_search_key", title);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            CustomJSONObjectRequest jsonObjectRequestSearch = new CustomJSONObjectRequest(Request.Method.POST,
                    Constants.searchTvShow, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    AppParser parser = new AppParser();
                    StatusBean statusBean = new StatusBean();
                    statusBean = parser.getTvShowSearchResponse(response.toString());
                    if (statusBean != null) {
                        if (statusBean.getStatus().equals("1")) {
                            final ArrayList<String> bean = new ArrayList<>();
                            for (int i = 0; i < statusBean.getShow_detailBeans().size(); i++) {
                                String title = statusBean.getShow_detailBeans().get(i).getTv_series_name();
                                bean.add(title);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, bean);
                            search.setAdapter(arrayAdapter);
                            search.setThreshold(1);

                            final StatusBean finalStatusBean1 = statusBean;
                            search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    String id2 = finalStatusBean1.getShow_detailBeans().get(position).getTv_series_id();
                                    String type = "tvshows";
                                    String image = finalStatusBean1.getShow_detailBeans().get(position).getTv_series_image();
                                    Log.e("img", ":-" + image);

                                    Intent intent = new Intent(getContext(), TvShow_SeasonDetail.class);
                                    intent.putExtra(Constants.SHOW_ID, id2);
                                    intent.putExtra(Constants.SHOW_TYPE, type);
                                    intent.putExtra(Constants.SHOW_IMAGE, image);
                                    getContext().startActivity(intent);

                                }
                            });

                        } else {
                            ConnectionDetector.showToast(getActivity(), statusBean.getMsg());
                        }
                    }

                    cnctn_layout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cnctn_layout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Utility.volleyErrorHandling(error, getActivity());
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjectRequestSearch);
        }
    }

    private void findViews() {

        pullToRefreshHome = (SwipeRefreshLayout) getView().findViewById(R.id.pullToRefreshTvShow);
        recyclerView = (RecyclerView) getView().findViewById(R.id.homeMoiveList_tv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2 = (RecyclerView) getView().findViewById(R.id.homeMoiveList_tv2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        alpha_grid = (GridView) getView().findViewById(R.id.alpha_grid);
        loading_layout = (RelativeLayout) getView().findViewById(R.id.loading);
        cnctn_layout = (RelativeLayout) getView().findViewById(R.id.no_internet);
        connection_layout = (LinearLayout) getView().findViewById(R.id.conctn_show);
        connection_error = (TextView) getView().findViewById(R.id.conctn_errorTvShow);
        retry = (Button) getView().findViewById(R.id.retry_tvShow);
        retry.setOnClickListener(this);

        pullToRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (connectionDetector.isConnectingToInternet()) {
                    isLayoutRefresh = true;
                    getTvShowList();
                    cnctn_layout.setVisibility(View.GONE);
                    connection_layout.setVisibility(View.VISIBLE);
                    loading_layout.setVisibility(View.VISIBLE);
                } else {
                    pullToRefreshHome.setRefreshing(false);
                    isLayoutRefresh = false;
                    ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));

                    cnctn_layout.setVisibility(View.VISIBLE);
                    connection_layout.setVisibility(View.GONE);
                    loading_layout.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getTvShowList() {
        if (getActivity() != null) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getActivity().getString(R.string.dialog_msg));
            dialog.setTitle(getActivity().getString(R.string.dialog_title));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            cnctn_layout.setVisibility(View.GONE);
            connection_layout.setVisibility(View.GONE);
            loading_layout.setVisibility(View.VISIBLE);

            if (isLayoutRefresh) {
                dialog.dismiss();
            }
            try {
                JSONObject request = new JSONObject();
                request.put("start", 0);
                request.put("limit", 6);

                CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest
                        (Request.Method.POST, Constants.tvShowResponse, request, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                TvShow_Parser parser = new TvShow_Parser();
                                StatusBean statusBean = new StatusBean();
                                statusBean = parser.getTvShowResponse(response.toString());
                                if (statusBean != null) {
                                    if (statusBean.getStatus().equals("1")) {
                                        setTvShowResult(statusBean);
                                    } else {
                                        ConnectionDetector.showToast(getActivity(), statusBean.getMsg());
                                    }
                                }
                                dialog.dismiss();
                                cnctn_layout.setVisibility(View.GONE);
                                loading_layout.setVisibility(View.GONE);
                                connection_layout.setVisibility(View.VISIBLE);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                cnctn_layout.setVisibility(View.GONE);
                                loading_layout.setVisibility(View.GONE);
                                connection_layout.setVisibility(View.VISIBLE);
                                Utility.volleyErrorHandling(error, getActivity());
                            }
                        });
                AppController.getInstance().addToRequestQueue(jsonObjectRequest);
            }catch (Exception e){
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }
    }

    private void setTvShowResult(StatusBean statusBean) {
        if (pullToRefreshHome.isRefreshing()) {
            pullToRefreshHome.setRefreshing(false);
        }

        Map<String, ArrayList<HomeMoivesBean>> tvListMap = new HashMap<>();
        tvListMap = statusBean.getHomeMovieMap();
        ArrayList<String> titleList = new ArrayList<>();

        titleList.add("LATEST ADDITIONS");
        titleList.add("LATEST TV SHOWS");


        LatestSeasonAdapter homeMovieAdapter = new LatestSeasonAdapter(tvListMap, getActivity(), titleList);
        recyclerView.setAdapter(homeMovieAdapter);

        EpisodeAdapter adapter = new EpisodeAdapter(tvListMap, getActivity(), titleList);
        recyclerView2.setAdapter(adapter);

        sortTvShowItems = new ArrayList<>();
        for (int i = 0; i < sortingText.length; i++) {
            SortTvShow_item sortItem = new SortTvShow_item(sortingText[i]);
            sortTvShowItems.add(sortItem);
            Sort_TvShow sort_tvShow = new Sort_TvShow(getContext(), sortTvShowItems);
            alpha_grid.setAdapter(sort_tvShow);
            alpha_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filter = (sortTvShowItems.get(position).getAlpha());
                    Log.e("aplhabet", ":-" + filter);
                    Intent intent1 = new Intent(getActivity(), TvShow_Category.class);
                    intent1.putExtra(Constants.SHOW_TYPE, "tvshows");
                    intent1.putExtra(Constants.TVSHOW_FILTER, filter);
                    getActivity().startActivity(intent1);

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_tvShow:
                if (connectionDetector.isConnectingToInternet()) {
                    getTvShowList();
                    loading_layout.setVisibility(View.VISIBLE);
                    cnctn_layout.setVisibility(View.GONE);
                    connection_layout.setVisibility(View.GONE);
                } else {
                    cnctn_layout.setVisibility(View.VISIBLE);
                    connection_layout.setVisibility(View.GONE);
                    loading_layout.setVisibility(View.GONE);
                    break;
                }
        }
    }
}

