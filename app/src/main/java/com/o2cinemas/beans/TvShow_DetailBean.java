package com.o2cinemas.beans;

import com.o2cinemas.models.GenreItem;

import java.util.ArrayList;

/**
 * Created by user on 5/30/2016.
 */

public class TvShow_DetailBean {


    private String msg;
    private String Status;
    private String tv_series_id;
    private String tv_series_name;
    private String tv_series_name_seo;
    private String tv_series_desc;
    private String tv_series_meta_descriptions;
    private String tv_series_meta_keywords;
    private String tv_series_cast;
    private String tv_series_image;
    private String tv_series_runtime;
    private String tv_series_views;
    private String imbd_link;
    private String rating_rate;
    private String rating_count;
    private String tv_series_added;
    private String tv_series_modified;
    private String tv_series_status;
    private String episode_id;
    private String episode_name;
    private String season_name;
    private ArrayList<SeasonBean> seasonBeanArrayList;
    private ArrayList<Episode_Bean> episode_beanArrayList;
    private ArrayList<GenreItem> genreItemArrayList;

    public String getEpisode_name() {
        return episode_name;
    }

    public void setEpisode_name(String episode_name) {
        this.episode_name = episode_name;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTv_series_id() {
        return tv_series_id;
    }

    public void setTv_series_id(String tv_series_id) {
        this.tv_series_id = tv_series_id;
    }

    public String getTv_series_name() {
        return tv_series_name;
    }

    public void setTv_series_name(String tv_series_name) {
        this.tv_series_name = tv_series_name;
    }

    public String getTv_series_name_seo() {
        return tv_series_name_seo;
    }

    public void setTv_series_name_seo(String tv_series_name_seo) {
        this.tv_series_name_seo = tv_series_name_seo;
    }

    public String getTv_series_desc() {
        return tv_series_desc;
    }

    public void setTv_series_desc(String tv_series_desc) {
        this.tv_series_desc = tv_series_desc;
    }

    public String getTv_series_meta_descriptions() {
        return tv_series_meta_descriptions;
    }

    public void setTv_series_meta_descriptions(String tv_series_meta_descriptions) {
        this.tv_series_meta_descriptions = tv_series_meta_descriptions;
    }

    public String getTv_series_meta_keywords() {
        return tv_series_meta_keywords;
    }

    public void setTv_series_meta_keywords(String tv_series_meta_keywords) {
        this.tv_series_meta_keywords = tv_series_meta_keywords;
    }

    public String getTv_series_cast() {
        return tv_series_cast;
    }

    public void setTv_series_cast(String tv_series_cast) {
        this.tv_series_cast = tv_series_cast;
    }

    public String getTv_series_image() {
        return tv_series_image;
    }

    public void setTv_series_image(String tv_series_image) {
        this.tv_series_image = tv_series_image;
    }

    public String getTv_series_runtime() {
        return tv_series_runtime;
    }

    public void setTv_series_runtime(String tv_series_runtime) {
        this.tv_series_runtime = tv_series_runtime;
    }

    public String getTv_series_views() {
        return tv_series_views;
    }

    public void setTv_series_views(String tv_series_views) {
        this.tv_series_views = tv_series_views;
    }

    public String getImbd_link() {
        return imbd_link;
    }

    public void setImbd_link(String imbd_link) {
        this.imbd_link = imbd_link;
    }

    public String getRating_rate() {
        return rating_rate;
    }

    public void setRating_rate(String rating_rate) {
        this.rating_rate = rating_rate;
    }

    public String getRating_count() {
        return rating_count;
    }

    public void setRating_count(String rating_count) {
        this.rating_count = rating_count;
    }

    public String getTv_series_added() {
        return tv_series_added;
    }

    public void setTv_series_added(String tv_series_added) {
        this.tv_series_added = tv_series_added;
    }

    public String getTv_series_modified() {
        return tv_series_modified;
    }

    public void setTv_series_modified(String tv_series_modified) {
        this.tv_series_modified = tv_series_modified;
    }

    public String getTv_series_status() {
        return tv_series_status;
    }

    public void setTv_series_status(String tv_series_status) {
        this.tv_series_status = tv_series_status;
    }

    public ArrayList<SeasonBean> getSeasonBeanArrayList() {
        return seasonBeanArrayList;
    }

    public void setSeasonBeanArrayList(ArrayList<SeasonBean> seasonBeanArrayList) {
        this.seasonBeanArrayList = seasonBeanArrayList;
    }

    public String getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(String episode_id) {
        this.episode_id = episode_id;
    }

    public ArrayList<Episode_Bean> getEpisode_beanArrayList() {
        return episode_beanArrayList;
    }

    public void setEpisode_beanArrayList(ArrayList<Episode_Bean> episode_beanArrayList) {
        this.episode_beanArrayList = episode_beanArrayList;
    }

    public ArrayList<GenreItem> getGenreItemArrayList() {
        return genreItemArrayList;
    }

    public void setGenreItemArrayList(ArrayList<GenreItem> genreItemArrayList) {
        this.genreItemArrayList = genreItemArrayList;
    }
}
