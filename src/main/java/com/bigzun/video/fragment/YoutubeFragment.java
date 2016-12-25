package com.bigzun.video.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.bigzun.video.R;
import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.util.Log;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.BindView;

/**
 * Created by namnh40 on 12/21/2016.
 */

public class YoutubeFragment extends VideoPlayerFragment implements YouTubePlayer.OnInitializedListener {

    private static YoutubeFragment mInstance;

    public static YoutubeFragment newInstance() {
        if (mInstance == null)
            mInstance = new YoutubeFragment();
        return mInstance;
    }

    @BindView(R.id.youtube_view)
    FrameLayout mYoutubeView;

    private YouTubePlayerSupportFragment mYouTubePlayerSupportFragment;
    private YouTubePlayer mYouTubePlayer;
    private YouTubePlaylistEventListener mYoutubePlaylistEventListener;
    private YouTubePlayerStateChangeListener mYouTubePlayerStateChangeListener;
    private YouTubePlaybackEventListener mYouTubePlaybackEventListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initYoutubeFragment();
    }

    protected void initYoutubeFragment() {
        mYouTubePlayerSupportFragment = new YouTubePlayerSupportFragment();
        mYouTubePlayerSupportFragment.initialize(ApplicationController.getInstance().getYoutubeKey(), this);
        mYoutubePlaylistEventListener = new YouTubePlaylistEventListener();
        mYouTubePlayerStateChangeListener = new YouTubePlayerStateChangeListener();
        mYouTubePlaybackEventListener = new YouTubePlaybackEventListener();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_view, mYouTubePlayerSupportFragment);
        transaction.commitAllowingStateLoss();
//        mYouTubePlayerSupportFragment.setMenuVisibility(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isYoutubePlayer && mYouTubePlayer != null) {
            mYouTubePlayer.play();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isYoutubePlayer && mYouTubePlayer != null) {
            mYouTubePlayer.pause();
        }
    }

    @Override
    protected void stopYoutubeVideo() {
        super.stopYoutubeVideo();
        if (isYoutubePlayer && mYouTubePlayer != null) {
            mYouTubePlayer.pause();
//            mYouTubePlayer.release();
        }
    }

    @Override
    protected void hideYoutubeVideo() {
        super.hideYoutubeVideo();
        if (mYoutubeView != null) {
            mYoutubeView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void doShowYoutubeVideo() {
        super.doShowYoutubeVideo();
        if (mYoutubeView != null) {
            mYoutubeView.setVisibility(View.VISIBLE);
        }
        if (isYoutubePlayer && mYouTubePlayer != null) {
            mYouTubePlayer.cueVideo(mVideoItem.getId());
        }
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "" : hours + ":")
                + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    private void updateText() {
        Log.e(TAG, String.format("Current state: %s %s %s",
                mYouTubePlayerStateChangeListener.playerState, mYouTubePlaybackEventListener.playbackState,
                mYouTubePlaybackEventListener.bufferingState));
    }

    protected final class YouTubePlaylistEventListener implements YouTubePlayer.PlaylistEventListener {
        @Override
        public void onNext() {
            Log.d(TAG, "NEXT VIDEO");
        }

        @Override
        public void onPrevious() {
            Log.d(TAG, "PREVIOUS VIDEO");
        }

        @Override
        public void onPlaylistEnded() {
            Log.d(TAG, "PLAYLIST ENDED");
        }
    }

    protected final class YouTubePlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
        String playbackState = "NOT_PLAYING";
        String bufferingState = "";

        @Override
        public void onPlaying() {
            playbackState = "PLAYING";
            updateText();
        }

        @Override
        public void onBuffering(boolean isBuffering) {
            bufferingState = isBuffering ? "(BUFFERING)" : "";
            updateText();
        }

        @Override
        public void onStopped() {
            playbackState = "STOPPED";
            updateText();
        }

        @Override
        public void onPaused() {
            playbackState = "PAUSED";
            updateText();
        }

        @Override
        public void onSeekTo(int endPositionMillis) {
            Log.e(TAG, String.format("\tSEEKTO: (%s/%s)",
                    formatTime(endPositionMillis),
                    formatTime(mYouTubePlayer.getDurationMillis())));
        }
    }

    protected final class YouTubePlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
        String playerState = "UNINITIALIZED";

        @Override
        public void onLoading() {
            playerState = "LOADING";
            updateText();
        }

        @Override
        public void onLoaded(String videoId) {
            playerState = String.format("LOADED %s", videoId);
            updateText();
        }

        @Override
        public void onAdStarted() {
            playerState = "AD_STARTED";
            updateText();
        }

        @Override
        public void onVideoStarted() {
            playerState = "VIDEO_STARTED";
            updateText();
        }

        @Override
        public void onVideoEnded() {
            playerState = "VIDEO_ENDED";
            updateText();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason reason) {
            playerState = "ERROR (" + reason + ")";
            if (reason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                mYouTubePlayer = null;
            }
            updateText();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;
        mYouTubePlayer.setPlaylistEventListener(mYoutubePlaylistEventListener);
        mYouTubePlayer.setPlayerStateChangeListener(mYouTubePlayerStateChangeListener);
        mYouTubePlayer.setPlaybackEventListener(mYouTubePlaybackEventListener);
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(mActivity, 1000).show();
        } else {
            Log.e(TAG, "onInitializationFailure: " + youTubeInitializationResult);
        }
    }
}
