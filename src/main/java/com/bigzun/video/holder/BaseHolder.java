package com.bigzun.video.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigzun.video.listener.ClickItemListener;

public class BaseHolder extends RecyclerView.ViewHolder {
    protected ClickItemListener mClickItemListener;
    private View rootView;

    public BaseHolder(View itemView) {
        super(itemView);
        rootView = itemView;
    }

    public BaseHolder(View itemView, ClickItemListener listener) {
        super(itemView);
        rootView = itemView;
        mClickItemListener = listener;
    }

    public View getRootView() {
        return rootView;
    }

}
