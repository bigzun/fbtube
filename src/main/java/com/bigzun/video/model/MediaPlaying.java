package com.bigzun.video.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaPlaying implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int TYPE_FULL_DATA = 0;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_ALBUM = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_PLAYLIST = 20;

    private String id = "";
    private int type = TYPE_FULL_DATA;
    private String name = "";
    private String singer = "";
    private String image = "";
    private String url = "";
    private ArrayList<MediaModel> mediaList;

    public MediaPlaying() {
        super();
        mediaList = new ArrayList<>();
    }

    public void setMedia(MediaModel item, int type) {
        if (item != null) {
            this.type = type;
            id = item.getId();
            name = item.getName();
            mediaList = item.getMediaList();
            image = item.getImage();
            singer = item.getSinger();
        }
    }

    public boolean isEmpty() {
        if (mediaList != null && mediaList.size() > 0)
            return false;
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<MediaModel> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<MediaModel> mediaList) {
        this.mediaList = mediaList;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MediaPlaying { "
                + "id= " + id
                + ", type= " + type
                + ", name= " + name
                + ", singer= " + singer
                + ", image= " + image
                + ", mediaList= " + mediaList
                + " }";
    }
}
