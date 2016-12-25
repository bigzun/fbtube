package com.bigzun.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigzun.video.activity.MainActivity;
import com.bigzun.video.adapter.KeengAdapter;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.WSRestful;
import com.bigzun.video.restful.paser.RestAllMediaModel;
import com.bigzun.video.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class SongListFragment extends RecyclerFragment<MediaModel> {
    private int currentPage = 1;
    private int numPerPage = 10;
    private static SongListFragment mInstance;
    private MainActivity mActivity;
    private KeengAdapter mAdapter;
    private String currentId = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    public static SongListFragment newInstance() {
        if (mInstance == null)
            mInstance = new SongListFragment();
        return mInstance;
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
//        if (isVisibled) {
        doLoadData(true);
//        }
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
        isRefresh = true;
        doLoadData(true);
    }

    protected void loadData() {
        if (TextUtils.isEmpty(currentId))
            return;
        WSRestful rest = new WSRestful(mActivity);
        rest.loadKeengSong(callback, currentId);

    }

    private RestCallback callback = new RestCallback<RestAllMediaModel>() {
        @Override
        public void onResponse(Call<RestAllMediaModel> call, Response<RestAllMediaModel> response) {
            super.onResponse(call, response);
            if (response.body() != null)
                doAddResult(response.body().getData(mActivity));
            else
                doAddResult(null);
        }

        @Override
        public void onFailure(Call<RestAllMediaModel> call, Throwable throwable) {
            super.onFailure(call, throwable);
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
                currentPage++;
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        canLoadMore = true;
        currentPage = 1;
        doLoadData(false);
    }

    @Override
    public void onLoadMore() {
        if (!isLoading && !isRefresh && canLoadMore) {
            loadMore();
            doLoadData(false);
        } else {
            mAdapter.setLoaded();
        }
    }

    @Override
    protected void checkLoadMore() {
        canLoadMore = true;
    }

    @Override
    public void onClickItem(View view, int position) {
        MediaModel item = mAdapter.getItem(position);
        mActivity.clickMediaItem(item);
    }

    @Override
    public void onLongClickItem(View view, int position) {

    }

}
