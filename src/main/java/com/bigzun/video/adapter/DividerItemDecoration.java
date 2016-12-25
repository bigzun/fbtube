package com.bigzun.video.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by namnh40 on 10/13/2016.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    @SuppressWarnings("deprecation")
    public DividerItemDecoration(Context context, int divider) {
        mDivider = context.getResources().getDrawable(divider);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDivider == null)
            return;
        if (parent.getChildAdapterPosition(view) < 1)
            return;

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL)
            outRect.top = mDivider.getIntrinsicHeight();
        else
            throw new IllegalArgumentException("Only usable with vertical lists");
    }

    private int getOrientation(RecyclerView parent) {
        if (!(parent.getLayoutManager() instanceof LinearLayoutManager))
            throw new IllegalStateException("Layout manager must be an instance of LinearLayoutManager");
        return ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
