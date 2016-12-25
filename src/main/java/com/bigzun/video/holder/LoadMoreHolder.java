package com.bigzun.video.holder;

import android.view.View;

import com.bigzun.video.R;
import com.bigzun.video.control.ProgressWheel;

public class LoadMoreHolder extends BaseHolder {
    public ProgressWheel mProgressWheel;

    public LoadMoreHolder(View itemView) {
        super(itemView, null);
        mProgressWheel = (ProgressWheel) itemView.findViewById(R.id.progress_wheel);
    }

}
