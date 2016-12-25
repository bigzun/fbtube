package com.bigzun.video.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigzun.video.R;
import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.control.VideoView;
import com.bigzun.video.helper.EventLogger;
import com.bigzun.video.holder.BaseHolder;
import com.bigzun.video.holder.MediaHolder;
import com.bigzun.video.holder.VideoAutoHolder;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;
import com.bigzun.video.util.Utilities;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by namnh40 on 10/24/2016.
 */

public class AutoPlayAdapter extends BaseRecyclerAdapter implements ExoPlayer.EventListener {
    private List<MediaModel> mData;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    private Handler mHandler;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private EventLogger mEventLogger;
    private VideoView mPlayerView;
    private DataSource.Factory mDataSourceFactory;
    private SimpleExoPlayer mPlayer;
    private MappingTrackSelector trackSelector;
    private MediaModel mVideoItem;
    private boolean lastPlaying = false;
    private VideoAutoHolder mCurrentVideoHolder;
    private final int screenHeight;

    private Map<String, SimpleExoPlayer> mapPlayer;

    public AutoPlayAdapter(Context context, List<MediaModel> data, String tag) {
        super(context, tag);
        mData = data;
        mHandler = new Handler();
        mDataSourceFactory = buildDataSourceFactory(true);
        screenHeight = Utilities.getScreenHeight(context);
        mapPlayer = new HashMap<>();
    }

    private int currentPosition = -1;

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {

        if (holder instanceof MediaHolder) {
            final MediaModel item = getItem(position);
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
                return ITEM_MEDIA_VIDEO_AUTO;
            default:
                return ITEM_EMPTY;
        }
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

    public void setOnScrollListener(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (ApplicationController.getInstance().isWifiConnected()) {
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                        ArrayList<MediaModel> videoList = new ArrayList<>();
                        for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                            MediaModel item = getItem(i);
                            if (item != null && item.isVideo()) {
                                videoList.add(item);
                            }
                        }

                        if (videoList.isEmpty()) {
                            stopVideo();
                            return;
                        } else {
                            MediaModel item = videoList.get(0);
//                        Log.e(TAG, "checkCanPlay 2");
                            if (checkCanPlay(recyclerView, item.getPosition())) {
                                currentPosition = item.getPosition();
                                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(currentPosition);
                                VideoAutoHolder itemHolder = (VideoAutoHolder) holder;
                                doShowVideo(item, itemHolder);
                                return;
                            } else {
                                if (videoList.size() >= 2) {
                                    MediaModel item2 = videoList.get(1);
//                                Log.e(TAG, "checkCanPlay 3");
                                    if (checkCanPlay(recyclerView, item2.getPosition())) {
                                        currentPosition = item2.getPosition();
                                        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(currentPosition);
                                        VideoAutoHolder itemHolder = (VideoAutoHolder) holder;
                                        doShowVideo(item2, itemHolder);
                                        return;
                                    }
                                }
                            }
                        }
                    } else {
                        stopVideo();
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }

    public boolean checkCanPlay(RecyclerView recyclerView, int position) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        if (holder != null && holder instanceof VideoAutoHolder) {
            VideoAutoHolder itemHolder = (VideoAutoHolder) holder;
            float dyCurrent = 0;
            View currentView = itemHolder.getRootView();
            if (currentView != null) {
                dyCurrent = currentView.getY();
            }
            currentView = itemHolder.playerView;
            if (currentView != null) {
                int videoHeight = currentView.getHeight();
                float index = videoHeight / 2 + dyCurrent;
                //Log.e(TAG, "position / index / screenHeight: " + position + " / " + index + " / " + screenHeight + " -------------------------");
                if (index >= 0 && index <= screenHeight)
                    return true;
            }
        }
        return false;
    }

    public void stopVideo() {
        if (mCurrentVideoHolder != null)
            mCurrentVideoHolder.hideControlVideo();
        mPlayerView = null;
        mCurrentVideoHolder = null;
        releasePlayer();
        mVideoItem = null;
        currentPosition = -1;
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            trackSelector = null;
            mEventLogger = null;
        }
    }

    private void doShowVideo(MediaModel item, VideoAutoHolder holder) {
        if (mCurrentVideoHolder != null)
            mCurrentVideoHolder.hideControlVideo();

        if (holder == null) {
            releasePlayer();
            mVideoItem = null;
            mCurrentVideoHolder = null;
            return;
        }

//        mPlayerView.setPlayer(mPlayer);
        if ((mVideoItem != null && item != null && item.getId() == mVideoItem.getId())) {
            if (mCurrentVideoHolder != null)
                mCurrentVideoHolder.showControlVideo();
            return;
        }

        mCurrentVideoHolder = holder;
        mPlayerView = mCurrentVideoHolder.playerView;
        mVideoItem = item;
        releasePlayer();
        if (mVideoItem == null)
            return;

        Log.e(TAG, "doShowVideo MediaUrl: " + mVideoItem);
        if (mPlayer == null) {
            TrackSelection.Factory mTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(mTrackSelectionFactory);
            mEventLogger = new EventLogger(trackSelector);
            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, new DefaultLoadControl());
            mPlayer.addListener(this);
            mPlayer.addListener(mEventLogger);
            mPlayer.setAudioDebugListener(mEventLogger);
            mPlayer.setVideoDebugListener(mEventLogger);
            mPlayer.setId3Output(mEventLogger);
            mPlayerView.setPlayer(mPlayer);
            mPlayerView.setTitle(mVideoItem.getTitle());
            mPlayer.setPlayWhenReady(true);
        }
        Uri uri = Uri.parse(mVideoItem.getMediaUrl());
        MediaSource videoSource = buildMediaSource(uri);
        mPlayer.prepare(videoSource, true, true);
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

    public void onRefresh() {
        stopVideo();
    }

    public void onResume() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(lastPlaying);
        }
    }

    public void onPause() {
        lastPlaying = false;
        if (mPlayer != null && mPlayer.getPlayWhenReady()) {
            lastPlaying = true;
            mPlayer.setPlayWhenReady(false);
        }
    }

    public void onDestroy() {
        stopVideo();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                Toast.makeText(mContext, R.string.error_unsupported_video);
            }
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                Toast.makeText(mContext, R.string.error_unsupported_audio);
            }
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            if (mPlayer != null) {
                mPlayer.setPlayWhenReady(false);
                mPlayer.seekTo(0);
            }
        } else if (playbackState == ExoPlayer.STATE_READY) {
            if (mCurrentVideoHolder != null)
                mCurrentVideoHolder.showControlVideo();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = mContext.getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = mContext.getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = mContext.getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = mContext.getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        Toast.makeText(mContext, errorString);
        releasePlayer();
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
