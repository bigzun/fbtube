package com.bigzun.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bigzun.video.activity.MainActivity;
import com.bigzun.video.adapter.KeengAdapter;
import com.bigzun.video.event.MessageEvent;
import com.bigzun.video.listener.OnPlayerMediaServiceListener;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.paser.RestMediaModel;
import com.bigzun.video.service.PlayMediaService;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Admin on 10/18/2016.
 */

public class VideoInfoFragment extends RecyclerFragment<MediaModel> implements OnPlayerMediaServiceListener {
    private static VideoInfoFragment mInstance;
    private MainActivity mActivity;
    private MediaModel mVideoItem;
    private KeengAdapter mAdapter;
    private final int INDEX_NEXT_VIDEO = 1;
    public MediaPlaying mMediaPlaying;

    public static VideoInfoFragment newInstance() {
        if (mInstance == null)
            mInstance = new VideoInfoFragment();
        return mInstance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity)
            mActivity = (MainActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new KeengAdapter(mActivity, getDatas(), TAG);
        setupRecyclerView(mAdapter);
        mAdapter.setRecyclerView(mRecyclerView, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        offRefresh(true);
    }

    private RestCallback callback = new RestCallback<RestMediaModel>() {
        @Override
        public void onResponse(Call<RestMediaModel> call, Response<RestMediaModel> response) {
            super.onResponse(call, response);
            if (response.body() != null && response.body().getData() != null)
                doAddResult(response.body().getData(mActivity).getMediaList());
            else
                doAddResult(null);
        }

        @Override
        public void onFailure(Call<RestMediaModel> call, Throwable throwable) {
            super.onFailure(call, throwable);
            doAddResult(null);
        }
    };

    protected void doAddResult(ArrayList<MediaModel> result) {
        Log.d(TAG, "doAddResult ...............");
        errorCount = 0;
        isLoading = false;
        try {
            mAdapter.setLoaded();
            checkLoadMore();
            if (result == null) {
                if (isRefresh) {
                    isRefresh = false;
                }
                loadMored();
                loadingError(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doLoadData(true);
                    }
                });
                return;
            }
            if (getDatas().size() == 0 && result.size() == 0) {
                loadMored();
                loadingEmpty();
                refreshed();
            } else {
                refreshed();
                loadMored();
                loadingFinish();
                setDatas(result);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void setVideo(MediaModel item) {
        clearData();
        mAdapter.notifyDataSetChanged();
        mVideoItem = item;
        if (mVideoItem != null)
            doLoadData(true);
    }

    public MediaModel getNextVideo() {
        MediaModel item = null;
        try {
            if (!getDatas().isEmpty()) {
                if (getDatas().size() == 1)
                    return getDatas().get(0);
                else
                    return getDatas().get(INDEX_NEXT_VIDEO);
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }

        return item;
    }

    @Override
    protected void loadData() {
//        WSRestful rest = new WSRestful(mActivity);
//        rest.loadKeengVideo(callback, mVideoItem.getId());
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
        MediaModel item = mAdapter.getItem(position);
        mActivity.clickMediaItem(item);
    }

    @Override
    public void onLongClickItem(View view, int position) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onDataLoaded(final MediaPlaying list, int position) {
        Log.d(TAG, "onDataLoaded --------------------");
        try {
            mMediaPlaying = list;
            if (mMediaPlaying != null && mMediaPlaying.getMediaList() != null && mMediaPlaying.getMediaList().size() > 0)
                mVideoItem = mMediaPlaying.getMediaList().get(position);
            else
                mVideoItem = null;
            setDatas(mMediaPlaying.getMediaList());
            mAdapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void onDataLoaderFailed() {

    }

    @Override
    public void onPlayerPreparingState(int position) {

    }

    @Override
    public void onPlayerPreparedState(long duration) {

    }

    @Override
    public void onPlayerBufferingState() {

    }

    @Override
    public void onPlayerPlayingState() {

    }

    @Override
    public void onPlayerPausedState() {

    }

    @Override
    public void onProgressBarChanged(long currentPosition, int currentPercentage, int bufferedPercentage) {

    }

    @Override
    public void onRepeatStateChanged(int state) {

    }

    @Override
    public void onFinishAll() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MessageEvent event) {
        if (event != null) {
            if (event.getState() == PlayMediaService.STATE_DATA_LOADER_SUCCESSFUL) {
                mMediaPlaying = event.getMediaPlaying();
                if (mMediaPlaying != null) {
                    setDatas(mMediaPlaying.getMediaList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    clearData();
                    mAdapter.notifyDataSetChanged();
                }
            } else if (event.getState() == PlayMediaService.STATE_DATA_LOADER_FAILED) {
                Toast.makeText(mActivity, "Lay du lieu that bai");
            }
        }
    }
}
