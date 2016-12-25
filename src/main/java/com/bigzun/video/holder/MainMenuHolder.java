package com.bigzun.video.holder;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.bigzun.video.R;
import com.bigzun.video.listener.ClickItemListener;

/**
 * Created by namnh40 on 10/13/2016.
 */

public class MainMenuHolder extends BaseHolder implements View.OnClickListener {
    public TextView mTvTitle;

    public MainMenuHolder(View itemView, ClickItemListener listener) {
        super(itemView, listener);
        mTvTitle = (TextView) itemView.findViewById(R.id.title);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mClickItemListener != null)
            mClickItemListener.onClickItem(view, getAdapterPosition());
    }
}
