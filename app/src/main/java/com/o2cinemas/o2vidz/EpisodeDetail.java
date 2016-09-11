package com.o2cinemas.o2vidz;

import android.annotation.TargetApi;
import android.app.DownloadManager;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.o2cinemas.beans.TvShow_DetailBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.DownloadItem;
import com.o2cinemas.parser.TvShow_Parser;
import com.o2cinemas.utilities.ConnectionDetector;
import com.o2cinemas.utilities.Utility;
import com.o2cinemas.volley.AppController;
import com.o2cinemas.volley.CustomJSONObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class EpisodeDetail extends AppCompatActivity implements View.OnClickListener {
    private TextView seasonName, format, cast, castDetail, genre, genreDetail, runtime, runtimeDetail, rating, ratingDetail,
            views, viewsDetail, desc, expand_desc, imdb, dwnld_seasonName, dwnld_seasonNumber,
            dwnld_episodeNumber, noDwnlMp4, noDwnlGp3;
    private ConnectionDetector connectionDetector;
    private LinearLayout descLayout, collapseLayout, collapseDesc, expandLayout, expnadDesc;
    private ImageView collapseimg, expandimg, episodeImage, blurrImage;
    private Button mp4, gp3, conctn_retry;
    private ImageLoader imageLoader;
    private RelativeLayout blurrLayout;
    private View blurrView;
    private Bitmap bitmap;
    private RelativeLayout conctn_available_tv,
            connctn_disable_tv, loadingLayout;
    private long mRequestStartTime;
    private String randomServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        connectionDetector = new ConnectionDetector(getApplicationContext());
        setRandomServer();
        findViews();
        if (connectionDetector.isConnectingToInternet()) {
            String id = getIntent().getStringExtra(Constants.EPISODE_ID);
            String season_name = getIntent().getStringExtra(Constants.SEASON_NAME);
            String episode_name = getIntent().getStringExtra(Constants.EPISODE_NAME);
//            String type1 = getIntent().getStringExtra(Constants.SHOW_TYPE);
            getEpisodeList(id);
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
        format = (TextView) findViewById(R.id.episode_format);
        seasonName = (TextView) findViewById(R.id.episode_seasonName);
        cast = (TextView) findViewById(R.id.cast_episode);
        castDetail = (TextView) findViewById(R.id.cast_nameEpisode);
        genre = (TextView) findViewById(R.id.genre_detail);
        genreDetail = (TextView) findViewById(R.id.genre_detailEpisode);
        runtime = (TextView) findViewById(R.id.runtime_episode);
        runtimeDetail = (TextView) findViewById(R.id.runtime_detailEpisode);
        rating = (TextView) findViewById(R.id.rating_episode);
        ratingDetail = (TextView) findViewById(R.id.rating_detailEpisode);
        views = (TextView) findViewById(R.id.view_episode);
        viewsDetail = (TextView) findViewById(R.id.views_detailEpisode);

        /**** blurr Background ****/
        blurrView = findViewById(R.id.blurr_episode);
        blurrImage = (ImageView) blurrView.findViewById(R.id.blurr_image);
        episodeImage = (ImageView) findViewById(R.id.episode_image);

        /*** Connection Layout ****/
        conctn_available_tv = (RelativeLayout) findViewById(R.id.cnctnAvailale_episode);
        connctn_disable_tv = (RelativeLayout) findViewById(R.id.nocnct_episode);
        conctn_retry = (Button) findViewById(R.id.nocnctn_button);
        conctn_retry.setOnClickListener(this);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_episode);

        /****** desc  **********/

        descLayout = (LinearLayout) findViewById(R.id.desc_viewEpisode);
        collapseLayout = (LinearLayout) findViewById(R.id.collapseDes);
        collapseDesc = (LinearLayout) findViewById(R.id.colapse_layout);
        desc = (TextView) findViewById(R.id.desc_episode);
        collapseimg = (ImageView) findViewById(R.id.collapse_img);
        expandLayout = (LinearLayout) findViewById(R.id.expand_layout);
        expnadDesc = (LinearLayout) findViewById(R.id.ExpandDesc_layout);
        expand_desc = (TextView) findViewById(R.id.expand_descEpisode);
        expandimg = (ImageView) findViewById(R.id.expand_img);

        expandLayout.setVisibility(View.GONE);
        collapseimg.setOnClickListener(this);
        expandimg.setOnClickListener(this);

        /****** detail of episode *******/

        imdb = (TextView) findViewById(R.id.link_episode);
        imdb.setOnClickListener(this);

        dwnld_seasonName = (TextView) findViewById(R.id.text_seasonName);
        dwnld_seasonNumber = (TextView) findViewById(R.id.text_seasonNumber);
        dwnld_episodeNumber = (TextView) findViewById(R.id.text_episodeNumber);
        noDwnlMp4 = (TextView) findViewById(R.id.noDwnld_mp4);
        noDwnlGp3 = (TextView) findViewById(R.id.noDwnld_gp3);
        mp4 = (Button) findViewById(R.id.dwnldMp4);
        gp3 = (Button) findViewById(R.id.dwnldGp3);

        imageLoader = AppController.getInstance().getImageLoader();
    }

    private void getEpisodeList(String id) {

        final ProgressDialog dialog = new ProgressDialog(EpisodeDetail.this);
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
            jsonObject.putOpt("episode_id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                Constants.tvShowEpisodeResponse, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        TvShow_Parser tvShow_parser = new TvShow_Parser();
                        TvShow_DetailBean tvShow_detailBean = new TvShow_DetailBean();
                        tvShow_detailBean = tvShow_parser.getEpisodeDetailResponse(response.toString());
                        if (tvShow_detailBean != null) {
                            if (tvShow_detailBean.getStatus().equals("1")) {
                                setEpisodeDetail(tvShow_detailBean);
                            } else {
                                ConnectionDetector.showToast(EpisodeDetail.this, tvShow_detailBean.getMsg());
                            }
                        } else {
                            ConnectionDetector.showToast(EpisodeDetail.this, getString(R.string.connection_fail));
                        }
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
                Utility.volleyErrorHandling(error, EpisodeDetail.this);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void setEpisodeDetail(final TvShow_DetailBean tvShow_detailBean) {
        seasonName.setText(tvShow_detailBean.getTv_series_name());
        castDetail.setText(tvShow_detailBean.getTv_series_cast());
        genreDetail.setText(tvShow_detailBean.getTv_series_runtime());
        runtimeDetail.setText(tvShow_detailBean.getTv_series_runtime());
        viewsDetail.setText(tvShow_detailBean.getTv_series_views());
        desc.setText(tvShow_detailBean.getTv_series_desc());
        expand_desc.setText(tvShow_detailBean.getTv_series_desc());
        imdb.setText(tvShow_detailBean.getImbd_link());


        String rating_count = tvShow_detailBean.getRating_count();
        String rating_rate = tvShow_detailBean.getRating_rate();
        float p = Integer.parseInt(rating_count);
        float r = Integer.parseInt(rating_rate);
        float rating = r / p;
        String rate = String.valueOf(new DecimalFormat("##.#").format(rating)).toString();

        ratingDetail.setText(rate);
        dwnld_seasonName.setText(tvShow_detailBean.getTv_series_name());
        noDwnlMp4.setText(tvShow_detailBean.getEpisode_beanArrayList().get(1).getFile_downloads() + " " + getString(R.string.downloads));
        noDwnlGp3.setText(tvShow_detailBean.getEpisode_beanArrayList().get(0).getFile_downloads() + " " +  getString(R.string.downloads));
//        dwnld_seasonNumber.setText(getIntent().getStringExtra(Constants.SEASON_NAME).toString() + "-");
//        dwnld_episodeNumber.setText(getIntent().getStringExtra(Constants.EPISODE_NAME).toString());
        dwnld_seasonNumber.setText(tvShow_detailBean.getSeason_name());
        dwnld_episodeNumber.setText(tvShow_detailBean.getEpisode_name());

        String image = tvShow_detailBean.getTv_series_image();
        String imageurl = Constants.imageResponseTvShow + image;
        imageurl = imageurl.replaceAll(" ", "%20");
        imageLoader.get(imageurl, ImageLoader.getImageListener(episodeImage, R.drawable.error, R.drawable.error));
        blurrImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageLoader.get(imageurl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                bitmap = response.getBitmap();
                if (bitmap != null) {
                    Bitmap blurred = blurRenderScript(getApplicationContext(), bitmap, 25);
                    blurrImage.setImageBitmap(blurred);
                } else {
                    blurrImage.setImageResource(R.drawable.no_preview);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                blurrImage.setImageResource(R.color.purple);
            }
        });

        mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String downloadUrl = randomServer + "/" + tvShow_detailBean.getEpisode_beanArrayList().get(1).getFile_path();
                downloadUrl = downloadUrl.replace(" ", "%20");

                // add to download queue if need
                if (Utility.shouldAddToDownloadQueue(EpisodeDetail.this)) {
                    // add to pending item
                    Intent intent = new Intent(Constants.ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION);
                    intent.putExtra(Constants.DOWNLOAD_URL_KEY, downloadUrl);
                    intent.putExtra(Constants.DOWNLOAD_TITLE_KEY, tvShow_detailBean.getTv_series_name());
                    sendBroadcast(intent);

                } else {
                    mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.

                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(tvShow_detailBean.getTv_series_name())
                            .setDescription(getString(R.string.dwnld_Description))
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir("/O2Vidz", tvShow_detailBean.getEpisode_name());
                    downloadManager.enqueue(request);
                }



                Log.e("mp4url", ":-" + downloadUrl);
            }
        });

        gp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gp3DownloadUrl = randomServer + "/" + tvShow_detailBean.getEpisode_beanArrayList().get(0).getFile_path();
                gp3DownloadUrl = gp3DownloadUrl.replace(" ", "%20");

                // add to download queue if need
                if (Utility.shouldAddToDownloadQueue(EpisodeDetail.this)) {
                    // add to pending item
                    Intent intent = new Intent(Constants.ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION);
                    intent.putExtra(Constants.DOWNLOAD_URL_KEY, gp3DownloadUrl);
                    intent.putExtra(Constants.DOWNLOAD_TITLE_KEY, tvShow_detailBean.getTv_series_name());
                    sendBroadcast(intent);
                } else {
                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(gp3DownloadUrl));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(tvShow_detailBean.getTv_series_name())
                            .setDescription(getString(R.string.dwnld_Description))
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir("/O2Vidz", tvShow_detailBean.getEpisode_name());
                    downloadManager.enqueue(request);
                    Log.e("3gpulr", ":-" + gp3DownloadUrl);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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

    private void setRandomServer() {
        final Random rand = new Random();
        int diceRoll = rand.nextInt(3);
        if (diceRoll == 0) {
            randomServer = Constants.TV_SERVER_1;
        } else if (diceRoll == 1) {
            randomServer = Constants.TV_SERVER_2;
        } else {
            randomServer = Constants.TV_SERVER_3;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collapse_img:
                expandLayout.setVisibility(View.VISIBLE);
                collapseLayout.setVisibility(View.GONE);
                break;
            case R.id.expand_img:
                expandLayout.setVisibility(View.GONE);
                collapseLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.nocnctn_button:
                if (connectionDetector.isConnectingToInternet()) {
                    String id = getIntent().getStringExtra(Constants.EPISODE_ID);
                    getEpisodeList(id);
                    connctn_disable_tv.setVisibility(View.GONE);
                    conctn_available_tv.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);
                } else {
                    connctn_disable_tv.setVisibility(View.VISIBLE);
                    conctn_available_tv.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.link_episode:
                String url = imdb.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

        }

    }
}
