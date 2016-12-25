package com.bigzun.video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.bigzun.video.business.ImageBusiness;
import com.bigzun.video.util.Log;

/**
 * Created by namnh40 on 10/13/2016.
 */

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected final String TAG = getClass().getSimpleName();
    protected final ImageBusiness mImageBusiness;
    protected Context mContext;
    protected final String fragTag; //TODO ten cua man hinh goi adapter

    public BaseAdapter(Context context, String tag) {
        this.mImageBusiness = new ImageBusiness(context);
        this.mContext = context;
        this.fragTag = tag;
        Log.d(TAG, "new adapter for screen " + fragTag + " -----");
    }
}
