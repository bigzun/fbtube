package com.bigzun.video.adapter;

import android.content.Context;

import com.bigzun.video.holder.BaseHolder;
import com.bigzun.video.holder.MediaHolder;
import com.bigzun.video.restful.paser.RestMostPopularVideo;
import com.bigzun.video.util.Log;

import java.util.List;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class MostPopularAdapter extends BaseRecyclerAdapter {
    private List<RestMostPopularVideo.Items> mData;

    public MostPopularAdapter(Context context, List<RestMostPopularVideo.Items> data, String tag) {
        super(context, tag);
        mData = data;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        final RestMostPopularVideo.Items item = getItem(position);
        if (holder instanceof MediaHolder && item != null) {
            final MediaHolder itemHolder = (MediaHolder) holder;
            itemHolder.tvName.setText(item.getSnippet().getTitle());
            itemHolder.tvSinger.setText(item.getSnippet().getChannelTitle());
            mImageBusiness.setVideo(itemHolder.imvImage, item.getSnippet().getThumbnails().getImage().getUrl());
        }
    }

    @Override
    public int getItemViewType(int position) {
        RestMostPopularVideo.Items item = getItem(position);
        if (item == null) {
            return ITEM_LOAD_MORE;
        }
//        switch (item.getType()) {
//            case song:
//                return ITEM_MEDIA_SONG;
//            case album:
//                return ITEM_MEDIA_ALBUM;
//            case video:
//                return ITEM_MEDIA_VIDEO;
//        }
        return ITEM_MEDIA_VIDEO;
//        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    public RestMostPopularVideo.Items getItem(int position) {
        if (mData != null) {
            try {
                return mData.get(position);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        return null;
    }
}
