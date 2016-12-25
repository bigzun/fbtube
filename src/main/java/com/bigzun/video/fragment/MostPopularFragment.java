package com.bigzun.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigzun.video.activity.MainActivity;
import com.bigzun.video.adapter.MostPopularAdapter;
import com.bigzun.video.helper.ConvertMediaHelper;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.WSRestful;
import com.bigzun.video.restful.paser.RestMostPopularVideo;
import com.bigzun.video.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class MostPopularFragment extends RecyclerFragment<RestMostPopularVideo.Items> {
    private String tokenPage = "";
    private int maxResults = 30;
    private static MostPopularFragment mInstance;
    private MainActivity mActivity;
    private MostPopularAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    public static MostPopularFragment newInstance() {
        if (mInstance == null)
            mInstance = new MostPopularFragment();
        return mInstance;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MostPopularAdapter(mActivity, getDatas(), TAG);
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
        rest.loadMostPopularVideo(callback, maxResults, tokenPage, 0);
    }

    private RestCallback callback = new RestCallback<RestMostPopularVideo>() {
        @Override
        public void onResponse(Call<RestMostPopularVideo> call, Response<RestMostPopularVideo> response) {
            super.onResponse(call, response);
            if (response.body() != null) {
                tokenPage = response.body().getNextPageToken();
                doAddResult(response.body().getItems());
            } else
                doAddResult(null);
        }

        @Override
        public void onFailure(Call<RestMostPopularVideo> call, Throwable throwable) {
            super.onFailure(call, throwable);
            doAddResult(null);
        }
    };

    protected void doAddResult(ArrayList<RestMostPopularVideo.Items> result) {
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

    @Override
    public void onRefresh() {
        isRefresh = true;
        canLoadMore = true;
        tokenPage = "";
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
        if (!TextUtils.isEmpty(tokenPage))
            canLoadMore = true;
        else
            canLoadMore = false;
    }

    @Override
    public void onClickItem(View view, int position) {
        RestMostPopularVideo.Items item = mAdapter.getItem(position);
        mActivity.clickMediaItem(ConvertMediaHelper.convertFromMostPopularVideo(item));
    }

    @Override
    public void onLongClickItem(View view, int position) {

    }

}
