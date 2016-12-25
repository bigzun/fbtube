

package com.bigzun.video.restful.paser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RestGuideCategories implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String TAG = RestGuideCategories.class.getSimpleName();

    @SerializedName("items")
    private List<Item> items;

    public List<Item> getData() {
        return items;
    }

    public int size() {
        if (items != null)
            return items.size();
        return 0;
    }

    public static class Item {
        @SerializedName("id")
        private String id;

        @SerializedName("snippet")
        private Snippet snippet;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }

        public String getChannelId() {
            if (snippet != null)
                return snippet.getChannelId();
            return null;
        }

        public String getTitle() {
            if (snippet != null)
                return snippet.getTitle();
            return null;
        }

        @Override
        public String toString() {
            return "{ id: " + getId() + "snippet: " + getSnippet() + " }";
        }
    }

    public static class Snippet {
        @SerializedName("channelId")
        private String channelId;

        @SerializedName("title")
        private String title;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "{ channelId: " + getChannelId() + ", title: " + getTitle() + " }";
        }
    }

    @Override
    public String toString() {
        return TAG + ":[ " + items + " ]";
    }
}