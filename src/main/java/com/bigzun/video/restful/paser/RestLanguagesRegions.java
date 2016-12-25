

package com.bigzun.video.restful.paser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RestLanguagesRegions implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String TAG = RestLanguagesRegions.class.getSimpleName();

    @SerializedName("items")
    private List<Item> data;

    public List<Item> getData() {
        return data;
    }

    public int size() {
        if (data != null)
            return data.size();
        return 0;
    }

    public static class Item {

        @SerializedName("snippet")
        private Snippet snippet;

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }

        public String getName() {
            if (snippet != null)
                return snippet.getName();
            return null;
        }

        public String getRegionCode() {
            if (snippet != null)
                return snippet.getGl();
            return null;
        }

        public String getLanguageCode() {
            if (snippet != null)
                return snippet.getHl();
            return null;
        }

        @Override
        public String toString() {
            return "{ snippet: " + getSnippet() + " }";
        }
    }

    public static class Snippet {
        @SerializedName("hl")
        private String hl;

        @SerializedName("gl")
        private String gl;

        @SerializedName("name")
        private String name;

        public String getHl() {
            return hl;
        }

        public void setHl(String hl) {
            this.hl = hl;
        }

        public String getGl() {
            return gl;
        }

        public void setGl(String gl) {
            this.gl = gl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "{ hl: " + hl + ", gl: " + gl + ", name: " + name + " }";
        }
    }

    @Override
    public String toString() {
        return TAG + ":[ " + data + " ]";
    }
}