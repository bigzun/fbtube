package com.bigzun.video.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.R;
import com.bigzun.video.listener.ClickItemListener;
import com.bigzun.video.model.MediaModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class MediaHolder extends BaseHolder implements View.OnClickListener {
    private MediaModel item;

    @BindView(R.id.title)
    public TextView tvName;

    @BindView(R.id.singer)
    public TextView tvSinger;

    @BindView(R.id.image)
    public ImageView imvImage;

    public MediaHolder(View itemView, ClickItemListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }

    public void bind(MediaModel item) {
        this.item = item;
        if (item != null) {
            item.setPosition(getAdapterPosition());
            tvName.setText(item.getName());
            if (TextUtils.isEmpty(item.getSinger()))
                tvSinger.setVisibility(View.GONE);
            else {
                tvSinger.setVisibility(View.VISIBLE);
                tvSinger.setText(item.getSinger());
            }
        }
    }

    public MediaModel getItem() {
        return item;
    }

    @OnClick(R.id.btn_media)
    public void onClick(View view) {
        if (mClickItemListener != null)
            mClickItemListener.onClickItem(view, getAdapterPosition());
    }
}