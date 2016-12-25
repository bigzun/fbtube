package com.bigzun.video.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigzun.video.R;
import com.bigzun.video.activity.BaseActivity;
import com.bigzun.video.adapter.BaseRecyclerAdapter;
import com.bigzun.video.adapter.DividerItemDecoration;
import com.bigzun.video.control.CustomLinearLayoutManager;
import com.bigzun.video.control.LoadingView;
import com.bigzun.video.listener.ClickItemListener;
import com.bigzun.video.listener.LoadMoreListener;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.SharedPref;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerFragment<T> extends BaseFragment implements OnRefreshListener,
        LoadMoreListener, ClickItemListener {

    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_NO_RESULT = 3;
    public static final int STATE_END_LIST = 4;
    public static final int STATE_COMPLETED_PAGE = 5;
    protected int errorCount = 0;
    protected int maxError = 2;
    protected SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected boolean isLoading;
    private CustomLinearLayoutManager layoutManager;
    protected SharedPref mPref;
    protected LoadingView loadingView;
    private List<T> mData;
    protected BaseRecyclerAdapter mAdapter;
    protected boolean isLoadingMore = false;
    protected boolean isRefresh = true;
    protected boolean canLoadMore = true;
    protected BaseActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
        mPref = new SharedPref(activity);
        if (mData == null)
            mData = new ArrayList<>();
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResoucreId(), container, false);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        return view;
    }

    protected int getLayoutResoucreId() {
        return R.layout.fragment_base;
    }

    public void setDatas(ArrayList<T> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(data);
    }

    public List<T> getDatas() {
        if (mData == null) {
            mData = new ArrayList<T>();
        }
        return mData;
    }

    public void clearData() {
        if (mData != null && mData.size() > 0) {
            mData.clear();
        } else if (mData == null) {
            mData = new ArrayList<>();
        }
    }

    public void setAdapter(BaseRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    public void setupRecyclerView(BaseRecyclerAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new CustomLinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickItemListener(this);
        mAdapter.onScrollEnd(mRecyclerView, this);
    }

    public void setupRecyclerView(BaseRecyclerAdapter adapter, int divider) {
        mAdapter = adapter;
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new CustomLinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        if (divider > 0)
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, divider));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickItemListener(this);
        mAdapter.onScrollEnd(mRecyclerView, this);
    }

    // ------------------Show/Hide Loading----------------------------------//
    public void refreshed() {
        if (isRefresh) {
            clearData();
            isRefresh = false;
        }
    }

    public void loadMore() {
        try {
            if (isLoadingMore || isLoading) {
                mRefreshLayout.setRefreshing(false);
            } else {
                isLoadingMore = true;
                getDatas().add(null);
                mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }

    }

    public void loadMored() {
        try {
            if (isLoadingMore) {
                isLoadingMore = false;
                if (getDatas().size() > 0) {
                    getDatas().remove(getDatas().size() - 1);
                    mAdapter.notifyItemRemoved(mAdapter.getItemCount() - 1);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void loadingBegin() {
        if (loadingView != null) {
            loadingView.loadBegin();
        }
    }

    public void loadingFinish() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (loadingView != null) {
            loadingView.loadFinish();
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void loadingError(View.OnClickListener paramOnClickListener) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (getDatas().size() > 0) {
            if (loadingView != null)
                loadingView.loadFinish();
            return;
        }
        if (loadingView != null) {
            loadingView.loadError();
            loadingView.setLoadingErrorListener(paramOnClickListener);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void loadingEmpty() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (getDatas().size() > 0) {
            if (loadingView != null)
                loadingView.loadFinish();
            return;
        }
        if (loadingView != null) {
            loadingView.loadEmpty();
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void loadingEmpty(String str) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (getDatas().size() > 0) {
            if (loadingView != null)
                loadingView.loadFinish();
            return;
        }
        if (loadingView != null) {
            loadingView.loadEmpty(str);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void loadingError(String error, View.OnClickListener paramOnClickListener) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (getDatas().size() > 0) {
            if (loadingView != null)
                loadingView.loadFinish();
            return;
        }
        if (loadingView != null) {
            loadingView.loadError(error);
            loadingView.setLoadingErrorListener(paramOnClickListener);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void loadingLogin(String str, View.OnClickListener paramOnClickListener) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (loadingView != null) {
            loadingView.loadLogin(str);
            loadingView.setBtnRetryListener(paramOnClickListener);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("deprecation")
    public void loadingNotice(String notice) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (getDatas().size() > 0) {
            if (loadingView != null)
                loadingView.loadFinish();
            return;
        }
        if (loadingView != null) {
            loadingView.setBackgroundColor(mActivity.getResources().getColor(R.color.transparent));
            loadingView.notice(notice);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        // if (mRecyclerView != null) {
        // mRecyclerView.setVisibility(View.GONE);
        // }
    }

    @Override
    public void onScrollEnd(boolean flag) {
        if (flag) {
            if (mActivity != null) {
                mActivity.hideBanner();
            }
        } else {
            if (mActivity != null) {
                mActivity.showBanner();
            }
        }
    }

    protected void doLoadData(boolean type) {
        if (!isLoading) {
            isLoading = true;
            if (type)
                loadingBegin();
            loadData();
        }
    }

    protected void loadDataFromCache(ArrayList<T> result) {
        Log.e(TAG, "loadDataFromCache -----------");
        if (result != null && result.size() > 0) {
            refreshed();
            loadMored();
            loadingFinish();
            setDatas(result);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void offRefresh(boolean frag) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(!frag);
        }
    }

    protected void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(listener);
        }
    }

    protected abstract void loadData();

    protected abstract void checkLoadMore();
}
