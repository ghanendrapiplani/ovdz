package com.o2cinemas.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.o2cinemas.adapters.HomeMovieAdapter;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.Detail_Page;
import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.parser.AppParser;
import com.o2cinemas.parser.StatusBean;
import com.o2cinemas.utilities.ConnectionDetector;
import com.o2cinemas.utilities.Utility;
import com.o2cinemas.volley.AppController;
import com.o2cinemas.volley.CustomJSONObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 5/19/2016.
 */
public class Gp3_Fragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    private ConnectionDetector connectionDetector;
    private SwipeRefreshLayout pullToRefreshHome;
    private RecyclerView recyclerView;
    private boolean isLayoutRefresh = false;
    private TextView connection_error;
    private Button retry;
    private Context context;
    private RelativeLayout cnctn_layout;
    private AutoCompleteTextView search;
    private String title;
    boolean isGotData;

    public static Gp3_Fragment gp3_fragment;

    public static Gp3_Fragment getNewInstance() {
        if (gp3_fragment == null) {
            gp3_fragment = new Gp3_Fragment();
        }
        return gp3_fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
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
                getGp3SearchResult();
            }
        });
    }

    public void getData() {
        if (!isGotData) {
            if (connectionDetector.isConnectingToInternet()) {
                get3GPMovieList();
                cnctn_layout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));
                cnctn_layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            isGotData = true;
        }

    }
    private void getGp3SearchResult() {
        Activity activity = getActivity();
        if (activity != null && ((MainActivity)activity).getCurrentTabPosition() == Constants.GP3_TAB_POSITION) {
            cnctn_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            title = search.getText().toString().trim();

            Log.e("stm", ":-" + title);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.putOpt("gp3_search_key", title);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                CustomJSONObjectRequest jsonObjectRequestSearch = new CustomJSONObjectRequest(Request.Method.POST,
                        Constants.searchGp3, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppParser parser = new AppParser();
                        StatusBean statusBean = new StatusBean();
                        statusBean = parser.get3gpSearchResponse(response.toString());
                        if (statusBean != null) {
                            if (statusBean.getStatus().equals("1")) {
                                final ArrayList<String> bean = new ArrayList<>();
                                for (int i = 0; i < statusBean.getDetailBeans().size(); i++) {
                                    String title = statusBean.getDetailBeans().get(i).getTitle();

                                    bean.add(title);
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, bean);
                                search.setAdapter(arrayAdapter);
                                search.setThreshold(1);


                                final StatusBean finalStatusBean1 = statusBean;
                                search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
                                        String id2 = finalStatusBean1.getDetailBeans().get(position).getId();
                                        String type = finalStatusBean1.getDetailBeans().get(position).getType();
                                        String image = finalStatusBean1.getDetailBeans().get(position).getSsname();
                                        Log.e("img", ":-" + image);

                                        Intent intent = new Intent(getContext(), Detail_Page.class);
                                        intent.putExtra(Constants.MOVIE_ID, id2);
                                        intent.putExtra(Constants.MOVIE_TYPE, type);
                                        intent.putExtra(Constants.MOVIE_IMAGE, image);
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

            }catch (Exception e){}
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
                    get3GPMovieList();
                    cnctn_layout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    pullToRefreshHome.setRefreshing(false);
                    isLayoutRefresh = false;
                    ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));
                    cnctn_layout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void get3GPMovieList() {
        Log.d("test", "get gp3 movies");
        if (getActivity() != null) {
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

            try {
                JSONObject request = new JSONObject();
                request.put("start", 0);
                request.put("limit", 6);
                CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST, Constants.gp3Response, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppParser parser = new AppParser();
                        StatusBean statusBean = new StatusBean();
                        statusBean = parser.getMp4Response(response.toString(), "movies_3gp_data");
                        if (statusBean != null){
                            if (statusBean.getStatus().equals("1")){
                                setResultOnViews(statusBean);
                            }else {
                                ConnectionDetector.showToast(getActivity(),statusBean.getMsg());
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

            }catch (Exception e){
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }
    }

    private void setResultOnViews(StatusBean statusBean) {
        if (pullToRefreshHome.isRefreshing()) {
            pullToRefreshHome.setRefreshing(false);
        }

        Map<String, ArrayList<HomeMoivesBean>> listMap = new HashMap<>();
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("Latest Additions");
        titleList.add("Bollywood");
        titleList.add("Hollywood");
        titleList.add("Hindi Dubbed");
        titleList.add("Wrestling");
        titleList.add("Others");
        listMap = statusBean.getHomeMovieMap();
        HomeMovieAdapter adapter = new HomeMovieAdapter(this, listMap, getActivity(),titleList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_retry_error:
                if (connectionDetector.isConnectingToInternet()) {
                    get3GPMovieList();
                } else {
                    cnctn_layout.setVisibility(View.VISIBLE);
                    break;
                }}

    }
}
