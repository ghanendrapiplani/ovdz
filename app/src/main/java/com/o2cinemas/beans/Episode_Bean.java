package com.o2cinemas.beans;

/**
 * Created by user on 5/31/2016.
 */
//
//"file_id": "1",
//        "fk_episode_id": "1",
//        "file_name": "Two And A Half Men - S01E01 (O2TvSeries.Com).3gp",
//        "file_path": "Two And A Half Men/Season 01/Two And A Half Men - S01E01 (O2TvSeries.Com).3gp",
//        "file_downloads": "14517",
//        "file_added": "2013-05-08 17:51:28",
//        "file_modified": "2015-08-05 18:12:47"
public class Episode_Bean {
    private String episode_id;
    private String fk_season_id;
    private String episode_name;
    private String episode_name_seo;
    private String episode_views;
    private String episode_added;
    private String episode_modified;
    private String episode_status;
    private String file_id;
    private String file_name;
    private String file_path;
    private String file_downloads;
    private String file_added;
    private String file_modified;

    public String getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(String episode_id) {
        this.episode_id = episode_id;
    }

    public String getFk_season_id() {
        return fk_season_id;
    }

    public void setFk_season_id(String fk_season_id) {
        this.fk_season_id = fk_season_id;
    }

    public String getEpisode_name() {
        return episode_name;
    }

    public void setEpisode_name(String episode_name) {
        this.episode_name = episode_name;
    }

    public String getEpisode_name_seo() {
        return episode_name_seo;
    }

    public void setEpisode_name_seo(String episode_name_seo) {
        this.episode_name_seo = episode_name_seo;
    }

    public String getEpisode_views() {
        return episode_views;
    }

    public void setEpisode_views(String episode_views) {
        this.episode_views = episode_views;
    }

    public String getEpisode_added() {
        return episode_added;
    }

    public void setEpisode_added(String episode_added) {
        this.episode_added = episode_added;
    }

    public String getEpisode_modified() {
        return episode_modified;
    }

    public void setEpisode_modified(String episode_modified) {
        this.episode_modified = episode_modified;
    }

    public String getEpisode_status() {
        return episode_status;
    }

    public void setEpisode_status(String episode_status) {
        this.episode_status = episode_status;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_downloads() {
        return file_downloads;
    }

    public void setFile_downloads(String file_downloads) {
        this.file_downloads = file_downloads;
    }

    public String getFile_added() {
        return file_added;
    }

    public void setFile_added(String file_added) {
        this.file_added = file_added;
    }

    public String getFile_modified() {
        return file_modified;
    }

    public void setFile_modified(String file_modified) {
        this.file_modified = file_modified;
    }
}
