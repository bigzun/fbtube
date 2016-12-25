package com.bigzun.video.fragment.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.bigzun.video.activity.PlayerActivity;
import com.bigzun.video.adapter.OnePlayerAdapter;
import com.bigzun.video.fragment.RecyclerFragment;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;

public class OneFragment extends RecyclerFragment<MediaModel> {

    private PlayerActivity mActivity;
    private MediaPlaying mMediaPlaying;

    public static OneFragment newInstance() {
        return new OneFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof PlayerActivity)
            mActivity = (PlayerActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new OnePlayerAdapter(mActivity, getDatas(), TAG);
        setupRecyclerView(mAdapter);
        mAdapter.setRecyclerView(mRecyclerView, this);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "registerReceiver");
        mActivity.registerReceiver(receiver, new IntentFilter(Constants.RECEIVER_MANAGER.MESSAGE_UPDATE_DATA_PLAYER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "unregisterReceiver");
        mActivity.unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        offRefresh(true);
        loadData();
    }

    @Override
    protected void loadData() {
        mMediaPlaying = mActivity.getMediaPlaying();
        if (mMediaPlaying != null) {
            clearData();
            getDatas().addAll(mMediaPlaying.getMediaList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void checkLoadMore() {
        canLoadMore = false;
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onClickItem(View view, int position) {
        if (getDatas().size() >= position && position > -1) {
            mActivity.playNextMusic(position);
        }
    }

    @Override
    public void onLongClickItem(View view, int position) {

    }

    @Override
    public void onLoadMore() {

    }
}