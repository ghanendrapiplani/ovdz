package com.o2cinemas.o2vidz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.o2cinemas.adapters.SeasonAdapter;
import com.o2cinemas.adapters.Sort_TvShow;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.beans.SortTvShow_item;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.navigation.About;
import com.o2cinemas.navigation.NavigationAdapter;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TvShow_Category extends AppCompatActivity implements View.OnClickListener {
    private TextView format, category, first, last, cnctn_text;
    private Button cnctn_button;
    private RecyclerView tvshowRecycler;
    private GridView sortGrid;
    private ConnectionDetector connectionDetector;
    private ArrayList<HomeMoivesBean> beanArrayList = new ArrayList<>();
    private AutoCompleteTextView searchView;
    private ImageView logo;
    private Toolbar toolbar;
    private RelativeLayout cnctnAvailable, cnctnDisable, loadingLayout;
    final private int NUM_ITEMS_PAGE = 9;
    private int start = 0;
    private int limit = 0;
    private TextView[] btns;
    private int noOfBtns;
    private LinearLayout btnLayout;
    private NavigationAdapter navigationAdapter;
    private ActionBarDrawerToggle toggle;
    private List<SortTvShow_item> sortTvShowItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private String sortingText[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0-9"};
    private String title;
    final private int loadfirst = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private int selectedPageNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show__category);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        connectionDetector = new ConnectionDetector(TvShow_Category.this);
        findViews();
        limit = NUM_ITEMS_PAGE;

        if (connectionDetector.isConnectingToInternet()) {

            String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
            String filter = getIntent().getStringExtra(Constants.TVSHOW_FILTER);
            getImagelist(type, filter, String.valueOf(start), String.valueOf(limit));
            loadingLayout.setVisibility(View.VISIBLE);
            cnctnAvailable.setVisibility(View.VISIBLE);
            cnctnDisable.setVisibility(View.GONE);}
        else {
            ConnectionDetector.showToast(TvShow_Category.this, getApplicationContext().getString(R.string.connection_toast));
            cnctnAvailable.setVisibility(View.GONE);
            cnctnDisable.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        }}


    private void findViews() {

        View toolbar_view = findViewById(R.id.toolbar_tvshowCategory);
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_);

        logo = (ImageView) toolbar_view.findViewById(R.id.logo);
        searchView = (AutoCompleteTextView) toolbar_view.findViewById(R.id.searchIcon);
        searchView.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchResult();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutCategory);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frameContainer);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        first = (TextView) findViewById(R.id.first);
        first.setOnClickListener(this);
        last = (TextView) findViewById(R.id.last);
        last.setOnClickListener(this);

        cnctnAvailable = (RelativeLayout) findViewById(R.id.cnctnAvailable_categoryTv);
        cnctnDisable = (RelativeLayout) findViewById(R.id.cnctn_categoryTv);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_categoryTv);
        cnctn_text = (TextView) findViewById(R.id.intenettext_tv);
        cnctn_button = (Button) findViewById(R.id.button_discnctTvCategory);
        cnctn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
                    String filter = getIntent().getStringExtra(Constants.TVSHOW_FILTER);
                    getImagelist(type, filter, String.valueOf(start), String.valueOf(limit));
                } else {
                    ConnectionDetector.showToast(TvShow_Category.this, getApplicationContext().getString(R.string.connection_toast));
                }

            }
        });

        format = (TextView) findViewById(R.id.tvshow_header);
        category = (TextView) findViewById(R.id.tvshow_category);

        tvshowRecycler = (RecyclerView) findViewById(R.id.recycler_aplhaSort);
        sortGrid = (GridView) findViewById(R.id.sort_category);
        tvshowRecycler.setHasFixedSize(true);
        tvshowRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        sortTvShowItems = new ArrayList<>();
        for (int i = 0; i < sortingText.length; i++) {
            SortTvShow_item sortItem = new SortTvShow_item(sortingText[i]);
            sortTvShowItems.add(sortItem);
            Sort_TvShow sort_tvShow = new Sort_TvShow(TvShow_Category.this, sortTvShowItems);
            sortGrid.setAdapter(sort_tvShow);
            sortGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filter = (sortTvShowItems.get(position).getAlpha());
                    Log.e("aplhabet", ":-" + filter);
                    Intent intent1 = new Intent(getApplicationContext(), TvShow_Category.class);
                    intent1.putExtra(Constants.SHOW_TYPE, "tvshows");
                    intent1.putExtra(Constants.TVSHOW_FILTER, filter);
                    startActivity(intent1);

                }
            });

            navigationAdapter = new NavigationAdapter(this);
            drawerList.setAdapter(navigationAdapter);
            Log.e("drawerList", ":-" + navigationAdapter);


            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }


                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawerLayout.addDrawerListener(toggle);

            drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedItemPosition(position);
                }

                private void selectedItemPosition(int position) {
                    drawerLayout.closeDrawers();
                    switch (position) {
                        case 0:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container,
//                                new NavigationFavourite()).commit();
                            break;
                        case 1:

                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container, new FeedBack()).commit();
//                        break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:

                            break;
                        case 8:
                            Intent intent8 = new Intent(getApplicationContext(), About.class);
                            startActivity(intent8);
                            break;
                        case 9:
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                    }
                }
            });


        }
    }

    private void getSearchResult() {

        title = searchView.getText().toString().trim();

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
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TvShow_Category.this, android.R.layout.simple_list_item_1, bean);
                        searchView.setThreshold(1);
                        searchView.setAdapter(arrayAdapter);

//                        setResultOnSearch(statusBean);

                        final StatusBean finalStatusBean1 = statusBean;
                        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
                                String id2 = finalStatusBean1.getShow_detailBeans().get(position).getTv_series_id();
                                String type = "tvshows";
                                String image = finalStatusBean1.getShow_detailBeans().get(position).getTv_series_image();

                                Intent intent = new Intent(TvShow_Category.this, TvShow_SeasonDetail.class);
                                intent.putExtra(Constants.SHOW_ID, id2);
                                intent.putExtra(Constants.SHOW_TYPE, type);
                                intent.putExtra(Constants.SHOW_IMAGE, image);
                                startActivity(intent);

                            }
                        });


                    } else {
                        ConnectionDetector.showToast(TvShow_Category.this, statusBean.getMsg());
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utility.volleyErrorHandling(error, TvShow_Category.this);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequestSearch);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//

    private void getImagelist(String type, String filter, String start, String limit) {
        final ProgressDialog progressDialog = new ProgressDialog(TvShow_Category.this);
        progressDialog.setTitle(getString(R.string.dialog_title));
        progressDialog.setMessage(getString(R.string.dialog_msg));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadingLayout.setVisibility(View.VISIBLE);
        cnctnAvailable.setVisibility(View.GONE);
        cnctnDisable.setVisibility(View.GONE);


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("filter_by", filter);
            jsonObject.putOpt("start", start);
            jsonObject.putOpt("limit", limit);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest jsonObjectRequest1 = new CustomJSONObjectRequest(Request.Method.POST,
                Constants.tvShowAlphabetResponse, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.e("AlphabetResponse", ":-" + response);
                TvShow_Parser parser = new TvShow_Parser();
                StatusBean statusBean = new StatusBean();
                statusBean = parser.getTvAlphabetResponse(response.toString());
                if (statusBean != null) {
                    if (statusBean.getStatus().equals("1")) {
                        setTvShowAlphabet(statusBean);
                    } else {
                        ConnectionDetector.showToast(TvShow_Category.this, statusBean.getMsg());
                    }
                }
                progressDialog.dismiss();
                cnctnAvailable.setVisibility(View.VISIBLE);
                cnctnDisable.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                cnctnAvailable.setVisibility(View.VISIBLE);
                cnctnDisable.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);

                Utility.volleyErrorHandling(error, TvShow_Category.this);
            }

        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest1);


        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.putOpt("filter_by", filter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                Constants.tvShowAlphabetResponse, jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TvShow_Parser parser = new TvShow_Parser();
                StatusBean statusBean = new StatusBean();
                statusBean = parser.getTvAlphabetResponse(response.toString());
                if (statusBean != null) {
                    if (statusBean.getStatus().equals("1")) {
                        setTvShowButton(statusBean);
                    } else {
                        ConnectionDetector.showToast(TvShow_Category.this, statusBean.getMsg());
                    }
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.volleyErrorHandling(error, TvShow_Category.this);
            }

        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void setTvShowButton(StatusBean statusBean) {
        Map<String, ArrayList<HomeMoivesBean>> tvMap = new HashMap<>();

        tvMap = statusBean.getHomeMovieMap();

        Set set = tvMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            beanArrayList = tvMap.get(iterator.next());
        }
//        int buttonSize = beanArrayList.size();
        buttonFooter();
//      BackGroud(0);

    }

    private void setTvShowAlphabet(StatusBean statusBean) {
//        format.setText(getIntent().getStringExtra(Constants.SHOW_TYPE.toString()));
        format.setText(R.string.Tv_Shows);
        category.setText(getIntent().getStringExtra(Constants.TVSHOW_FILTER.toString()));


        Map<String, ArrayList<HomeMoivesBean>> tvMap = new HashMap<>();
        ArrayList<HomeMoivesBean> beanArrayList = new ArrayList<>();
        tvMap = statusBean.getHomeMovieMap();

//
        Set set = tvMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            beanArrayList = tvMap.get(iterator.next());
        }
        if (beanArrayList.size() >0){
            SeasonAdapter seasonAdapter = new SeasonAdapter(beanArrayList, TvShow_Category.this);
            tvshowRecycler.setAdapter(seasonAdapter);
        } else {
        Toast.makeText(TvShow_Category.this , "no data" ,Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(this,MainActivity.class );
        startActivity(intent);
    }}


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                loadFirst(loadfirst);
                break;

            case R.id.last:
                loadLast();

                break;

        }
    }

    private void loadLast() {
        if (btns != null && btns.length > 0) {
            start = (btns.length - 1)  * NUM_ITEMS_PAGE;
            if (connectionDetector.isConnectingToInternet()) {
                selectedPageNumber = btns.length - 1;
                String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
                String filter = getIntent().getStringExtra(Constants.TVSHOW_FILTER);
                getImagelist(type, filter, String.valueOf(start), String.valueOf(limit));
            } else {
                ConnectionDetector.showToast(TvShow_Category.this, getApplicationContext().getString(R.string.connection_toast));
            }
        }
    }


    private void buttonFooter() {
        int buttonSize = beanArrayList.size();
        int val = buttonSize % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = buttonSize / NUM_ITEMS_PAGE + val;


        LinearLayout l1 = (LinearLayout) findViewById(R.id.btnLayout);
        l1.removeAllViews();
        btns = new TextView[noOfBtns];
//     for (int k = 0; k <noOfBtns; k++){
//         btns[k] = new Button(this);
//         btns[k].setTextColor(getResources().getColor(android.R.color.black));
//         btns[k].setText("" + (k + 1));
//         Log.e("noOf", ":-" + noOfBtns);
//     }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(2, 2, 2, 2);

        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new TextView(this);
            btns[i].setLayoutParams(lp);
            if (i == selectedPageNumber) {
                btns[i].setBackgroundResource(R.drawable.bg_btn_2);
            } else {
                btns[i].setBackgroundResource(R.drawable.bg_btn_1);
            }

            btns[i].setTextColor(getResources().getColor(android.R.color.black));
            btns[i].setText("" + (i + 1));
            l1.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (j != selectedPageNumber) {
                        loadList(j);
                        CheckBtnBackGroud(j);
                    }
                }
            });
        }
    }

    private void loadFirst(int number) {
        start = number * NUM_ITEMS_PAGE;

        if (connectionDetector.isConnectingToInternet()) {
            selectedPageNumber = number;
            String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
            String filter = getIntent().getStringExtra(Constants.TVSHOW_FILTER);
            getImagelist(type, filter, String.valueOf(start), String.valueOf(limit));
        } else {
            ConnectionDetector.showToast(TvShow_Category.this, getApplicationContext().getString(R.string.connection_toast));
        }
    }

    private void loadList(int number) {
        start = number * 9;
//        for (int i = start; i < (start) + 9; i++) {
            if (connectionDetector.isConnectingToInternet()) {
                selectedPageNumber = number;
                String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
                String filter = getIntent().getStringExtra(Constants.TVSHOW_FILTER);
                getImagelist(type, filter, String.valueOf(start), String.valueOf(limit));
            } else {
                ConnectionDetector.showToast(TvShow_Category.this, getApplicationContext().getString(R.string.connection_toast));
//
            }
//        }
    }


    private void CheckBtnBackGroud(int index) {

        btns[index].setText("" + (index + 1));
        for (int i = 0; i < noOfBtns; i++) {

            Log.e("noOfButtons", ":-" + noOfBtns);
            if (index == i) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.tvshow_sort_background));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }


    }



}





