package com.bigzun.video.fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.R;
import com.bigzun.video.activity.MainActivity;
import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.helper.EventLogger;
import com.bigzun.video.helper.PermissionHelper;
import com.bigzun.video.listener.OnPlayerMediaServiceListener;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 10/18/2016.
 */

public class VideoPlayerFragment extends BaseFragment implements ExoPlayer.EventListener, OnPlayerMediaServiceListener {
    protected MainActivity mActivity;
    protected MediaModel mVideoItem;
    private static VideoPlayerFragment mInstance;
    protected boolean isYoutubePlayer = false;

    public static VideoPlayerFragment newInstance() {
        if (mInstance == null)
            mInstance = new VideoPlayerFragment();
        return mInstance;
    }

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;

    private Handler mHandler;
    private EventLogger mEventLogger;
    private DataSource.Factory mDataSourceFactory;
    private SimpleExoPlayer mPlayer;
    private MappingTrackSelector trackSelector;
    private boolean autoPlay;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    public int getLayoutId() {
        return R.layout.fragment_video_player;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null, false);
        ButterKnife.bind(this, view);
        ButterKnife.setDebug(BuildConfig.DEBUG);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        autoPlay = true;
        mDataSourceFactory = buildDataSourceFactory(true);
        mHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isYoutubePlayer && mPlayer != null) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isYoutubePlayer && mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopVideo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doShowVideo();
        } else {
            Toast.makeText(mActivity, R.string.storage_permission_denied);
            mActivity.finish();
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.e(TAG, "onLoadingChanged: " + isLoading);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                break;
            case ExoPlayer.STATE_ENDED:
                mActivity.autoPlayNextVideo();
                break;
            case ExoPlayer.STATE_IDLE:
                break;
            case ExoPlayer.STATE_READY:
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        Log.e(TAG, "onPositionDiscontinuity");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        Log.e(TAG, "onPlayerError", e);
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        Toast.makeText(mActivity, errorString);
    }

    protected DataSource.Factory buildDataSourceFactory(boolean bandwidthMeter) {
        return ApplicationController.getInstance().buildDataSourceFactory(bandwidthMeter ? BANDWIDTH_METER : null);
    }

    protected MediaSource buildMediaSource(Uri uri) {
        if (uri != null) {
            int type = Util.inferContentType(uri.getLastPathSegment());
            switch (type) {
                case C.TYPE_SS:
                    return new SsMediaSource(uri, buildDataSourceFactory(false),
                            new DefaultSsChunkSource.Factory(mDataSourceFactory), mHandler, mEventLogger);
                case C.TYPE_DASH:
                    return new DashMediaSource(uri, buildDataSourceFactory(false),
                            new DefaultDashChunkSource.Factory(mDataSourceFactory), mHandler, mEventLogger);
                case C.TYPE_HLS:
                    return new HlsMediaSource(uri, mDataSourceFactory, mHandler, mEventLogger);
                case C.TYPE_OTHER:
                    return new ExtractorMediaSource(uri, mDataSourceFactory, new DefaultExtractorsFactory(),
                            mHandler, mEventLogger);
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }
        }
        throw new IllegalStateException("Uri is null");
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            autoPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
            trackSelector = null;
            mEventLogger = null;
        }
    }

    protected void doShowVideo() {
        //releasePlayer();
        hideYoutubeVideo();

        Log.e(TAG, "doShowVideo MediaUrl: " + mVideoItem);
        if (mPlayer == null) {
            TrackSelection.Factory mTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(mTrackSelectionFactory);
            mEventLogger = new EventLogger(trackSelector);
            mPlayer = ExoPlayerFactory.newSimpleInstance(mActivity, trackSelector, new DefaultLoadControl());
            mPlayer.addListener(this);
            mPlayer.addListener(mEventLogger);
            mPlayer.setAudioDebugListener(mEventLogger);
            mPlayer.setVideoDebugListener(mEventLogger);
            mPlayer.setId3Output(mEventLogger);
            mPlayer.setPlayWhenReady(autoPlay);
            mPlayerView.setPlayer(mPlayer);
        }
        Uri uri = Uri.parse(mVideoItem.getMediaUrl());
        if (Util.maybeRequestReadExternalStoragePermission(mActivity, uri)) {
            PermissionHelper.requestPermission(mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Constants.REQUEST_READ_EXTERNAL_STORAGE_VIDEO_PLAYER);
            return;
        }
        MediaSource videoSource = buildMediaSource(uri);
        Uri subtitleUri = Uri.parse(mVideoItem.getSubTitle());
        if (Util.maybeRequestReadExternalStoragePermission(mActivity, subtitleUri)) {
            PermissionHelper.requestPermission(mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Constants.REQUEST_READ_EXTERNAL_STORAGE_VIDEO_PLAYER);
            return;
        }
//        MediaSource subtitleSource = new SingleSampleMediaSource(subtitleUri, mDataSourceFactory,
//                Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, null,
//                        Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, ApplicationController.getInstance().getLanguageCode(), null, 0), 50000);
//        MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
        mPlayer.prepare(videoSource, true, true);
    }

    public boolean isFullScreen() {
        return false;
    }

    public void toggleScreen() {

    }

    public void setVideo(MediaModel item) {
        mActivity.stopAudio();
        if (mVideoItem != null && item != null
                && !TextUtils.isEmpty(item.getId()) && mVideoItem.getId().equals(item.getId())) {
            return;
        }
        stopVideo();
        mVideoItem = item;
        if (mVideoItem == null || TextUtils.isEmpty(mVideoItem.getId())) {
            return;
        } else {
            isYoutubePlayer = mVideoItem.isYoutube();
            if (!isYoutubePlayer)
                doShowVideo();
            else
                doShowYoutubeVideo();
        }
    }

    public void stopVideo() {
        if (!isYoutubePlayer) {
            releasePlayer();
        } else {
            stopYoutubeVideo();
        }
        isYoutubePlayer = false;
    }

    protected void stopYoutubeVideo() {

    }

    protected void hideYoutubeVideo() {
        if (mPlayerView != null)
            mPlayerView.setVisibility(View.VISIBLE);
    }

    protected void doShowYoutubeVideo() {
        if (mPlayerView != null)
            mPlayerView.setVisibility(View.GONE);
    }

    @Override
    public void onDataLoaded(MediaPlaying list, int position) {

    }

    @Override
    public void onDataLoaderFailed() {

    }

    @Override
    public void onPlayerPreparingState(int position) {

    }

    @Override
    public void onPlayerPreparedState(long duration) {

    }

    @Override
    public void onPlayerBufferingState() {

    }

    @Override
    public void onPlayerPlayingState() {

    }

    @Override
    public void onPlayerPausedState() {

    }

    @Override
    public void onProgressBarChanged(long currentPosition, int currentPercentage, int bufferedPercentage) {

    }

    @Override
    public void onRepeatStateChanged(int state) {

    }

    @Override
    public void onFinishAll() {

    }

}
