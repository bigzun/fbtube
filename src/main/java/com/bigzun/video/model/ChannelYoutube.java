package com.bigzun.video.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by namnh40 on 12/23/2016.
 */
@IgnoreExtraProperties
public class ChannelYoutube {

    @SerializedName("channel_id")
    private String channel_id;
    @SerializedName("channel_name")
    private String channel_name;

    public ChannelYoutube() {
        super();
    }

    public ChannelYoutube(String id, String name) {
        channel_id = id;
        channel_name = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("channel_id", channel_id);
        result.put("channel_name", channel_name);
        return result;
    }

    public String getChannelId() {
        return channel_id;
    }

    public void setChannelId(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannelName() {
        return channel_name;
    }

    public void setChannelName(String channel_name) {
        this.channel_name = channel_name;
    }

    @Override
    public String toString() {
        return "ChannelYoutube{" +
                "channel_id='" + channel_id + '\'' +
                ", channel_name='" + channel_name + '\'' +
                '}';
    }
}
