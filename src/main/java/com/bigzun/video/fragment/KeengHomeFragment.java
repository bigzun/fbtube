package com.bigzun.video.fragment;

import android.app.Activity;
import android.os.Bundle;
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

public class KeengHomeFragment extends RecyclerFragment<MediaModel> {
    private int currentPage = 1;
    private int numPerPage = 10;
    private static KeengHomeFragment mInstance;
    private MainActivity mActivity;
    private KeengAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    public static KeengHomeFragment newInstance() {
        if (mInstance == null)
            mInstance = new KeengHomeFragment();
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
        if (getDatas().isEmpty())
            doLoadData(true);
//        }
    }

    protected void loadData() {
        WSRestful rest = new WSRestful(mActivity);
        rest.loadKeengHome(callback, currentPage, numPerPage);
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
