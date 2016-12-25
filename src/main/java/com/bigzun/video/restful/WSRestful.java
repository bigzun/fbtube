package com.bigzun.video.restful;

import android.content.Context;

import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.model.LocationModel;
import com.bigzun.video.restful.paser.RestAllMediaModel;
import com.bigzun.video.restful.paser.RestGuideCategories;
import com.bigzun.video.restful.paser.RestLanguagesRegions;
import com.bigzun.video.restful.paser.RestMediaModel;
import com.bigzun.video.restful.paser.RestMostPopularVideo;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Admin on 9/29/2016.
 */

public class WSRestful {
    private Context mContext;
    private Retrofit mRetrofit;
    private BaseService mBaseService;
    private final String API_KEY;
    private static final String LOCATION_URL = "http://freegeoip.net/";
    private static final String YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/";
    private static final String KEENG_URL = "http://vip.service.keeng.vn:8080/";

    private final String LANGUAGE_CODE;
    private final String REGION_CODE;

    public WSRestful(Context context) {
        this.mContext = context;
        LANGUAGE_CODE = ApplicationController.getInstance().getLanguageCode();
        REGION_CODE = ApplicationController.getInstance().getRegionCode();
        API_KEY = ApplicationController.getInstance().getApiKey();
    }

    private void initLocationApi() {
        mRetrofit = Injector.initRetrofit(LOCATION_URL);
        mBaseService = mRetrofit.create(BaseService.class);
    }

    private void initYoutubeApi() {
        mRetrofit = Injector.initRetrofit(YOUTUBE_URL);
        mBaseService = mRetrofit.create(BaseService.class);
    }

    private void initKeengApi() {
        mRetrofit = Injector.initRetrofit(KEENG_URL);
        mBaseService = mRetrofit.create(BaseService.class);
    }

    public void loadLocation(RestCallback<LocationModel> callback) {
        initLocationApi();
        Call<LocationModel> mCallRequest = mBaseService.getLocation();
        mCallRequest.enqueue(callback);
    }

    public void loadLanguages(RestCallback<RestLanguagesRegions> callback) {
        initYoutubeApi();
        Call<RestLanguagesRegions> mCallRequest = mBaseService.getLanguages(LANGUAGE_CODE, API_KEY);
        mCallRequest.enqueue(callback);
    }

    public void loadRegions(RestCallback<RestLanguagesRegions> callback) {
        initYoutubeApi();
        Call<RestLanguagesRegions> mCallRequest = mBaseService.getRegions(LANGUAGE_CODE, API_KEY);
        mCallRequest.enqueue(callback);
    }

    public void loadGuideCategories(RestCallback<RestGuideCategories> callback) {
        initYoutubeApi();
        Call<RestGuideCategories> mCallRequest = mBaseService.getGuideCategories(LANGUAGE_CODE, API_KEY, REGION_CODE);
        mCallRequest.enqueue(callback);
    }

    public void loadMostPopularVideo(RestCallback<RestMostPopularVideo> callback, int maxResults, String pageToken, int videoCategoryId) {
        initYoutubeApi();
        Call<RestMostPopularVideo> mCallRequest = mBaseService.getMostPopularVideo(LANGUAGE_CODE, API_KEY, REGION_CODE, maxResults, pageToken, videoCategoryId);
        mCallRequest.enqueue(callback);
    }

    public void loadKeengHome(RestCallback<RestAllMediaModel> callback, int page, int num) {
        initKeengApi();
        Call<RestAllMediaModel> mCallRequest = mBaseService.getKeengHome(page, num);
        mCallRequest.enqueue(callback);
    }

    public void loadKeengSong(RestCallback<RestMediaModel> callback, String id) {
        initKeengApi();
        Call<RestMediaModel> mCallRequest = mBaseService.getKeengSong(id);
        mCallRequest.enqueue(callback);
    }

    public Call<RestMediaModel> getAudioInfoRequest(String id) {
        initKeengApi();
        return mBaseService.getKeengSong(id);
    }

    public Call<RestMediaModel> getAlbumInfoRequest(String id) {
        initKeengApi();
        return mBaseService.getKeengAlbum(id);
    }

    public Call<RestMediaModel> getPlaylistInfoRequest(String id) {
        initKeengApi();
        return mBaseService.getKeengAlbum(id);
    }

    public Call<RestMediaModel> getVideoInfoRequest(String id) {
        initKeengApi();
        return mBaseService.getKeengVideo(id);
    }

    public void loadKeengAlbum(RestCallback<RestMediaModel> callback, String id) {
        initKeengApi();
        Call<RestMediaModel> mCallRequest = mBaseService.getKeengAlbum(id);
        mCallRequest.enqueue(callback);
    }

    public void loadKeengVideo(RestCallback<RestMediaModel> callback, String id) {
        initKeengApi();
        Call<RestMediaModel> mCallRequest = mBaseService.getKeengVideo(id);
        mCallRequest.enqueue(callback);
    }

}
