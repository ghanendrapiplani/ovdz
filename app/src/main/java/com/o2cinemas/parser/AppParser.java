package com.o2cinemas.parser;

import android.app.job.JobScheduler;
import android.util.Log;

import com.o2cinemas.beans.Detail_Bean;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.beans.TvShow_DetailBean;
import com.o2cinemas.o2vidz.TvShow_SeasonDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user on 4/19/2016.
 */
public class AppParser {

    public StatusBean getHomeMoviesResponse(String response) {
        StatusBean statusBean = new StatusBean();

        if (response != null) {

            try {
                JSONObject jsonObject = new JSONObject(response);

                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                statusBean.setStatus(status);
                statusBean.setMsg(msg);
                if (status.equals("1")) {

                    JSONObject homeObject = jsonObject.getJSONObject("homeData");
                    JSONArray mp4Array = homeObject.getJSONArray("mp4_movies");
                    Map<String, ArrayList<HomeMoivesBean>> arrayListMap = new HashMap<>();
                    ArrayList<HomeMoivesBean> mp4List = new ArrayList<>();
                    for (int i = 0; i < mp4Array.length(); i++) {
                        JSONObject dataObject = mp4Array.getJSONObject(i);
                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                        moivesBean.setId(dataObject.getString("id"));
                        moivesBean.setImage(dataObject.getString("ssname"));
                        moivesBean.setTitle(dataObject.getString("title"));
                        moivesBean.setType("mp4Movies");
                        mp4List.add(moivesBean);
                    }
                    arrayListMap.put("MP4 Movies", mp4List);


                    JSONArray gpArray = homeObject.getJSONArray("3gp_movies");
                    ArrayList<HomeMoivesBean> gp3List = new ArrayList<>();
                    for (int i = 0; i < gpArray.length(); i++) {
                        JSONObject dataObject = gpArray.getJSONObject(i);
                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                        moivesBean.setId(dataObject.getString("id"));
                        moivesBean.setImage(dataObject.getString("ssname"));
                        moivesBean.setTitle(dataObject.getString("title"));
                        moivesBean.setType("3gpMovies");
                        gp3List.add(moivesBean);
                    }
                    arrayListMap.put("3GP Movies", gp3List);


                    JSONArray tvArray = homeObject.getJSONArray("tv_shows");
                    ArrayList<HomeMoivesBean> tvShowList = new ArrayList<>();
                    for (int i = 0; i < tvArray.length(); i++) {
                        JSONObject dataObject = tvArray.getJSONObject(i);
                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                        moivesBean.setId(dataObject.getString("episode_id"));
                        moivesBean.setImage(dataObject.getString("tv_series_image"));
                        moivesBean.setTitle(dataObject.getString("tv_series_name"));
                        moivesBean.setType("tvshows");
                        tvShowList.add(moivesBean);
                    }
                    arrayListMap.put("TV SHOWS", tvShowList);
                    statusBean.setHomeMovieMap(arrayListMap);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return statusBean;
    }

    public StatusBean getMp4Gor3GpGroupDetailResponse(String response) {
        StatusBean statusBean = new StatusBean();
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                statusBean.setMsg(msg);
                statusBean.setStatus(status);

                ArrayList<HomeMoivesBean> mp4List = new ArrayList<>();
                if (status.equals("1")) {
                    JSONArray array = null;
                    if (jsonObject.has("mp4Data")){
                        array = jsonObject.getJSONArray("mp4Data");
                    } else {
                        array = jsonObject.getJSONArray("Gp3Data");
                    }


                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject dataObject = (JSONObject) array.get(i);
                            HomeMoivesBean moivesBean = new HomeMoivesBean();
                            moivesBean.setId(dataObject.getString("id"));
                            moivesBean.setImage(dataObject.getString("ssname"));
                            moivesBean.setTitle(dataObject.getString("title"));
                            moivesBean.setType("mp4Movies");
                            mp4List.add(moivesBean);
                        }
                    }
                }
                statusBean.setHomeMoviesBean(mp4List);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return statusBean;
    }
    public StatusBean getMp4Response(String response, String objectName) {
        StatusBean statusBean = new StatusBean();
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                statusBean.setMsg(msg);
                statusBean.setStatus(status);
                if (status.equals("1")) {
                    JSONObject mp4DataObject = jsonObject.getJSONObject(objectName);

                    Iterator<String> iterator = mp4DataObject.keys();
                    Map<String, ArrayList<HomeMoivesBean>> mp4Map = new HashMap<>();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Log.e("Mp4", "Keys = " + key);
                        ArrayList<HomeMoivesBean> mp4DataBeanArrayList = new ArrayList<>();
                        JSONArray jsonArray = mp4DataObject.getJSONArray(key);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            HomeMoivesBean mp4DataBean = new HomeMoivesBean();
                            mp4DataBean.setId(dataObject.getString("id"));
                            mp4DataBean.setTitle(dataObject.getString("title"));
                            mp4DataBean.setImage(dataObject.getString("ssname"));
                            if (objectName.equals("mp4Data")){
                                mp4DataBean.setType("mp4Movies");
                            }else if (objectName.equals("movies_3gp_data")){
                                mp4DataBean.setType("3gpMovies");
                            }


                            mp4DataBeanArrayList.add(mp4DataBean);
                        }
                        mp4Map.put(putMapKey(key), mp4DataBeanArrayList);
                    }
                    statusBean.setHomeMovieMap(mp4Map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return statusBean;
    }

    private String putMapKey(String inputKey) {
        String finalKey = "";
        if (inputKey.equals("latest_movies")) {
            finalKey = "Latest Additions";
        } else if (inputKey.equals("bollywood")) {
            finalKey = "Bollywood";
        } else if (inputKey.equals("hollywood")) {
            finalKey = "Hollywood";
        } else if (inputKey.equals("hindi_dubbed")) {
            finalKey = "Hindi Dubbed";
        } else if (inputKey.equals("wrestling")) {
            finalKey = "Wrestling";
        } else if (inputKey.equals("others")) {
            finalKey = "Others";
        }

        return finalKey;
    }


    public Detail_Bean getDetailResponse(String response1) {
        Detail_Bean detailBean = new Detail_Bean();
        if (response1 != null) {
            try {
                JSONObject jsonObject1 = new JSONObject(response1);
                String msg = jsonObject1.getString("msg");
                String status = jsonObject1.getString("status");
                detailBean.setMsg(msg);
                detailBean.setStatus(status);
                if (status.equals("1")) {
                    JSONObject jsonObject = jsonObject1.getJSONObject("movie_data");
                    detailBean.setId(jsonObject.getString("id"));
                    detailBean.setDescription(jsonObject.getString("description"));
                    detailBean.setCategory(jsonObject.getString("category"));
                    detailBean.setLength(jsonObject.getString("length"));
                    detailBean.setDownload(jsonObject.getString("download"));
                    detailBean.setSsname(jsonObject.getString("ssname"));
                    detailBean.setTitle(jsonObject.getString("title"));
                    detailBean.setFoldername(jsonObject.getString("foldername"));

                    JSONArray jsonArray = jsonObject.getJSONArray("parts");
                    ArrayList<String> partsList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String partName = jsonArray.getString(i);
                        partsList.add(partName);
                    }
                    detailBean.setPartsList(partsList);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detailBean;
    }
//

//    public Detail_Bean getSearchResponse(String response1) {
//        Detail_Bean detailBean = new Detail_Bean();
//        if (response1 != null) {
//            try {
//                JSONObject jsonObject1 = new JSONObject(response1);
//                String msg = jsonObject1.getString("msg");
//                String status = jsonObject1.getString("status");
//                detailBean.setMsg(msg);
//                detailBean.setStatus(status);
//                if (status.equals("1")) {
//                    ArrayList<Detail_Bean> searchArray  = new ArrayList<>();
//                    JSONArray jsonObject = jsonObject1.getJSONArray("mp4Data");
//
//                    for (int i = 0; i<jsonObject.length(); i++) {
//                        JSONObject jsonObject2 = jsonObject.getJSONObject(i);
//                        Detail_Bean detailBean1 = new Detail_Bean();
//                        detailBean1.setId(jsonObject2.getString("id"));
//                        detailBean1.setCategory(jsonObject2.getString("category"));
//                        detailBean1.setTitle(jsonObject2.getString("title"));
//                        searchArray.add(detailBean1);
//                    }
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return detailBean;


    public StatusBean getSearchResponse(String response1) {
        StatusBean detailBean = new StatusBean();
        if (response1 != null) {
            try {
                JSONObject jsonObject1 = new JSONObject(response1);
                String msg = jsonObject1.getString("msg");
                String status = jsonObject1.getString("status");
                detailBean.setMsg(msg);
                detailBean.setStatus(status);
                if (status.equals("1")) {

                    JSONArray jsonObject = jsonObject1.getJSONArray("mp4Data");
                    ArrayList<Detail_Bean> searchArray  = new ArrayList<>();
                    for (int i = 0; i<jsonObject.length(); i++) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject(i);
                        Detail_Bean detailBean1 = new Detail_Bean();
                        detailBean1.setId(jsonObject2.getString("id"));
                        detailBean1.setCategory(jsonObject2.getString("category"));
                        detailBean1.setTitle(jsonObject2.getString("title"));
                        detailBean1.setSsname(jsonObject2.getString("ssname"));

                        detailBean1.setType("mp4Movies");
                        searchArray.add(detailBean1);
                    }
                    detailBean.setDetailBeans(searchArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detailBean;

    }

    public StatusBean get3gpSearchResponse(String response1) {
        StatusBean detailBean = new StatusBean();
        if (response1 != null) {
            try {
                JSONObject jsonObject1 = new JSONObject(response1);
                String msg = jsonObject1.getString("msg");
                String status = jsonObject1.getString("status");
                detailBean.setMsg(msg);
                detailBean.setStatus(status);
                if (status.equals("1")) {

                    JSONArray jsonObject = jsonObject1.getJSONArray("Gp3Data");
                    ArrayList<Detail_Bean> searchArray  = new ArrayList<>();
                    for (int i = 0; i<jsonObject.length(); i++) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject(i);
                        Detail_Bean detailBean1 = new Detail_Bean();
                        detailBean1.setId(jsonObject2.getString("id"));
                        detailBean1.setCategory(jsonObject2.getString("category"));
                        detailBean1.setTitle(jsonObject2.getString("title"));
                        detailBean1.setSsname(jsonObject2.getString("ssname"));

                        detailBean1.setType("3gpMovies");
                        searchArray.add(detailBean1);
                    }
                    detailBean.setDetailBeans(searchArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detailBean;

    }

    public StatusBean getTvShowSearchResponse(String response1) {
        StatusBean showBean = new StatusBean();
        if (response1 != null) {
            try {
                JSONObject jsonObject1 = new JSONObject(response1);
                String msg = jsonObject1.getString("msg");
                String status = jsonObject1.getString("status");
                showBean.setMsg(msg);
                showBean.setStatus(status);
                if (status.equals("1")) {
                    JSONObject jsonObject = jsonObject1.getJSONObject("TvShowsData");
                    JSONArray showArray = jsonObject.getJSONArray("tv_series");
                    ArrayList<TvShow_DetailBean> searchArray  = new ArrayList<>();
                    for (int i = 0; i< showArray.length(); i++) {
                        JSONObject jsonObject2 = showArray.getJSONObject(i);
                        TvShow_DetailBean show = new TvShow_DetailBean();
                        show.setTv_series_id(jsonObject2.getString("tv_series_id"));
                        show.setTv_series_name(jsonObject2.getString("tv_series_name"));
                        show.setTv_series_image(jsonObject2.getString("tv_series_image"));
                        searchArray.add(show);
                    }
                    JSONArray episodeArray = jsonObject.getJSONArray("tv_episodes");
                    if (episodeArray!= null){
                    }
                    showBean.setShow_detailBeans(searchArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return showBean;

    }
}
