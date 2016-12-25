package com.bigzun.video.restful.paser;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Admin on 12/25/2016.
 */

public class RestMostPopularVideo {

    @SerializedName("nextPageToken")
    private String mNextPageToken;
    @SerializedName("prevPageToken")
    private String mPrevPageToken;
    @SerializedName("items")
    private ArrayList<Items> mItems;

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String mNextPageToken) {
        this.mNextPageToken = mNextPageToken;
    }

    public String getPrevPageToken() {
        return mPrevPageToken;
    }

    public void setPrevPageToken(String mPrevPageToken) {
        this.mPrevPageToken = mPrevPageToken;
    }

    public ArrayList<Items> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Items> mItems) {
        this.mItems = mItems;
    }

    public static class Items {
        @SerializedName("kind")
        private String mKind;
        @SerializedName("id")
        private String mId;
        @SerializedName("snippet")
        private Snippet mSnippet;

        public String getKind() {
            return mKind;
        }

        public void setKind(String mKind) {
            this.mKind = mKind;
        }

        public String getId() {
            return mId;
        }

        public void setId(String mId) {
            this.mId = mId;
        }

        public Snippet getSnippet() {
            return mSnippet;
        }

        public void setSnippet(Snippet mSnippet) {
            this.mSnippet = mSnippet;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "mId='" + mId + '\'' +
                    ", mKind='" + mKind + '\'' +
                    ", mSnippet=" + mSnippet +
                    '}';
        }
    }

    public static class Snippet {
        @SerializedName("publishedAt")
        private String mPublishedAt;
        @SerializedName("channelId")
        private String mChannelId;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("description")
        private String mDescription;
        @SerializedName("thumbnails")
        private Thumbnails mThumbnails;
        @SerializedName("channelTitle")
        private String mChannelTitle;
        @SerializedName("tags")
        private ArrayList<String> mTags;
        @SerializedName("categoryId")
        private String mCategoryId;
        @SerializedName("liveBroadcastContent")
        private String mLiveBroadcastContent;

        public String getPublishedAt() {
            return mPublishedAt;
        }

        public void setPublishedAt(String mPublishedAt) {
            this.mPublishedAt = mPublishedAt;
        }

        public String getChannelId() {
            return mChannelId;
        }

        public void setChannelId(String mChannelId) {
            this.mChannelId = mChannelId;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        public Thumbnails getThumbnails() {
            return mThumbnails;
        }

        public void setThumbnails(Thumbnails mThumbnails) {
            this.mThumbnails = mThumbnails;
        }

        public String getChannelTitle() {
            return mChannelTitle;
        }

        public void setChannelTitle(String mChannelTitle) {
            this.mChannelTitle = mChannelTitle;
        }

        public ArrayList<String> getTags() {
            return mTags;
        }

        public void setTags(ArrayList<String> mTags) {
            this.mTags = mTags;
        }

        public String getCategoryId() {
            return mCategoryId;
        }

        public void setCategoryId(String mCategoryId) {
            this.mCategoryId = mCategoryId;
        }

        public String getLiveBroadcastContent() {
            return mLiveBroadcastContent;
        }

        public void setLiveBroadcastContent(String mLiveBroadcastContent) {
            this.mLiveBroadcastContent = mLiveBroadcastContent;
        }

        @Override
        public String toString() {
            return "Snippet{" +
                    "mCategoryId='" + mCategoryId + '\'' +
                    ", mPublishedAt='" + mPublishedAt + '\'' +
                    ", mChannelId='" + mChannelId + '\'' +
                    ", mTitle='" + mTitle + '\'' +
                    ", mDescription='" + mDescription + '\'' +
                    ", mThumbnails=" + mThumbnails +
                    ", mChannelTitle='" + mChannelTitle + '\'' +
                    ", mTags=" + mTags +
                    ", mLiveBroadcastContent='" + mLiveBroadcastContent + '\'' +
                    '}';
        }
    }

    public static class Thumbnails {
        @SerializedName("default")
        private ImageObj mdefault;
        @SerializedName("medium")
        private ImageObj mMedium;
        @SerializedName("high")
        private ImageObj mHigh;
        @SerializedName("standard")
        private ImageObj mStandard;
        @SerializedName("maxres")
        private ImageObj mMaxres;

        public ImageObj getImage() {
            if (mMaxres != null && !TextUtils.isEmpty(mMaxres.getUrl()))
                return mMaxres;

            if (mStandard != null && !TextUtils.isEmpty(mStandard.getUrl()))
                return mStandard;

            if (mHigh != null && !TextUtils.isEmpty(mHigh.getUrl()))
                return mHigh;

            if (mMedium != null && !TextUtils.isEmpty(mMedium.getUrl()))
                return mMedium;

            if (mdefault != null && !TextUtils.isEmpty(mdefault.getUrl()))
                return mdefault;

            return new ImageObj();
        }

        @Override
        public String toString() {
            return "Thumbnails{" +
                    "mdefault=" + mdefault +
                    ", mMedium=" + mMedium +
                    ", mHigh=" + mHigh +
                    ", mStandard=" + mStandard +
                    ", mMaxres=" + mMaxres +
                    '}';
        }
    }

    public static class ImageObj {
        @SerializedName("url")
        private String mUrl = "";
        @SerializedName("width")
        private int mWidth = 0;
        @SerializedName("height")
        private int mHeight = 0;

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String mUrl) {
            this.mUrl = mUrl;
        }

        public int getWidth() {
            return mWidth;
        }

        public void setWidth(int mWidth) {
            this.mWidth = mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public void setHeight(int mHeight) {
            this.mHeight = mHeight;
        }

        @Override
        public String toString() {
            return "ImageObj{" +
                    "mUrl='" + mUrl + '\'' +
                    '}';
        }
    }

}
