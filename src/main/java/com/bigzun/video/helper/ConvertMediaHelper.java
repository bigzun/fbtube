package com.bigzun.video.helper;

import com.bigzun.video.model.MediaModel;
import com.bigzun.video.restful.paser.RestMostPopularVideo;

/**
 * Created by Admin on 12/25/2016.
 */

public class ConvertMediaHelper {

    public static final String VIDEO_URL_PATH = "https://www.youtube.com/watch?v=";

    public static MediaModel convertFromMostPopularVideo(RestMostPopularVideo.Items item) {
        if (item == null)
            return null;

        MediaModel model = new MediaModel();
        model.setYoutube(true);
        model.setId(item.getId());
        model.setName(item.getSnippet().getTitle());
        model.setDescription(item.getSnippet().getDescription());
        model.setImage(item.getSnippet().getThumbnails().getImage().getUrl());
        model.setSinger(item.getSnippet().getChannelTitle());
        model.setUrl(VIDEO_URL_PATH + item.getId());
        return model;
    }
}
