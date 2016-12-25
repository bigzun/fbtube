package com.bigzun.video.restful;

import com.bigzun.video.model.LocationModel;
import com.bigzun.video.restful.paser.RestAllMediaModel;
import com.bigzun.video.restful.paser.RestGuideCategories;
import com.bigzun.video.restful.paser.RestLanguagesRegions;
import com.bigzun.video.restful.paser.RestMediaModel;
import com.bigzun.video.restful.paser.RestMostPopularVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Admin on 12/21/2016.
 */

public interface BaseService {
    @GET("json/")
    Call<LocationModel> getLocation();

    @GET("i18nLanguages?part=snippet&fields=items%2Fsnippet")
    Call<RestLanguagesRegions> getLanguages(@Query("hl") String languageCode, @Query("key") String apiKey);

    @GET("i18nRegions?part=snippet&fields=items%2Fsnippet")
    Call<RestLanguagesRegions> getRegions(@Query("hl") String languageCode, @Query("key") String apiKey);

    @GET("guideCategories?part=snippet&fields=items(id%2Csnippet)")
    Call<RestGuideCategories> getGuideCategories(
            @Query("hl") String languageCode, @Query("key") String apiKey, @Query("regionCode") String regionCode);

    @GET("KeengWSRestful/ws/common/getHomeV2")
    Call<RestAllMediaModel> getKeengHome(@Query("page") int page, @Query("num") int num);

    @GET("KeengWSRestful/ws/common/getSong")
    Call<RestMediaModel> getKeengSong(@Query("id") String id);

    @GET("KeengWSRestful/ws/common/getAlbum")
    Call<RestMediaModel> getKeengAlbum(@Query("id") String id);

    @GET("KeengWSRestful/ws/common/getVideo")
    Call<RestMediaModel> getKeengVideo(@Query("id") String id);

    @GET("videos?part=snippet&chart=mostPopular")
    Call<RestMostPopularVideo> getMostPopularVideo(
            @Query("hl") String languageCode, @Query("key") String apiKey, @Query("regionCode") String regionCode,
            @Query("maxResults") int maxResults, @Query("pageToken") String pageToken,
            @Query("videoCategoryId") int videoCategoryId
    );
}
