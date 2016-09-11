package com.o2cinemas.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.o2cinemas.adapters.AlphabetSortGroupDetailAdapter;
import com.o2cinemas.adapters.GroupDetailAdapter;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.AlphabetSortItem;
import com.o2cinemas.o2vidz.R;
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
import java.util.List;

/**
 * Created by admin on 9/9/16.
 */
public class GroupDetailFragment extends Fragment implements View.OnClickListener{

    static GroupDetailFragment groupDetailFragment;
    final private int NUM_ITEMS_PAGE = 9;

    private ConnectionDetector connectionDetector;
    private TextView format, category, first, last;
    private RecyclerView groupDetailRecycler;
    private GridView sortGrid;

    private int currentPage = 0;
    private TextView[] btns;
    private int noOfBtns;
    private LinearLayout btnLayout;
    private List<AlphabetSortItem> alphabetSortItems;
    private String groupType;
    private String movieType;
    private final int totalItem = 20; //TODO need change when api is ready
    AlphabetSortGroupDetailAdapter adapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public static GroupDetailFragment getInstance() {
        if (groupDetailFragment == null) {
            groupDetailFragment = new GroupDetailFragment();
        }

        return groupDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.GROUP_TYPE_KEY)) {
            groupType = args.getString(Constants.GROUP_TYPE_KEY);
            movieType = args.getString(Constants.MOVIE_TYPE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_group_details, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        connectionDetector = new ConnectionDetector(getActivity());
        findViews();
    }

    private void findViews() {

        first = (TextView) getActivity().findViewById(R.id.first);
        first.setOnClickListener(this);
        last = (TextView) getActivity().findViewById(R.id.last);
        last.setOnClickListener(this);
        format = (TextView) getActivity().findViewById(R.id.tvshow_header);
        category = (TextView) getActivity().findViewById(R.id.tvshow_category);

        groupDetailRecycler = (RecyclerView) getActivity().findViewById(R.id.recycler_aplhaSort);
        sortGrid = (GridView) getActivity().findViewById(R.id.sort_category);
        groupDetailRecycler.setHasFixedSize(true);
        groupDetailRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        String typeTitle = "";
        String groupName = groupType;

        if (movieType != null) {
            if (movieType.equals(Constants.MP_4_TYPE)) {
                typeTitle = getString(R.string.Mp4_Movies);
            } else if (movieType.equals(Constants.GP3_TYPE)){
                typeTitle = getString(R.string.gp3_Movies);
            }else {
                typeTitle = getString(R.string.Tv_Shows);
                if (groupType.equals(Constants.LATEST_CONDITIONS_TYPE)) {
                    groupName = getString(R.string.latest_addition);
                } else {
                    groupName = getString(R.string.latest_show);
                }
            }
        }

        format.setText(typeTitle);
        category.setText(groupName);


        alphabetSortItems = new ArrayList<>();
        for (int i = 0; i < Constants.SORTING_TEXT.length; i++) {
            AlphabetSortItem sortItem = new AlphabetSortItem(Constants.SORTING_TEXT[i], false);
            alphabetSortItems.add(sortItem);
            adapter = new AlphabetSortGroupDetailAdapter(getActivity(), alphabetSortItems);

            sortGrid.setAdapter(adapter);
            sortGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlphabetSortItem item = alphabetSortItems.get(position);
                    if (item.isSelected()) {
                        item.setSelected(false);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setSelectedItem(item);
                    }
                    currentPage = 0;
                    getData(currentPage, getCurrentSelectedFilterString());
                }
            });
        }

        if (connectionDetector.isConnectingToInternet()) {
            currentPage = 0;
            getData(currentPage, null);
        } else {
            ConnectionDetector.showToast(getActivity(), getActivity().getString(R.string.connection_toast));
        }

    }


    private void getData(int page, String filterBy) {
        if (movieType != null) {
            if (movieType.equals(Constants.MP_4_TYPE) || movieType.equals(Constants.GP3_TYPE)) {
                getMp4or3gpMovies(page, filterBy);
            } else {
                getTvShowData(page, filterBy);
            }
        }
    }

    private void getMp4or3gpMovies(int page, String filterBy) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.dialog_msg));
        dialog.setTitle(getActivity().getString(R.string.dialog_title));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        try{
            JSONObject request = new JSONObject();

            if (groupType != null) {
                String type = groupType;
                if (!groupType.equals(Constants.HOLLYWOOD_TYPE)) { // if group type is not Hollywood, it will be Bollywood
                    type = Constants.BOLLYWOOD_TYPE;
                }
                request.put("cat", type);
                request.put("start", page);
                request.put("limit", NUM_ITEMS_PAGE);
                if (filterBy != null && !filterBy.isEmpty()) {
                    if (movieType != null && movieType.equals(Constants.MP_4_TYPE)) {
                        request.put("mp4_search_key", filterBy);
                    } else {
                        request.put("gp3_search_key", filterBy);
                    }
                }
            }
            // set url base on movie type
            String url = Constants.mp4MoviesByCatKey;
            if (movieType.equals(Constants.GP3_TYPE)) {
                url = Constants.gp3MoviesByCatKey;
            }
            CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                    url, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    AppParser parser = new AppParser();
                    StatusBean statusBean = new StatusBean();
                    statusBean = parser.getMp4Gor3GpGroupDetailResponse(response.toString());
                    if (statusBean != null) {
                        if (statusBean.getStatus().equals("1")) {
                            setData(statusBean);
                            addButtonFooter();
                        } else {
                            setData(null);
                            ConnectionDetector.showToast(getActivity(), statusBean.getMsg());
                        }
                    }
                    dialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Utility.volleyErrorHandling(error, getActivity());
                    currentPage --;
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        }catch (Exception e){
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }
    private void getTvShowData(int page, String filter) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.dialog_title));
        progressDialog.setMessage(getString(R.string.dialog_msg));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JSONObject jsonObject = null;
        String url = "";
        boolean isFilteringTmp = false;
        try {
            jsonObject = new JSONObject();
            url = Constants.tvShowResponse;

            if (filter != null && !filter.isEmpty()) {
                jsonObject.putOpt("filter_by", filter);
                url = Constants.tvShowAlphabetResponse;
                isFilteringTmp = true;
            } else {
                jsonObject.put("tvshow", "yes");
            }
            jsonObject.putOpt("start", String.valueOf(page));
            jsonObject.putOpt("limit", String.valueOf(NUM_ITEMS_PAGE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final boolean isFiltering = isFilteringTmp;

        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TvShow_Parser parser = new TvShow_Parser();
                StatusBean statusBean = new StatusBean();
                statusBean = parser.getTvShowGroupResponse(response.toString(), groupType, isFiltering);
                if (statusBean != null) {
                    if (statusBean.getStatus().equals("1")) {
                        setData(statusBean);
                        addButtonFooter();
                    } else {
                        setData(null);
                        ConnectionDetector.showToast(getActivity(), statusBean.getMsg());
                    }
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                Utility.volleyErrorHandling(error, getActivity());
            }

        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    private void setData(StatusBean statusBean) {
        ArrayList<HomeMoivesBean> beanArrayList = null;
        if (statusBean != null) {
            beanArrayList = statusBean.getHomeMoviesBean();
        }
        if (beanArrayList == null) {
            beanArrayList = new ArrayList<>();
        }
        GroupDetailAdapter adapter= new GroupDetailAdapter(beanArrayList, getActivity());
        groupDetailRecycler.setAdapter(adapter);
    }

    private void addButtonFooter() {
        int buttonSize = totalItem;
        int val = buttonSize % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = buttonSize / NUM_ITEMS_PAGE + val;

        LinearLayout l1 = (LinearLayout) getActivity().findViewById(R.id.btnLayout);
        l1.removeAllViews();
        btns = new TextView[noOfBtns];

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(2, 2, 2, 2);

        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new TextView(getActivity());
            btns[i].setLayoutParams(lp);
            if (i == currentPage) {
                btns[i].setBackgroundResource(R.drawable.bg_btn_2);
            } else {
                btns[i].setBackgroundResource(R.drawable.bg_btn_1);
            }

            btns[i].setTextColor(getResources().getColor(android.R.color.black));
            btns[i].setText("" + (i + 1));
            btns[i].setTextSize(12);
            l1.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (j != currentPage) {
                        currentPage = j;
                        getData(currentPage, getCurrentSelectedFilterString());
                    }
                }
            });
        }
    }

    private String getCurrentSelectedFilterString() {
        if (adapter != null) {
            AlphabetSortItem item = adapter.getSelectedItem();
            if (item != null) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                currentPage = 0;
                getData(currentPage, getCurrentSelectedFilterString());
                break;
            case R.id.last:
                int val = totalItem % NUM_ITEMS_PAGE;
                val = val == 0 ? 0 : 1;
                currentPage = totalItem / NUM_ITEMS_PAGE + val - 1;
                getData(currentPage, getCurrentSelectedFilterString());
                break;
        }
    }
}
