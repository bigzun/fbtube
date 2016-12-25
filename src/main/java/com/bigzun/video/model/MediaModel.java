package com.bigzun.video.model;

import android.text.TextUtils;

import com.bigzun.video.helper.Typez;
import com.bigzun.video.util.Log;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 10/18/2016.
 */

public class MediaModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String TAG = MediaModel.class.getSimpleName();
    public static final String TYPE_AUDIO = "1";
    public static final String TYPE_ALBUM = "2";
    public static final String TYPE_VIDEO = "3";
    public static final String TYPE_PLAYLIST = "4";

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("singer")
    private String singer;

    @SerializedName("description")
    private String description;

    @SerializedName("item_type")
    private String type;

    @SerializedName("image310")
    private String image;

    @SerializedName("url")
    private String url;

    @SerializedName("media_url")
    private String mediaUrl;

    @SerializedName("song_list")
    private ArrayList<MediaModel> mediaList;

    private String lyric = "";
    private String subTitle = "http://data.hdonline.vn/api/vsub.php?url=http%3A%2F%2Fs.vn-hd.com%3A8080%2F122015%2F15%2FTake_Care_of_the_Young_Lady_720p_HDTV_x264_Zeus%2FE001%2FTake_Care_of_the_Young_Lady_720p_HDTV_x264_Zeus_E001_VIE.srt&client=android";
    private int position = -1;
    private boolean isYoutube = false;

    public MediaModel() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleCoverSinger() {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(singer))
            return name + " - " + singer;
        return name;
    }

    public Typez getType() {
        if (!TextUtils.isEmpty(type))
            switch (type) {
                case TYPE_AUDIO:
                    return Typez.song;
                case TYPE_ALBUM:
                    return Typez.album;
                case TYPE_VIDEO:
                    return Typez.video;
            }
        return Typez.empty;
    }

    public boolean isVideo() {
        if (Typez.video.equals(getType()))
            return true;

        return false;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaUrl() {

//        if (!TextUtils.isEmpty(mediaUrl)) {
//            if (mediaUrl.contains("?")) {
//                if (mediaUrl.contains("&rt=WP")) {
//                    mediaUrl = mediaUrl.replace("&rt=WP", "&rt=CP");
//                } else if (mediaUrl.contains("&rt=P")) {
//                    mediaUrl = mediaUrl.replace("&rt=P", "&rt=CP");
//                }
//            }
//        }
        Log.e(TAG, "getMediaUrl: " + mediaUrl);
        return mediaUrl;
        //return "http://www.youtube.com/get_video_info?video_id=CCiHDjAH2Hg";
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<MediaModel> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<MediaModel> mediaList) {
        this.mediaList = mediaList;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(singer))
            return name + " - " + singer;
        return name;
    }

    public boolean isYoutube() {
        return isYoutube;
    }

    public void setYoutube(boolean youtube) {
        if (youtube)
            type = TYPE_VIDEO;
        isYoutube = youtube;
    }

    @Override
    public String toString() {
        return TAG + "{ "
                + "id: " + id
                + ", name: " + name
                + ", singer: " + singer
                + ", description: " + description
                + ", type: " + type
                + ", mediaUrl: " + mediaUrl
                + ", image: " + image
                + ", url: " + url
                + ", lyric: " + lyric
                + ", mediaList: " + mediaList
                + " }";
    }
}
