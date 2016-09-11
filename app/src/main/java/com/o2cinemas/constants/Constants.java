package com.o2cinemas.constants;

/**
 * Created by user on 4/15/2016.
 */
public interface Constants {

    String SERVER_ADDRESS = "http://188.166.249.218/";
    String homeResponse = "http://188.166.249.218/o2vidz/api/getHomeMovies";
    String imageResponseMp4 = "http://mp4mania.com/screenshots/";
    String imageResponseGp3="http://3gpmania.co/screenshots/";
    String imageResponseTvShow= "http://tvshows4mobile.com/";
    String mp4Response="http://188.166.249.218/o2vidz/api/getMp4Movies";
    String gp3Response= "http://188.166.249.218/o2vidz/api/get3gpMovies";
    String tvShowResponse="http://188.166.249.218/o2vidz/api/getTvShows";
    String tvShowAlphabetResponse = "http://188.166.249.218/o2vidz/api/getTvShowsByAlphabets";
    String mp4MoviesByCatKey = SERVER_ADDRESS + "o2vidz/api/getMp4MoviesCatByKey";
    String gp3MoviesByCatKey = SERVER_ADDRESS + "o2vidz/api/get3gpMoviesCatByKey";

    /****** movie detail *******/
    String detailResponse="http://188.166.249.218/o2vidz/api/getMovieDetails";

    /******* Server *******////
    String SERVER_1="http://download1.hdmoviesluv.com";
    String SERVER_2 = "http://download2.hdmoviesluv.com";
    String SERVER_3 = "http://download3.hdmoviesluv.com";
    String SERVER_4 = "http://download5.hdmoviesluv.com";

    /****** server for 3gp movies **********/
    String GP3_SERVER_1 = "http://download5.moviesluv.com";
    String GP3_SERVER_2 = "http://download6.moviesluv.com";
    String GP3_SERVER_3 = "http://download7.moviesluv.com";
    String GP3_SERVER_4 = "http://download8.moviesluv.com";

/********* server for tv show *******/
    String TV_SERVER_1 = "http://download1.tvshows4mobile.com";
    String TV_SERVER_2 = "http://download2.tvshows4mobile.com";
    String TV_SERVER_3 = "http://download3.tvshows4mobile.com";
    /************* Tv Show Detail***********/
    String tvShowSeasonResponse= "http://188.166.249.218/o2vidz/api/getTvShowsDetails";
    String tvShowEpisodeResponse= "http://188.166.249.218/o2vidz/api/getEpisodeDetails";

/********** search ************/
    String searchMp4 = "http://188.166.249.218/o2vidz/api/getMp4MoviesByKey";
    String searchGp3 = "http://188.166.249.218/o2vidz/api/get3gpMoviesByKey";
    String searchTvShow = "http://188.166.249.218/o2vidz/api/getTvShowsByKey";

    /*************** Intent param *****************/
    String MOVIE_ID= "movie_id";
    String MOVIE_TYPE= "movie_type";
    String MOVIE_IMAGE= "movie_image";

    String SHOW_ID= "tv_series_id";
    String SHOW_TYPE="tv_series_type";
    String SHOW_IMAGE="tv_series_image";

    String TVSHOW_FILTER = "filter_by";
    String EPISODE_ID = "episode_id";
    String EPISODE_NAME= "episode_name";
    String SEASON_NAME = "season_name";

    // Download
    String ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION = "ADD_TO_QUEUE_DOWNLOAD_PENDING_ACTION";
    String DOWNLOAD_URL_KEY = "DOWNLOAD_URL_KEY";
    String DOWNLOAD_TITLE_KEY = "DOWNLOAD_TITLE_KEY";

    // group details fragment
    String GROUP_TYPE_KEY = "GROUP_TYPE_KEY";
    String MOVIE_TYPE_KEY = "MOVIE_TYPE_KEY";
    String MP_4_TYPE = "mp4Movies";
    String GP3_TYPE = "3gpMovies";
    String TV_SHOW_TYPE = "tvshows";
    String HOLLYWOOD_TYPE = "Hollywood";
    String BOLLYWOOD_TYPE = "Bollywood";
    String LATEST_CONDITIONS_TYPE ="latest_additions";
    String TV_SHOWS_TYPE = "tv_shows";

    String SORTING_TEXT[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0-9"};
    //
    int HOME_TAB_POSITION = 0;
    int MP4_TAB_POSITION = 1;
    int GP3_TAB_POSITION = 2;
    int TV_SHOW_TAB_POSITION = 3;
    int TOP_DOWNLOAD_TAB_POSITION = 4;


}
