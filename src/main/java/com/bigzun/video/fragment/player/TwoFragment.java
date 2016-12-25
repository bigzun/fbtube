package com.bigzun.video.fragment.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigzun.video.R;
import com.bigzun.video.activity.PlayerActivity;
import com.bigzun.video.fragment.BaseFragment;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;

public class TwoFragment extends BaseFragment {

    private TextView txtContent;
    private PlayerActivity mPlayerActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPlayerActivity = (PlayerActivity) activity;
    }

    public static TwoFragment newInstance() {
        return new TwoFragment();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive");
            am = mPlayerActivity.mMedia;
            setLyricCurrent();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "registerReceiver");
        mPlayerActivity.registerReceiver(receiver, new IntentFilter(Constants.RECEIVER_MANAGER.MESSAGE_UPDATE_DATA_PLAYER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "unregisterReceiver");
        mPlayerActivity.unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisibled)
            if (mPlayerActivity.mMedia != null) {
                if (am != null) {
                    if (!am.getId().equals(mPlayerActivity.mMedia.getId())) {
                        am = mPlayerActivity.mMedia;
                        setLyricCurrent();
                    }
                } else {
                    am = mPlayerActivity.mMedia;
                    setLyricCurrent();
                }
            }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_two, container, false);
        txtContent = (TextView) view.findViewById(R.id.content);
        return view;
    }

    private MediaModel am;

    private void setLyricCurrent() {
        if (am == null) {
            normalAndSetLyric("");
        } else if (!TextUtils.isEmpty(am.getLyric())) {
            normalAndSetLyric(am.getLyric());
        } else {
            //TODO goi api lay loi bai hat
        }
    }

    private void normalAndSetLyric(String lyric) {
        if (txtContent == null)
            return;
        if (lyric != "") {
            txtContent.setText(Html.fromHtml(lyric));
            txtContent.setMovementMethod(new ScrollingMovementMethod());
        } else {
            lyric = "Loi bai hat dang duoc cap nhat.";
            txtContent.setText(Html.fromHtml(lyric));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
