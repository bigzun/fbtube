package com.bigzun.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bigzun.video.activity.BaseActivity;
import com.bigzun.video.adapter.AutoPlayAdapter;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.WSRestful;
import com.bigzun.video.restful.paser.RestAllMediaModel;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class AutoPlayFragment extends RecyclerFragment<MediaModel> {
    private int currentPage = 1;
    private int numPerPage = 10;
    private static AutoPlayFragment mInstance;
    private BaseActivity mActivity;
    private AutoPlayAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    public static AutoPlayFragment newInstance() {
        if (mInstance == null)
            mInstance = new AutoPlayFragment();
        return mInstance;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new AutoPlayAdapter(mActivity, getDatas(), TAG);
        setupRecyclerView(mAdapter);
        mAdapter.setRecyclerView(mRecyclerView, this);
        mAdapter.setOnScrollListener(mRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDatas().isEmpty())
            doLoadData(true);

        if (mAdapter != null)
            mAdapter.onResume();
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
        if (mAdapter != null)
            mAdapter.onRefresh();
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
        Toast.makeText(mActivity, "Click position " + position);
//        MediaModel item = mAdapter.getItem(position);
//        mActivity.clickMediaItem(item);
    }

    @Override
    public void onLongClickItem(View view, int position) {

    }

    @Override
    public void onPause() {
        if (mAdapter != null)
            mAdapter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null)
            mAdapter.onDestroy();
        super.onDestroy();
    }
}
