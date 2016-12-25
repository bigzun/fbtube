package com.bigzun.video.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.bigzun.video.model.MediaModel;

/*
 * thienvd
 */
public abstract class IOListener {

    public interface OnClickHomeChannel {
        void onUploadClick(View v);

        void onFollowClick(View v);

        void onAvatarClick(View v);

        void onCoverClick(View v);

        void onEditClick(View v);

        void onCommentClick(View v);

        void onShareClick(View v);

        void onTitleClick(View v, int position);
    }

    public interface OnClickSingerHome extends OnClickHomeChannel {
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int parent, AdapterView<?> adapterview, int position);
    }

    public interface OnClickHeader extends OnItemClickListener {

        void onHeaderClick(View v, int position);

        void onFollowClick(View v, int position);

        void onClickMore(View v, Object model);
    }

    public interface OnClickItemExtend {
        void onlick(View v, MediaModel am);

        void onClickMore(View v, MediaModel am);
    }

    public interface OnClickMedia {
        public void onClickMedia(MediaModel am);

        public void onExpandChildClicked(View v, MediaModel am);
    }

    public interface OnNotifyChange {
        public void onNotifyChangeLikeComment(MediaModel am, int type);
    }

    public interface ItemTouchHelperViewHolder {
        void onItemSelected();

        void onItemClear();
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);

    }

    public interface OnShowAll {
        void onShowAll(View v, int position);
    }

    public interface ItemTouchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }
}
