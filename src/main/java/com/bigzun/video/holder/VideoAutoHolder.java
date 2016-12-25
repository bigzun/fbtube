package com.bigzun.video.holder;

import android.view.View;

import com.bigzun.video.R;
import com.bigzun.video.control.VideoView;
import com.bigzun.video.listener.ClickItemListener;

import butterknife.BindView;

/**
 * Created by namnh40 on 12/16/2016.
 */

public class VideoAutoHolder extends MediaHolder {

    @BindView(R.id.player_view)
    public VideoView playerView;

    @BindView(R.id.info_layout)
    public View infoView;

    public VideoAutoHolder(View itemView, ClickItemListener listener) {
        super(itemView, listener);
    }

    public void showControlVideo() {
        infoView.setVisibility(View.GONE);
        playerView.setVisibility(View.VISIBLE);
    }

    public void hideControlVideo() {
        infoView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
    }

}
