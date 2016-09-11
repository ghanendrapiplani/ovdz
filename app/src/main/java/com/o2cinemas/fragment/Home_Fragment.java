package com.o2cinemas.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.o2cinemas.adapters.HomeMoiveItemAdapter;
import com.o2cinemas.adapters.HomeMovieAdapter;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.o2vidz.RecyclerItemClickListner;
import com.o2cinemas.parser.AppParser;
import com.o2cinemas.parser.StatusBean;
import com.o2cinemas.utilities.ConnectionDetector;
import com.o2cinemas.utilities.Utility;
import com.o2cinemas.volley.AppController;
import com.o2cinemas.volley.CustomJSONObjectRequest;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home_Fragment extends Fragment implements View.OnClickListener{

    private ConnectionDetector connectionDetector;
    private SwipeRefreshLayout pullToRefreshHome;
    private RecyclerView recyclerView;
    private boolean isLayoutRefresh = false;
    private View view;
    private TextView connection_error;
    private Button retry;
    private Context context;
    private RelativeLayout cnctn_layout;
    public static Home_Fragment home_fragment;

    public static Home_Fragment getNewInstance() {
        if (home_fragment == null) {
            home_fragment = new Home_Fragment();
        }

        return home_fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        connection_error = (TextView) getView().findViewById(R.id.conctn_text);
//        retry = (Button) getView().findViewById(R.id.button_retry);

        connectionDetector = new ConnectionDetector(getActivity());
        findViews();
        if (connectionDetector.isConnectingToInternet()) {
            getHomeMovieList();
            cnctn_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));
            cnctn_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void findViews() {


        pullToRefreshHome = (SwipeRefreshLayout) getView().findViewById(R.id.pullToRefreshHome);
        recyclerView = (RecyclerView) getView().findViewById(R.id.homeMoiveList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cnctn_layout = (RelativeLayout) getView().findViewById(R.id.cnctn_layout);
        connection_error = (TextView) getView().findViewById(R.id.conctn_text_error);
        retry = (Button) getView().findViewById(R.id.button_retry_error);
        retry.setOnClickListener(this);

        pullToRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (connectionDetector.isConnectingToInternet()) {
                    isLayoutRefresh = true;
                    getHomeMovieList();
                    cnctn_layout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);



                } else {
                    pullToRefreshHome.setRefreshing(false);
                    isLayoutRefresh = false;
                    ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));
//
                    cnctn_layout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getHomeMovieList() {
        Log.d("test", "get home movies");
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.dialog_msg));
        dialog.setTitle(getActivity().getString(R.string.dialog_title));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        cnctn_layout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (isLayoutRefresh) {
            dialog.dismiss();
        }

        try{
            JSONObject request = new JSONObject();
            request.put("start", 0);
            request.put("limit", 6);
            CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST, Constants.homeResponse, request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            AppParser parser = new AppParser();
                            StatusBean statusBean = new StatusBean();
                            statusBean = parser.getHomeMoviesResponse(response.toString());
                            if (statusBean != null) {
                                if (statusBean.getStatus().equals("1")) {
                                    setResultOnViews(statusBean);
                                } else {
                                    ConnectionDetector.showToast(getActivity(), statusBean.getMsg());
                                }
                            }
                            dialog.dismiss();
                            cnctn_layout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    cnctn_layout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Utility.volleyErrorHandling(error, getActivity());
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }catch(Exception e){
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    private void setResultOnViews(final StatusBean statusBean) {

        if (pullToRefreshHome.isRefreshing()) {
            pullToRefreshHome.setRefreshing(false);
        }

        Map<String, ArrayList<HomeMoivesBean>> listMap = new HashMap<>();
        final ArrayList<String> titleList = new ArrayList<>();
        titleList.add("MP4 Movies");
        titleList.add("3GP Movies");
        titleList.add("TV SHOWS");

        listMap = statusBean.getHomeMovieMap();
        final HomeMovieAdapter adapter = new HomeMovieAdapter(this, listMap, getActivity(), titleList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_retry_error:
                if (connectionDetector.isConnectingToInternet()) {
                    getHomeMovieList();
                } else {
                    cnctn_layout.setVisibility(View.VISIBLE);
                    break;
                }
        }
    }}



