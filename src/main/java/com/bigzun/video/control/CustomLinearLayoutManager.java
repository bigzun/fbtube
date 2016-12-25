package com.bigzun.video.control;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bigzun.video.util.Log;

/**
 * Created by namnh40 on 10/13/2016.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("CustomLinearLayoutManager", e);
        } catch (Exception e) {
            Log.e("CustomLinearLayoutManager", e);
        }
    }
}
