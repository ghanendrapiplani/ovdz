package com.o2cinemas.o2vidz;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.o2cinemas.adapters.Detail_Adapter;
import com.o2cinemas.adapters.ViewPager_Adapter;
import com.o2cinemas.beans.Detail_Bean;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.DownloadItem;
import com.o2cinemas.models.PartDetails;
import com.o2cinemas.parser.AppParser;
import com.o2cinemas.utilities.ConnectionDetector;
import com.o2cinemas.utilities.DialogFactory;
import com.o2cinemas.utilities.Utility;
import com.o2cinemas.volley.AppController;
import com.o2cinemas.volley.CustomJSONObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class Detail_Page extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout title_grid;
    private LinearLayout movie_title, desc_grid, linearDesc, desc_layout;
    private TextView format, category, name, movieCategory, categoryDetail, legth, length_detail, parts, part_detail,
            download, download_detail, description, description_text, dwnld_title, expandtext;
    private ImageView arrow, img, viewMore_image, blurr_image, expand_img;
    private LinearLayout expandDesc, textDesc;
    private View viewPager_Image;
    private Button donloadButton, button_discnct;
    private ConnectionDetector connectionDetector;
    private ImageLoader imageLoader;
    private Bitmap bitmap;
    private RecyclerView partsRecycler;
    private Context context;
    private ArrayList<HomeMoivesBean> beanArrayList;
    private RelativeLayout cnctnAvailable, cnctnDisable, loadingLayout;
    private BroadcastReceiver receiverNotificationClicked;
    private BroadcastReceiver receiverDownloadComplete;
    private long myDownloadRefrence;
    private DownloadManager downloadManager;
    final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
    private long mRequestStartTime;
    private String randomServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__page);

        setRandomServer();
        connectionDetector = new ConnectionDetector(Detail_Page.this);
        findviews();

        if (connectionDetector.isConnectingToInternet()) {

            String id = getIntent().getStringExtra(Constants.MOVIE_ID);
            String type = getIntent().getStringExtra(Constants.MOVIE_TYPE);

            getDetailList(id, type);
            loadingLayout.setVisibility(View.VISIBLE);
            cnctnAvailable.setVisibility(View.VISIBLE);
            cnctnDisable.setVisibility(View.GONE);

        } else {
            ConnectionDetector.showToast(Detail_Page.this, getApplicationContext().getString(R.string.connection_toast));
            cnctnAvailable.setVisibility(View.GONE);
            cnctnDisable.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        }
    }

    private void findviews() {
        viewPager_Image = findViewById(R.id.blurr_background);
        blurr_image = (ImageView) viewPager_Image.findViewById(R.id.blurr_image);

        loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        expandDesc = (LinearLayout) findViewById(R.id.linearDescExpand);
        textDesc = (LinearLayout) findViewById(R.id.Expanddesc_layout);
        expandtext = (TextView) findViewById(R.id.desc_text);
        expand_img = (ImageView) findViewById(R.id.viewMore_img);
        cnctnAvailable = (RelativeLayout) findViewById(R.id.conctn_available);
        cnctnDisable = (RelativeLayout) findViewById(R.id.connctn_disable);
        title_grid = (RelativeLayout) findViewById(R.id.title_grid);
        movie_title = (LinearLayout) findViewById(R.id.movie_title);
        format = (TextView) findViewById(R.id.movie_format);
        category = (TextView) findViewById(R.id.movie_category);
        name = (TextView) findViewById(R.id.movie_name);
        movieCategory = (TextView) findViewById(R.id.category);
        categoryDetail = (TextView) findViewById(R.id.category_detail);
        arrow = (ImageView) findViewById(R.id.movie_arrow);
        img = (ImageView) findViewById(R.id.movie_image);
        legth = (TextView) findViewById(R.id.length);
        length_detail = (TextView) findViewById(R.id.length_detail);
        parts = (TextView) findViewById(R.id.parts);
        part_detail = (TextView) findViewById(R.id.parts_detail);
        download = (TextView) findViewById(R.id.download);
        download_detail = (TextView) findViewById(R.id.download_detail);
        desc_grid = (LinearLayout) findViewById(R.id.desc_grid);
        linearDesc = (LinearLayout) findViewById(R.id.linearDesc);
        desc_layout = (LinearLayout) findViewById(R.id.desc_layout);
        description = (TextView) findViewById(R.id.description);
        viewMore_image = (ImageView) findViewById(R.id.viewMore_image);
        description_text = (TextView) findViewById(R.id.description_text);
        dwnld_title = (TextView) findViewById(R.id.dwnld_title);
        donloadButton = (Button) findViewById(R.id.buttonDownload);
        donloadButton.setOnClickListener(this);
        partsRecycler = (RecyclerView) findViewById(R.id.download_list);
        viewMore_image.setOnClickListener(this);
        expand_img.setOnClickListener(this);
        expandDesc.setVisibility(View.GONE);
        button_discnct = (Button) findViewById(R.id.button_discnct);
        button_discnct.setOnClickListener(this);

        partsRecycler.hasFixedSize();
        partsRecycler.setLayoutManager(new LinearLayoutManager(this));
        imageLoader = AppController.getInstance().getImageLoader();

    }

    private void getDetailList(String id, String type) {
        final ProgressDialog progressDialog = new ProgressDialog(Detail_Page.this);
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
            jsonObject.putOpt("movie_id", id);
            jsonObject.putOpt("movie_type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                Constants.detailResponse, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppParser appParser = new AppParser();
                        Detail_Bean detailBean = new Detail_Bean();
                        detailBean = appParser.getDetailResponse(response.toString());
                        if (detailBean != null) {
                            if (detailBean.getStatus().equals("1")) {
                                setDetailOnViews(detailBean);

                            } else {
                                ConnectionDetector.showToast(Detail_Page.this, detailBean.getMsg());
                            }
                        }
                        progressDialog.dismiss();
                        cnctnAvailable.setVisibility(View.VISIBLE);
                        cnctnDisable.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utility.volleyErrorHandling(error, Detail_Page.this);
                        progressDialog.dismiss();
                        cnctnAvailable.setVisibility(View.VISIBLE);
                        cnctnDisable.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);

                    }
                });
        AppController.getInstance().addToRequestQueue(customJSONObjectRequest);
    }

    private void setDetailOnViews(final Detail_Bean bean) {
        format.setText(getIntent().getStringExtra(Constants.MOVIE_TYPE).toString());
        name.setText(bean.getTitle());
        categoryDetail.setText(bean.getCategory());
        length_detail.setText(bean.getLength());
        download_detail.setText(bean.getDownload());
        category.setText(bean.getCategory());
        description_text.setText(bean.getDescription());
        expandtext.setText(bean.getDescription());
        ArrayList<PartDetails> partList = new ArrayList<>();

        if (bean.getPartsList().size() > 0) {
            for (int i = 0; i < bean.getPartsList().size(); i++) {
                String path = bean.getPartsList().get(i);
                if (!path.matches("")) {
                    partList.add(new PartDetails(bean.getTitle() + " Part - " + (i + 1), path, false));
                }
            }
            Detail_Adapter adapter = new Detail_Adapter(Detail_Page.this, partList);
            partsRecycler.setAdapter(adapter);
            int partsSize = partList.size();

            part_detail.setText(" " + partsSize);
        }
        String imageUrl = "";
        String image = getIntent().getStringExtra(Constants.MOVIE_IMAGE).replace(" ", "%20");
        String type = getIntent().getStringExtra(Constants.MOVIE_TYPE);
        if (type.equalsIgnoreCase("mp4Movies")) {
            imageUrl = Constants.imageResponseMp4 + image;
        } else if (type.equalsIgnoreCase("3gpMovies")) {
            imageUrl = Constants.imageResponseGp3 + image + imageUrl.replace(" ", "%20");
        }

        donloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail_Adapter adapter = (Detail_Adapter) partsRecycler.getAdapter();
                if (adapter != null) {
                    PartDetails selectedItem = adapter.getSelectedItem();
                    if (selectedItem != null) {
                        String path = selectedItem.getPath();

                        String movieDownloadUrl = randomServer + "/" + bean.getCategory()
                                + "/" + bean.getFoldername() + "/"
                                + path;
                        movieDownloadUrl = movieDownloadUrl.replace(" ", "%20");

                        // add to download queue if need
                        if (Utility.shouldAddToDownloadQueue(Detail_Page.this)) {
                            // add to pending item
                            Intent intent = new Intent(Constants.ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION);
                            intent.putExtra(Constants.DOWNLOAD_URL_KEY, movieDownloadUrl);
                            intent.putExtra(Constants.DOWNLOAD_TITLE_KEY, bean.getTitle());
                            sendBroadcast(intent);

                        } else {
                            mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.

                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(movieDownloadUrl));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                    .setAllowedOverRoaming(false)
                                    .setTitle(bean.getTitle())
                                    .setDescription("Downloading via O2vidz..")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    .setDestinationInExternalPublicDir("/O2Vidz", bean.getTitle());

                            downloadManager.enqueue(request);
                        }
                    }
                }
            }
        });

        imageLoader.get(imageUrl, ImageLoader.getImageListener(img, R.drawable.error, R.drawable.error));
        blurr_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                bitmap = response.getBitmap();
                if (bitmap != null) {
                    Bitmap blurred = blurRenderScript(getApplicationContext(), bitmap, 25);
                    blurr_image.setImageBitmap(blurred);
                } else {
//                    viewPagerImage.setImageResource(R.drawable.image_background);
                    blurr_image.setImageResource(R.drawable.no_preview);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                blurr_image.setImageResource(R.color.purple);
                Log.e("error", ":" + error.toString());
            }
        });
    }

    private void setRandomServer() {
        final Random rand = new Random();
        int diceRoll = rand.nextInt(4);
        switch (diceRoll) {
            case 0:
                randomServer = Constants.SERVER_1;
                break;
            case 1:
                randomServer = Constants.SERVER_2;
                break;
            case 2:
                randomServer = Constants.SERVER_3;
                break;
            case 3:
                randomServer = Constants.SERVER_4;
                break;
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewMore_image:
                linearDesc.setVisibility(View.GONE);
                expandDesc.setVisibility(View.VISIBLE);
                break;

            case R.id.viewMore_img:
                linearDesc.setVisibility(View.VISIBLE);
                expandDesc.setVisibility(View.GONE);
                break;

            case R.id.button_discnct:

                if (connectionDetector.isConnectingToInternet()) {
                    String id = getIntent().getStringExtra(Constants.MOVIE_ID);
                    String type = getIntent().getStringExtra(Constants.MOVIE_TYPE);
                    getDetailList(id, type);
                    loadingLayout.setVisibility(View.VISIBLE);
                    cnctnAvailable.setVisibility(View.GONE);
                    cnctnDisable.setVisibility(View.GONE);
                } else {
                    cnctnAvailable.setVisibility(View.GONE);
                    cnctnDisable.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                }
                break;

        }
    }


//
/*    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        receiverNotificationClicked = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String extraId = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                long[] refrences = intent.getLongArrayExtra(extraId);
                for (long refrence : refrences) {
                    if (refrence == myDownloadRefrence) {

                    }
                }

            }
        };
        registerReceiver(receiverNotificationClicked, filter);

        IntentFilter filter1 = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiverDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long refrence = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (myDownloadRefrence == refrence) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(refrence);
                    Cursor cursor = downloadManager.query(query);

                    cursor.moveToFirst();
                    *//** get the status of the download ***//*
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);

                    int filterNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    String savedFilePath = cursor.getString(filterNameIndex);

                    *//** get the reson- more detail on status ***//*
                    int coloumnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    int reason = cursor.getInt(coloumnReason);
                    String statusText = "";
                    String reasonText = "";
                    switch (status) {
                        case DownloadManager.STATUS_FAILED:
                            statusText = "STATUS_FAILED";
                            switch (reason) {
                                case DownloadManager.ERROR_CANNOT_RESUME:
                                    reasonText = "ERROR_CANNOT_RESUME";
                                    break;
                                case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                    reasonText = "ERROR_DEVICE_NOT_FOUND";
                                    break;
                                case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                    reasonText = "ERROR_FILE_ALREADY_EXISTS";
                                    break;
                                case DownloadManager.ERROR_FILE_ERROR:
                                    reasonText = "ERROR_FILE_ERROR";
                                    break;
                                case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                    reasonText = "ERROR_HTTP_DATA_ERROR";
                                    break;
                                case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                    reasonText = "ERROR_INSUFFICIENT_SPACE";
                                    break;
                                case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                    reasonText = "ERROR_TOO_MANY_REDIRECTS";
                                    break;
                                case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                    reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                                    break;
                                case DownloadManager.ERROR_UNKNOWN:
                                    reasonText = "ERROR_UNKNOWN";
                                    break;
                            }
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            statusText = "STATUS_PAUSED";
                            switch (reason) {
                                case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                    reasonText = "PAUSED_QUEUED_FOR_WIFI";
                                    break;
                                case DownloadManager.PAUSED_UNKNOWN:
                                    reasonText = "PAUSED_UNKNOWN";
                                    break;
                                case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                    reasonText = "PAUSED_WAITING_FOR_NETWORK";
                                    break;
                                case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                    reasonText = "PAUSED_WAITING_TO_RETRY";
                                    break;
                            }
                            Toast toast = Toast.makeText(Detail_Page.this,
                                    statusText + "\n" +
                                            reasonText,
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 25, 400);
                            toast.show();
                    }
                }
            }
        };*/

/*}
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverDownloadComplete);
        unregisterReceiver(receiverNotificationClicked);
    }*/

    /* @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        CheckDwnloadStatus();

        IntentFilter intentFilter
                = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            CheckDwnloadStatus();
        }
    };

    private void CheckDwnloadStatus() {

        // TODO Auto-generated method stub
        DownloadManager.Query query = new DownloadManager.Query();
        long id = preferenceManager.getLong(strPref_Download_ID, 0);
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    String failedReason = "";
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            failedReason = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            failedReason = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            failedReason = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            failedReason = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            failedReason = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            failedReason = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            failedReason = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            failedReason = "ERROR_UNKNOWN";
                            break;
                    }

                    Toast.makeText(Detail_Page.this,
                            "FAILED: " + failedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    String pausedReason = "";

                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            pausedReason = "PAUSED_QUEUED_FOR_WIFI";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            pausedReason = "PAUSED_UNKNOWN";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            pausedReason = "PAUSED_WAITING_FOR_NETWORK";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            pausedReason = "PAUSED_WAITING_TO_RETRY";
                            break;
                    }

                    Toast.makeText(Detail_Page.this,
                            "PAUSED: " + pausedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    Toast.makeText(Detail_Page.this,
                            "PENDING",
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Toast.makeText(Detail_Page.this,
                            "RUNNING",
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:

                    Toast.makeText(Detail_Page.this,
                            "SUCCESSFUL",
                            Toast.LENGTH_LONG).show();
//                    GetFile();
                    downloadManager.remove(id);
                    break;
            }
        }
    }*/
//    private void GetFile(){
//        //Retrieve the saved request id
//        long downloadID = preferenceManager.getLong(strPref_Download_ID, 0);
//
//        ParcelFileDescriptor file;
//        try {
//            file = downloadManager.openDownloadedFile(downloadID);
//            FileInputStream fileInputStream
//                    = new ParcelFileDescriptor.AutoCloseInputStream(file);
//            Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
//            image.setImageBitmap(bm);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    }



