package com.bigzun.video.adapter;

import android.content.Context;

import com.bigzun.video.holder.BaseHolder;
import com.bigzun.video.holder.MediaHolder;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.util.Log;

import java.util.List;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class KeengAdapter extends BaseRecyclerAdapter {
    private List<MediaModel> mData;

    public KeengAdapter(Context context, List<MediaModel> data, String tag) {
        super(context, tag);
        mData = data;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        final MediaModel item = getItem(position);
        if (holder instanceof MediaHolder && item != null) {
            final MediaHolder itemHolder = (MediaHolder) holder;
            itemHolder.bind(item);
            switch (item.getType()) {
                case song:
                    mImageBusiness.setSong(itemHolder.imvImage, item.getImage());
                    break;
                case album:
                    mImageBusiness.setAlbum(itemHolder.imvImage, item.getImage());
                    break;
                case video:
                    mImageBusiness.setVideo(itemHolder.imvImage, item.getImage());
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        MediaModel item = getItem(position);
        if (item == null) {
            return ITEM_LOAD_MORE;
        }
        switch (item.getType()) {
            case song:
                return ITEM_MEDIA_SONG;
            case album:
                return ITEM_MEDIA_ALBUM;
            case video:
                return ITEM_MEDIA_VIDEO;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    public MediaModel getItem(int position) {
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
