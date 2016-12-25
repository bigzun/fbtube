package com.bigzun.video.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigzun.video.R;
import com.bigzun.video.holder.BaseHolder;
import com.bigzun.video.holder.LoadMoreHolder;
import com.bigzun.video.holder.MainMenuHolder;
import com.bigzun.video.holder.MediaHolder;
import com.bigzun.video.holder.VideoAutoHolder;
import com.bigzun.video.listener.ClickItemListener;
import com.bigzun.video.listener.LoadMoreListener;
import com.bigzun.video.util.Log;

/**
 * Created by namnh40 on 10/13/2016.
 */

public abstract class BaseRecyclerAdapter extends BaseAdapter<BaseHolder> {
    public static final int ITEM_MAIN_MENU = 0;
    public static final int ITEM_HOT = 1;
    public static final int ITEM_NORMAL = 2;

    public static final int ITEM_EMPTY = 10;
    public static final int ITEM_LOAD_MORE = 13;
    public static final int ITEM_MEDIA_VIDEO = 20;
    public static final int ITEM_MEDIA_ALBUM = 21;
    public static final int ITEM_MEDIA_SONG = 22;
    public static final int ITEM_MEDIA_PLAYLIST = 23;
    public static final int ITEM_MEDIA_VIDEO_AUTO = 24;
    private int numStartLoadMore = 5;
    private int lastVisibleItem, totalItemCount, previousTotal;
    private boolean loading;
    protected ClickItemListener mListener;

    public void setClickItemListener(ClickItemListener listener) {
        mListener = listener;
    }

    public BaseRecyclerAdapter(Context context, String tag) {
        super(context, tag);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            // header
            case ITEM_MAIN_MENU:
                view = LayoutInflater.from(mContext).inflate(R.layout.holder_main_menu, parent, false);
                return new MainMenuHolder(view, mListener);

            case ITEM_MEDIA_ALBUM:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_media_album, parent, false);
                return new MediaHolder(view, mListener);

            case ITEM_MEDIA_VIDEO:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_media_video, parent, false);
                return new MediaHolder(view, mListener);
            case ITEM_MEDIA_SONG:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_media_song, parent, false);
                return new MediaHolder(view, mListener);
            case ITEM_MEDIA_VIDEO_AUTO:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_media_video, parent, false);
                return new VideoAutoHolder(view, mListener);
            case ITEM_LOAD_MORE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
                return new LoadMoreHolder(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.holder_empty, parent, false);
                return new BaseHolder(view, mListener);
        }
    }

    public void setRecyclerView(RecyclerView recyclerView, final LoadMoreListener listener) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!loading && totalItemCount <= (lastVisibleItem + numStartLoadMore)) {
                        Log.d(TAG, "onScrolled loadMore");
                        if (listener != null) {
                            loading = true;
                            listener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    public void onScrollEnd(RecyclerView recyclerView, final LoadMoreListener listener) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (totalItemCount == lastVisibleItem + 1) {
                        if (listener != null) {
                            listener.onScrollEnd(true);
                        }
                    } else {
                        if (listener != null) {
                            listener.onScrollEnd(false);
                        }
                    }
                }
            });
        }
    }

    public Object getItem(int position) {
        return position;
    }

    public void setLoaded() {
        this.loading = false;
    }
}
