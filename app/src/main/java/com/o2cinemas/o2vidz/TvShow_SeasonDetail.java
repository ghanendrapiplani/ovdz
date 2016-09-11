package com.o2cinemas.o2vidz;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.o2cinemas.adapters.Expandable_SeasonAdapter;
import com.o2cinemas.beans.Episode_Bean;
import com.o2cinemas.beans.SeasonBean;
import com.o2cinemas.beans.TvShow_DetailBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.GenreItem;
import com.o2cinemas.parser.TvShow_Parser;
import com.o2cinemas.utilities.ConnectionDetector;
import com.o2cinemas.utilities.Utility;
import com.o2cinemas.volley.AppController;
import com.o2cinemas.volley.CustomJSONObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TvShow_SeasonDetail extends AppCompatActivity implements View.OnClickListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener {
    private ConnectionDetector connectionDetector;
    private RelativeLayout conctn_available_tv, tool_layout_tv, title_grid_tv,
            connctn_disable_tv, loadingLayout;
    private LinearLayout tool_detail_tv;
    private LinearLayout movie_title_tv, linearDesc_tv, linearDescExpand_tv;
    private TextView format, season_name, cast, cast_detail, genre, genre_detail, runtime, runtime_detail,
            rating, rating_detail, view, view_detail, desc, desc_detail, desc_expand, idbm, idbm_detail, tv_intenet;
    private ImageView tvImage, desc_image, image_expand, blurr_image;
    RecyclerView season_recycler;
    private ImageLoader imageLoader;
    private View viewPager_Image;
    private ViewPager blurr_pager;
    private Bitmap bitmap;
    private ExpandableListView seasonList;
    //    private ArrayList<SeasonBean> seasonBeans;
    private ArrayList<Episode_Bean> episode_beans;
    private Context context;
    private Button tv_discnct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show__detail);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        findViews();
        if (connectionDetector.isConnectingToInternet()) {
            String id1 = getIntent().getStringExtra(Constants.SHOW_ID);
//            String type1 = getIntent().getStringExtra(Constants.SHOW_TYPE);
            getTvList(id1);
            loadingLayout.setVisibility(View.VISIBLE);
            connctn_disable_tv.setVisibility(View.GONE);
            conctn_available_tv.setVisibility(View.VISIBLE);
        } else {
            ConnectionDetector.showToast(getApplicationContext(), getApplicationContext().getString(R.string.connection_toast));
            connctn_disable_tv.setVisibility(View.VISIBLE);
            conctn_available_tv.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        viewPager_Image = findViewById(R.id.blurr_background_tv);

        blurr_image = (ImageView) viewPager_Image.findViewById(R.id.blurr_image);

        linearDescExpand_tv = (LinearLayout) findViewById(R.id.linearDescExpand_tv);
        linearDesc_tv = (LinearLayout) findViewById(R.id.linearDesc_tv);
        conctn_available_tv = (RelativeLayout) findViewById(R.id.conctn_available_tv);
        tool_layout_tv = (RelativeLayout) findViewById(R.id.tool_layout_tv);
        tool_detail_tv = (LinearLayout) findViewById(R.id.tool_detail_tv);
        title_grid_tv = (RelativeLayout) findViewById(R.id.title_grid_tv);
        movie_title_tv = (LinearLayout) findViewById(R.id.movie_title_tv);
        format = (TextView) findViewById(R.id.movie_format_tv);
        season_name = (TextView) findViewById(R.id.season_name);
        tvImage = (ImageView) findViewById(R.id.tv_image);
        cast = (TextView) findViewById(R.id.cast);
        cast_detail = (TextView) findViewById(R.id.cast_detail);
        genre = (TextView) findViewById(R.id.genre);
        genre_detail = (TextView) findViewById(R.id.genre_detail);
        runtime = (TextView) findViewById(R.id.runtime);
        runtime_detail = (TextView) findViewById(R.id.runtime_detail);
        rating = (TextView) findViewById(R.id.rating);
        rating_detail = (TextView) findViewById(R.id.rating_detail);
        view = (TextView) findViewById(R.id.views);
        view_detail = (TextView) findViewById(R.id.views_detail);
        desc = (TextView) findViewById(R.id.desc_tv);
        desc_detail = (TextView) findViewById(R.id.Tvdesc_text);
        desc_expand = (TextView) findViewById(R.id.desc_textTv);
        desc_image = (ImageView) findViewById(R.id.tv_viewimg);
        image_expand = (ImageView) findViewById(R.id.viewMore_imgTV);
        idbm = (TextView) findViewById(R.id.idbm);
        idbm_detail = (TextView) findViewById(R.id.idbm_detail);
        idbm_detail.setOnClickListener(this);
        connctn_disable_tv = (RelativeLayout) findViewById(R.id.connctn_disable_tv);
        tv_intenet = (TextView) findViewById(R.id.tv_intenet);
        tv_discnct = (Button) findViewById(R.id.tv_discnct);
        tv_discnct.setOnClickListener(this);
        seasonList = (ExpandableListView) findViewById(R.id.expandable_seasonList);
        seasonList.setOnGroupClickListener(this);
        seasonList.setOnGroupExpandListener(this);
//        season_recycler= (RecyclerView) findViewById(R.id.list_season);
        linearDescExpand_tv.setVisibility(View.GONE);
        desc_image.setOnClickListener(this);
        image_expand.setOnClickListener(this);

        loadingLayout = (RelativeLayout) findViewById(R.id.loading_seasonLayout);
        imageLoader = AppController.getInstance().getImageLoader();
    }

    private void getTvList(String id1) {
        final ProgressDialog dialog = new ProgressDialog(TvShow_SeasonDetail.this);
        dialog.setMessage(getString(R.string.dialog_msg));
        dialog.setTitle(getString(R.string.dialog_title));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        connctn_disable_tv.setVisibility(View.GONE);
        conctn_available_tv.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("tv_series_id", id1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST, Constants.tvShowSeasonResponse, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        TvShow_Parser tvShow_parser = new TvShow_Parser();
                        TvShow_DetailBean tvShow_detailBean = new TvShow_DetailBean();
                        tvShow_detailBean = tvShow_parser.getTvShowDetailResponse(response.toString());
                        if (tvShow_detailBean != null) {
                            if (tvShow_detailBean.getStatus().equals("1")) {
                                SetTvShowDetail(tvShow_detailBean);
                            } else {
                                ConnectionDetector.showToast(TvShow_SeasonDetail.this, tvShow_detailBean.getMsg());
                            }
                        } else {
                            ConnectionDetector.showToast(TvShow_SeasonDetail.this, getString(R.string.connection_fail));
                        }
//                        Log.e("tvResponse", ":-" + response.toString());
                        dialog.dismiss();
                        conctn_available_tv.setVisibility(View.VISIBLE);
                        loadingLayout.setVisibility(View.GONE);
                        connctn_disable_tv.setVisibility(View.GONE);
//
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                conctn_available_tv.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
                connctn_disable_tv.setVisibility(View.GONE);
                Utility.volleyErrorHandling(error, TvShow_SeasonDetail.this);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void SetTvShowDetail(TvShow_DetailBean tvShow_detailBean) {
        format.setText(getIntent().getStringExtra(Constants.SHOW_TYPE).toString());
        season_name.setText(tvShow_detailBean.getTv_series_name());
        cast_detail.setText(tvShow_detailBean.getTv_series_cast());
        runtime_detail.setText(tvShow_detailBean.getTv_series_runtime());
        view_detail.setText(tvShow_detailBean.getTv_series_views());
        desc_detail.setText(tvShow_detailBean.getTv_series_desc());
        desc_expand.setText(tvShow_detailBean.getTv_series_desc());
        idbm_detail.setText(tvShow_detailBean.getImbd_link());
        // set genre details
        String genreDetails = "";
        ArrayList<GenreItem> genreItems = tvShow_detailBean.getGenreItemArrayList();
        if (genreItems != null && genreItems.size() > 0) {
            for (int i = 0; i < genreItems.size(); i++) {
                GenreItem item = genreItems.get(i);

                if (genreItems.size() == 1) {
                    genreDetails = item.getName();
                } else if (i == genreItems.size() - 1) { //last item
                    genreDetails += item.getName();
                } else {
                    genreDetails += item.getName() + ", ";
                }
            }
        }
        genre_detail.setText(genreDetails);

        String rating_count = tvShow_detailBean.getRating_count().toString();
        String rating_rate = tvShow_detailBean.getRating_rate().toString();

        float p = Integer.parseInt(rating_count);
        float r = Integer.parseInt(rating_rate);
        float rating = r / p;

        String rate = String.valueOf(new DecimalFormat("##.#").format(rating)).toString();
        rating_detail.setText(rate);

        ArrayList<SeasonBean> seasonBeans = new ArrayList<>();
        seasonBeans = tvShow_detailBean.getSeasonBeanArrayList();
        Expandable_SeasonAdapter expandable_seasonAdapter = new Expandable_SeasonAdapter(seasonBeans, TvShow_SeasonDetail.this);
        seasonList.setAdapter(expandable_seasonAdapter);
        setListViewHeight(seasonList, -1);

        String image = getIntent().getStringExtra(Constants.SHOW_IMAGE).replace(" ", "%20");
        String type = getIntent().getStringExtra(Constants.SHOW_TYPE);
        String imageurl = Constants.imageResponseTvShow + image;

        imageLoader.get(imageurl, ImageLoader.getImageListener(tvImage, R.drawable.error, R.drawable.error));
        blurr_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageLoader.get(imageurl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                bitmap = response.getBitmap();
                if (bitmap != null) {
                    Bitmap blurred = blurRenderScript(getApplicationContext(), bitmap, 25);
                    blurr_image.setImageBitmap(blurred);
                } else {
                    blurr_image.setImageResource(R.drawable.no_preview);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                blurr_image.setImageResource(R.color.purple);
            }
        });

    }

    public static Bitmap blurRenderScript(Context context, Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;

    }

    private void setListViewHeight(ExpandableListView listView, int willExpendPos) {

        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

           if (((!listView.isGroupExpanded(i) && i == willExpendPos))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 600;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_viewimg:
                linearDesc_tv.setVisibility(View.GONE);
                linearDescExpand_tv.setVisibility(View.VISIBLE);
                break;
            case R.id.viewMore_imgTV:
                linearDesc_tv.setVisibility(View.VISIBLE);
                linearDescExpand_tv.setVisibility(View.GONE);
                break;
            case R.id.tv_discnct:
                if (connectionDetector.isConnectingToInternet()) {
                String id1 = getIntent().getStringExtra(Constants.SHOW_ID);
                getTvList(id1);
                connctn_disable_tv.setVisibility(View.GONE);
                conctn_available_tv.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);}
                else {
                    connctn_disable_tv.setVisibility(View.VISIBLE);
                    conctn_available_tv.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.idbm_detail :
                String url = idbm_detail.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;


        }

    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (seasonList != null) {
            setListViewHeight(seasonList, groupPosition);
        }
        return false;
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (seasonList != null) {
            ExpandableListAdapter listAdapter = (ExpandableListAdapter) seasonList.getExpandableListAdapter();
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                if (seasonList.isGroupExpanded(i) && i != groupPosition) {
                    seasonList.collapseGroup(i);
                }
            }
        }

    }
}

