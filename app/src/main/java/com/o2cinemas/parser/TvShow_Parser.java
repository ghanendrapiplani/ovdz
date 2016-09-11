package com.o2cinemas.parser;

import android.util.Log;

import com.o2cinemas.beans.Episode_Bean;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.beans.SeasonBean;
import com.o2cinemas.beans.TvShow_DetailBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.GenreItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 5/30/2016.
 */
public class TvShow_Parser {


    public TvShow_DetailBean getTvShowDetailResponse(String response) {
        TvShow_DetailBean tvShow_detailBean = new TvShow_DetailBean();

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                tvShow_detailBean.setMsg(msg);
                tvShow_detailBean.setStatus(status);
                if (status.equals("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("tvShowsDetails");
                    tvShow_detailBean.setTv_series_id(jsonObject1.getString("tv_series_id"));
                    tvShow_detailBean.setTv_series_name(jsonObject1.getString("tv_series_name"));
                    tvShow_detailBean.setTv_series_name_seo(jsonObject1.getString("tv_series_name_seo"));
                    tvShow_detailBean.setTv_series_desc(jsonObject1.getString("tv_series_desc"));
                    tvShow_detailBean.setTv_series_meta_descriptions(jsonObject1.getString("tv_series_meta_descriptions"));
                    tvShow_detailBean.setTv_series_meta_keywords(jsonObject1.getString("tv_series_meta_keywords"));
                    tvShow_detailBean.setTv_series_cast(jsonObject1.getString("tv_series_cast"));
                    tvShow_detailBean.setTv_series_image(jsonObject1.getString("tv_series_image"));
                    tvShow_detailBean.setTv_series_runtime(jsonObject1.getString("tv_series_runtime"));
                    tvShow_detailBean.setTv_series_views(jsonObject1.getString("tv_series_views"));
                    tvShow_detailBean.setImbd_link(jsonObject1.getString("imbd_link"));
                    tvShow_detailBean.setRating_rate(jsonObject1.getString("rating_rate"));
                    tvShow_detailBean.setRating_count(jsonObject1.getString("rating_count"));
                    tvShow_detailBean.setTv_series_added(jsonObject1.getString("tv_series_added"));
                    tvShow_detailBean.setTv_series_modified(jsonObject1.getString("tv_series_modified"));
                    tvShow_detailBean.setTv_series_status(jsonObject1.getString("tv_series_status"));

                    if (jsonObject1.has("genre")) {
                        JSONArray arr = jsonObject1.getJSONArray("genre");
                        if (arr != null && arr.length() > 0) {
                            ArrayList<GenreItem> genreItems = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i ++) {
                                JSONObject genreObj = (JSONObject) arr.get(i);
                                GenreItem item = new GenreItem(genreObj.getString("genre_id"), genreObj.getString("genre_name"));
                                genreItems.add(item);
                            }
                            tvShow_detailBean.setGenreItemArrayList(genreItems);
                        }
                    }

                    ArrayList<SeasonBean> seasonBeanArrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject1.getJSONArray("seasons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject seasonObject = jsonArray.getJSONObject(i);
                        SeasonBean seasonBean = new SeasonBean();
                        seasonBean.setSeason_id(seasonObject.getString("season_id"));
                        seasonBean.setSeason_name(seasonObject.getString("season_name"));
                        seasonBean.setSeason_views(seasonObject.getString("season_views"));

                        ArrayList<Episode_Bean> episodeBeanArrayList = new ArrayList<>();
                        JSONArray jsonArray1 = seasonObject.getJSONArray("episodes");
                        for (int j = jsonArray1.length() -1 ; j >= 0; j--) {
                            JSONObject episodeObject = jsonArray1.getJSONObject(j);

                            Episode_Bean episode_bean = new Episode_Bean();
                            episode_bean.setEpisode_id(episodeObject.getString("episode_id"));
                            episode_bean.setFk_season_id(episodeObject.getString("fk_season_id"));
                            episode_bean.setEpisode_name(episodeObject.getString("episode_name"));
                            episode_bean.setEpisode_name_seo(episodeObject.getString("episode_name_seo"));
                            episode_bean.setEpisode_views(episodeObject.getString("episode_views"));
                            episode_bean.setEpisode_added(episodeObject.getString("episode_added"));
                            episode_bean.setEpisode_modified(episodeObject.getString("episode_modified"));
                            episode_bean.setEpisode_status(episodeObject.getString("episode_status"));
//                            Log.e("episode_name", ":-" + episodeObject.getString("episode_name"));
                            episodeBeanArrayList.add(episode_bean);

                        }
                        seasonBeanArrayList.add(seasonBean);
                        seasonBean.setEpisode_beans(episodeBeanArrayList);
                    }

                    tvShow_detailBean.setSeasonBeanArrayList(seasonBeanArrayList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tvShow_detailBean;
    }

    public StatusBean getTvShowResponse(String response) {
        StatusBean statusBean = new StatusBean();

        if (response != null) {

            try {
                JSONObject jsonObject = new JSONObject(response);

                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                statusBean.setStatus(status);
                statusBean.setMsg(msg);
                if (status.equals("1")) {

                    JSONObject homeObject = jsonObject.getJSONObject("tvShowsData");
                    JSONArray latest_additions = homeObject.getJSONArray("latest_additions");
                    Map<String, ArrayList<HomeMoivesBean>> arrayListTvMap = new HashMap<>();
                    ArrayList<HomeMoivesBean> tvLatestList = new ArrayList<>();
                    for (int i = 0; i < latest_additions.length(); i++) {
                        JSONObject dataObject = latest_additions.getJSONObject(i);
                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                        moivesBean.setId(dataObject.getString("episode_id"));
                        moivesBean.setImage(dataObject.getString("tv_series_image"));
                        moivesBean.setTitle(dataObject.getString("tv_series_name"));
                        moivesBean.setType("tvshows");
                        tvLatestList.add(moivesBean);
                    }
                    arrayListTvMap.put("LATEST ADDITIONS", tvLatestList);

                    JSONArray tvArray = homeObject.getJSONArray("tv_shows");
                    ArrayList<HomeMoivesBean> tvSeasonList = new ArrayList<>();
                    for (int i = 0; i < tvArray.length(); i++) {
                        JSONObject dataObject = tvArray.getJSONObject(i);
                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                        moivesBean.setId(dataObject.getString("tv_series_id"));
                        moivesBean.setImage(dataObject.getString("tv_series_image"));
                        moivesBean.setTitle(dataObject.getString("tv_series_name"));
                        moivesBean.setType("tvshows");
                        tvSeasonList.add(moivesBean);

                    }
                    arrayListTvMap.put("LATEST TV SHOWS", tvSeasonList);
                    statusBean.setHomeMovieMap(arrayListTvMap);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return statusBean;
    }

    public StatusBean getTvAlphabetResponse(String response) {
        StatusBean statusBean = new StatusBean();
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                statusBean.setStatus(status);
                statusBean.setMsg(msg);
                if (status.equals("1")) {
                    JSONObject tvAlphabet = jsonObject.getJSONObject("tvShowsData");
                    JSONArray sortingArray = tvAlphabet.getJSONArray("tv_shows");
                    Map<String, ArrayList<HomeMoivesBean>> arrayListSortMap = new HashMap<>();
                    ArrayList<HomeMoivesBean> tvSortList = new ArrayList<>();
                    for (int i = 0; i < sortingArray.length(); i++) {
                        JSONObject dataObject = sortingArray.getJSONObject(i);
                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                        moivesBean.setId(dataObject.getString("tv_series_id"));
                        moivesBean.setImage(dataObject.getString("tv_series_image"));
                        moivesBean.setTitle(dataObject.getString("tv_series_name"));

                        moivesBean.setType("tvshows");

                        tvSortList.add(moivesBean);
                    }
                    arrayListSortMap.put("A-Z", tvSortList);
                    statusBean.setHomeMovieMap(arrayListSortMap);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return statusBean;
    }

    public StatusBean getTvShowGroupResponse(String response, String groupType, boolean isFiltering) {
        StatusBean statusBean = new StatusBean();
        ArrayList<HomeMoivesBean> tvList = new ArrayList<>();
        if (response != null) {

            try {
                JSONObject jsonObject = new JSONObject(response);

                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                statusBean.setStatus(status);
                statusBean.setMsg(msg);
                if (status.equals("1")) {
                    JSONObject homeObject = jsonObject.getJSONObject("tvShowsData");
                    if (homeObject != null) {
                        if (isFiltering) {
                            //TODO need to change when api changed
                            JSONArray tvArray = homeObject.getJSONArray(Constants.TV_SHOWS_TYPE);

                            if (tvArray != null && tvArray.length() > 0) {
                                for (int i = 0; i < tvArray.length(); i++) {
                                    JSONObject dataObject = tvArray.getJSONObject(i);
                                    HomeMoivesBean moivesBean = new HomeMoivesBean();
                                    moivesBean.setId(dataObject.getString("tv_series_id"));
                                    moivesBean.setImage(dataObject.getString("tv_series_image"));
                                    moivesBean.setTitle(dataObject.getString("tv_series_name"));
                                    moivesBean.setType("tvshows");
                                    tvList.add(moivesBean);
                                }
                            }
                        } else {
                            if (groupType != null && groupType.equals(Constants.LATEST_CONDITIONS_TYPE)) {
                                JSONArray latest_additions = homeObject.getJSONArray(Constants.LATEST_CONDITIONS_TYPE);

                                if (latest_additions != null && latest_additions.length() > 0) {
                                    for (int i = 0; i < latest_additions.length(); i++) {
                                        JSONObject dataObject = latest_additions.getJSONObject(i);
                                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                                        moivesBean.setId(dataObject.getString("episode_id"));
                                        moivesBean.setImage(dataObject.getString("tv_series_image"));
                                        moivesBean.setTitle(dataObject.getString("tv_series_name"));
                                        moivesBean.setType("tvshows");
                                        tvList.add(moivesBean);
                                    }
                                }
                            } else {
                                JSONArray tvArray = homeObject.getJSONArray(Constants.TV_SHOWS_TYPE);

                                if (tvArray != null && tvArray.length() > 0) {
                                    for (int i = 0; i < tvArray.length(); i++) {
                                        JSONObject dataObject = tvArray.getJSONObject(i);
                                        HomeMoivesBean moivesBean = new HomeMoivesBean();
                                        moivesBean.setId(dataObject.getString("tv_series_id"));
                                        moivesBean.setImage(dataObject.getString("tv_series_image"));
                                        moivesBean.setTitle(dataObject.getString("tv_series_name"));
                                        moivesBean.setType("tvshows");
                                        tvList.add(moivesBean);
                                    }
                                }
                            }
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        statusBean.setHomeMoviesBean(tvList);
        return statusBean;
    }

    public TvShow_DetailBean getEpisodeDetailResponse(String response) {
        TvShow_DetailBean episode_detailBean = new TvShow_DetailBean();

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                episode_detailBean.setMsg(msg);
                episode_detailBean.setStatus(status);
                if (status.equals("1")) {
                    JSONObject episodeObject = jsonObject.getJSONObject("episodeDetails");
                    episode_detailBean.setEpisode_id(episodeObject.getString("episode_id"));
                    episode_detailBean.setTv_series_id(episodeObject.getString("tv_series_id"));
                    episode_detailBean.setTv_series_name(episodeObject.getString("tv_series_name"));
                    episode_detailBean.setTv_series_name_seo(episodeObject.getString("tv_series_name_seo"));
                    episode_detailBean.setTv_series_desc(episodeObject.getString("tv_series_desc"));
                    episode_detailBean.setTv_series_meta_descriptions(episodeObject.getString("tv_series_meta_descriptions"));
                    episode_detailBean.setTv_series_meta_keywords(episodeObject.getString("tv_series_meta_keywords"));
                    episode_detailBean.setTv_series_cast(episodeObject.getString("tv_series_cast"));
                    episode_detailBean.setTv_series_image(episodeObject.getString("tv_series_image"));
                    episode_detailBean.setTv_series_runtime(episodeObject.getString("tv_series_runtime"));
                    episode_detailBean.setTv_series_views(episodeObject.getString("tv_series_views"));
                    episode_detailBean.setImbd_link(episodeObject.getString("imbd_link"));
                    episode_detailBean.setRating_rate(episodeObject.getString("rating_rate"));
                    episode_detailBean.setRating_count(episodeObject.getString("rating_count"));
                    episode_detailBean.setTv_series_added(episodeObject.getString("tv_series_added"));
                    episode_detailBean.setTv_series_modified(episodeObject.getString("tv_series_modified"));
                    episode_detailBean.setTv_series_status(episodeObject.getString("tv_series_status"));
                    episode_detailBean.setSeason_name(episodeObject.getString("season_name"));
                    episode_detailBean.setEpisode_name(episodeObject.getString("episode_name"));


                    ArrayList<Episode_Bean> episodeBeanArrayList = new ArrayList<>();
                    JSONArray episodeFileArray = episodeObject.getJSONArray("download_details");
                    for (int j = 0; j < episodeFileArray.length(); j++) {
                        JSONObject fileObject = episodeFileArray.getJSONObject(j);

                        Episode_Bean episode_bean = new Episode_Bean();
                        episode_bean.setFile_id(fileObject.getString("file_id"));
                        episode_bean.setFk_season_id(fileObject.getString("fk_episode_id"));
                        episode_bean.setFile_name(fileObject.getString("file_name"));
                        episode_bean.setFile_path(fileObject.getString("file_path"));
                        episode_bean.setFile_downloads(fileObject.getString("file_downloads"));
                        episode_bean.setFile_added(fileObject.getString("file_added"));
                        episode_bean.setFile_modified(fileObject.getString("file_modified"));
//                            Log.e("file_name", ":-" + fileObject.getString("file_name"));
                        episodeBeanArrayList.add(episode_bean);
                    }
                    episode_detailBean.setEpisode_beanArrayList(episodeBeanArrayList);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return episode_detailBean;
    }
}
