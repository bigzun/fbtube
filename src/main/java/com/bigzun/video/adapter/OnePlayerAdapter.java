package com.bigzun.video.adapter;

import android.app.Activity;

import com.bigzun.video.holder.BaseHolder;
import com.bigzun.video.holder.MediaHolder;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.util.Log;

import java.util.List;

public class OnePlayerAdapter extends BaseRecyclerAdapter {
//implements IOListener.ItemTouchHelperAdapter {

    public List<MediaModel> mData;

    public OnePlayerAdapter(Activity activity, List<MediaModel> data, String tag) {
        super(activity, tag);
        this.mData = data;
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

    @Override
    public int getItemViewType(int position) {
        MediaModel item = getItem(position);
        if (item == null) {
            return ITEM_LOAD_MORE;
        }
        return ITEM_MEDIA_SONG;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        final MediaModel item = getItem(position);
        if (holder instanceof MediaHolder && item != null) {
            final MediaHolder itemHolder = (MediaHolder) holder;
            itemHolder.bind(item);
            mImageBusiness.setSong(itemHolder.imvImage, item.getImage());
        }
    }

}
