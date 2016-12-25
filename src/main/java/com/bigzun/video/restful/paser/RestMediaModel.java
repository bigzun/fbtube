package com.bigzun.video.restful.paser;

import android.content.Context;

import com.bigzun.video.model.MediaModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 10/18/2016.
 */

public class RestMediaModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String TAG = RestMediaModel.class.getSimpleName();

    @SerializedName("data")
    private MediaModel data;

    public MediaModel getData() {
        return data;
    }

    public MediaModel getData(Context context) {
        return data;
    }

    @Override
    public String toString() {
        return TAG + ":{ " + data + " }";
    }
}
