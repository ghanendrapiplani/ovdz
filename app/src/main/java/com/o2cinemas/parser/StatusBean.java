package com.o2cinemas.parser;

import com.o2cinemas.beans.Detail_Bean;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.beans.TvShow_DetailBean;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user on 4/12/2016.
 */
public class StatusBean {

    private String msg;
    private String status;
    private Map<String , ArrayList<HomeMoivesBean>> homeMovieMap;
    private ArrayList<Detail_Bean> detailBeans;
    private ArrayList<TvShow_DetailBean> show_detailBeans;
    private ArrayList<HomeMoivesBean> homeMoviesBean;

    public ArrayList<TvShow_DetailBean> getShow_detailBeans() {
        return show_detailBeans;
    }

    public void setShow_detailBeans(ArrayList<TvShow_DetailBean> show_detailBeans) {
        this.show_detailBeans = show_detailBeans;
    }
    //    private Map<String, ArrayList<TvShow_DetailBean>> tvShowMap;


    public ArrayList<Detail_Bean> getDetailBeans() {
        return detailBeans;
    }

    public void setDetailBeans(ArrayList<Detail_Bean> detailBeans) {
        this.detailBeans = detailBeans;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, ArrayList<HomeMoivesBean>> getHomeMovieMap() {
        return homeMovieMap;
    }

    public void setHomeMovieMap(Map<String, ArrayList<HomeMoivesBean>> homeMovieMap) {
        this.homeMovieMap = homeMovieMap;
    }

    public ArrayList<HomeMoivesBean> getHomeMoviesBean() {
        return homeMoviesBean;
    }

    public void setHomeMoviesBean(ArrayList<HomeMoivesBean> homeMoviesBean) {
        this.homeMoviesBean = homeMoviesBean;
    }
}
