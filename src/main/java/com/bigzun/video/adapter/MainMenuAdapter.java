package com.bigzun.video.adapter;

import android.content.Context;

import com.bigzun.video.holder.BaseHolder;
import com.bigzun.video.holder.MainMenuHolder;
import com.bigzun.video.restful.paser.RestGuideCategories;
import com.bigzun.video.util.Log;

import java.util.List;

/**
 * Created by namnh40 on 10/13/2016.
 */

public class MainMenuAdapter extends BaseRecyclerAdapter {
    private List<RestGuideCategories.Item> mData;

    public MainMenuAdapter(Context context, List<RestGuideCategories.Item> data) {
        super(context, "MainMenu");
        mData = data;
    }

    @Override
    public RestGuideCategories.Item getItem(int position) {
        try {
            if (mData != null)
                return mData.get(position);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_MAIN_MENU;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (holder != null && holder instanceof MainMenuHolder) {
            MainMenuHolder itemHolder = (MainMenuHolder) holder;
            RestGuideCategories.Item item = getItem(position);
            if (item != null && itemHolder != null) {
                itemHolder.mTvTitle.setText(item.getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }
}
