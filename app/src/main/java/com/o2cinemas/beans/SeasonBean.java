package com.o2cinemas.beans;

import java.util.ArrayList;

/**
 * Created by user on 5/30/2016.
 */

public class SeasonBean {
    private String season_id;
    private String season_name;
    private String season_views;
    private ArrayList<Episode_Bean> episode_beans;


    public ArrayList<Episode_Bean> getEpisode_beans() {
        return episode_beans;
    }

    public void setEpisode_beans(ArrayList<Episode_Bean> episode_beans) {
        this.episode_beans = episode_beans;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }

    public String getSeason_views() {
        return season_views;
    }

    public void setSeason_views(String season_views) {
        this.season_views = season_views;
    }
}

