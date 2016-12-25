package com.bigzun.video.restful.paser;

import android.content.Context;

import com.bigzun.video.model.MediaModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 10/18/2016.
 */

public class RestAllMediaModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String TAG = RestAllMediaModel.class.getSimpleName();

    @SerializedName("data")
    private ArrayList<MediaModel> data;

    public ArrayList<MediaModel> getData() {
        return data;
    }

    public ArrayList<MediaModel> getData(Context context) {
        return data;
    }

    @Override
    public String toString() {
        return TAG + ":[ " + data + " ]";
    }
}
