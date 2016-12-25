package com.bigzun.video.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.bigzun.video.R;
import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.helper.EventLogger;
import com.bigzun.video.listener.OnPlayerMediaServiceListener;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.restful.paser.RestMediaModel;
import com.bigzun.video.util.SharedPref;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by namnh40 on 11/17/2016.
 */

public abstract class PlayMediaBaseService extends Service implements ExoPlayer.EventListener {

    protected final String TAG = getClass().getSimpleName();
    public static final int REPEAT_All = 0;
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_SUFF = 2;
    public static final int REPEAT_NONE = 4;

    public static final int STATE_IDLE = 0;
    public static final int STATE_BUFFERING = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_ENDED = 3;
    public static final int STATE_STOP_MEDIA = 4;
    public static final int STATE_DATA_LOADER_SUCCESSFUL = 5;
    public static final int STATE_DATA_LOADER_FAILED = 6;
    public static final int STATE_PLAYING = 9;
    public static final int STATE_PAUSED = 10;
    public static final int STATE_FINISH_ALL = 11;
    public static final int STATE_LOAD_AUDIO_ERROR = 12;
    public static final int STATE_LOAD_ALBUM_ERROR = 13;
    public static final int STATE_LOAD_PLAYLIST_ERROR = 14;
    public static final int UPDATE_PROGRESS = 15;
    public static final int UPDATE_REPEAT_STATE = 16;
    public static final int STATE_PREPARING = 17;
    public static final int STATE_LOAD_VIDEO_ERROR = 18;
    public static final int MEDIA_PLAYER_ID = 13579;

    protected static final int MAX_RETRY = 1;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static final int PROGRESS_BAR_MAX = 1000;
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;

    protected Call<RestMediaModel> mGetInfoRequest;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public int statePhone = TelephonyManager.CALL_STATE_IDLE;
    public int statePlaying = STATE_IDLE;
    protected String mCurrentMediaUrl = "";
    protected SimpleExoPlayer mMediaPlayer;
    protected int errorCount = 0;
    protected SharedPref config;
    private boolean autoPlay = true;
    protected boolean isTimelineStatic;
    protected int playerWindow;
    protected long playerPosition = 0;
    protected Timeline.Window currentWindow;
    protected int stateRepeat = REPEAT_All;
    protected int mCurrentMediaPosition = 0;
    protected final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    protected MappingTrackSelector trackSelector;
    protected DataSource.Factory mDataSourceFactory;
    protected EventLogger mEventLogger;
    protected Handler mHander;
    protected MediaPlaying mMediaPlaying = new MediaPlaying();
    protected ArrayList<OnPlayerMediaServiceListener> mServiceListeners = new ArrayList<>();

    public void addOnPlayerMediaServiceListener(OnPlayerMediaServiceListener listener) {
        this.mServiceListeners.add(listener);
    }

    public void removeOnPlayerMediaServiceListener(OnPlayerMediaServiceListener listener) {
        this.mServiceListeners.remove(listener);
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
                            new DefaultSsChunkSource.Factory(mDataSourceFactory), mHander, mEventLogger);
                case C.TYPE_DASH:
                    return new DashMediaSource(uri, buildDataSourceFactory(false),
                            new DefaultDashChunkSource.Factory(mDataSourceFactory), mHander, mEventLogger);
                case C.TYPE_HLS:
                    return new HlsMediaSource(uri, mDataSourceFactory, mHander, mEventLogger);
                case C.TYPE_OTHER:
                    return new ExtractorMediaSource(uri, mDataSourceFactory, new DefaultExtractorsFactory(),
                            mHander, mEventLogger);
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }
        }
        throw new IllegalStateException("Uri is null");
    }

    public void initPlayer() {
        mHander = new Handler();
        currentWindow = new Timeline.Window();
        TrackSelection.Factory mTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(mTrackSelectionFactory);
        mEventLogger = new EventLogger(trackSelector);
        mDataSourceFactory = buildDataSourceFactory(true);
        mMediaPlayer = ExoPlayerFactory.newSimpleInstance(ApplicationController.getInstance(), trackSelector, new DefaultLoadControl());
        mMediaPlayer.addListener(this);
        mMediaPlayer.addListener(mEventLogger);
        mMediaPlayer.setAudioDebugListener(mEventLogger);
        mMediaPlayer.setVideoDebugListener(mEventLogger);
        mMediaPlayer.setId3Output(mEventLogger);
//        if (isTimelineStatic) {
//            if (playerPosition == C.TIME_UNSET) {
//                mMediaPlayer.seekToDefaultPosition(playerWindow);
//            } else {
//                mMediaPlayer.seekTo(playerWindow, playerPosition);
//            }
//        }
        mMediaPlayer.setPlayWhenReady(autoPlay);
    }

    public void setAutoPlay(boolean flag) {
        autoPlay = flag;
    }

    protected void releasePlayer() {
        if (mMediaPlayer != null) {
            autoPlay = mMediaPlayer.getPlayWhenReady();
            playerWindow = mMediaPlayer.getCurrentWindowIndex();
            playerPosition = C.TIME_UNSET;
            Timeline timeline = mMediaPlayer.getCurrentTimeline();
            if (timeline != null && timeline.getWindow(playerWindow, currentWindow).isSeekable) {
                playerPosition = 0;
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            trackSelector = null;
            mEventLogger = null;
        }
    }

    public boolean isPlaying() {
        if (statePlaying == STATE_BUFFERING)
            return true;
        if (mMediaPlayer != null)
            return mMediaPlayer.getPlayWhenReady();
        return false;
    }

    public ExoPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public long getDuration() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getDuration();
        return 0;
    }

    public long getCurrenPosition() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    public int getBufferedPercentage() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getBufferedPercentage();
        return 0;
    }

    public int getPositionPercentage() {
        if (mMediaPlayer != null) {
            double duration = mMediaPlayer.getDuration();
            double currentPosition = mMediaPlayer.getCurrentPosition();

            return (int) (currentPosition / duration * 100);
        }
        return 0;
    }

    public MediaModel getCurrentMedia() {
        if (mMediaPlaying != null && !mMediaPlaying.isEmpty()) {
            try {
                return getMediaList().get(mCurrentMediaPosition);
            } catch (IndexOutOfBoundsException e) {
            } catch (Exception e) {
            }
        }
        return null;
    }

    public int getCurrentMediaPosition() {
        return mCurrentMediaPosition;
    }

    public MediaPlaying getMediaPlaying() {
        return mMediaPlaying;
    }

    public ArrayList<MediaModel> getMediaList() {
        if (mMediaPlaying != null)
            return mMediaPlaying.getMediaList();
        return null;
    }

    public int getRepeatState() {
        return stateRepeat;
    }

    CountDownTimer progressUpdateTimer = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long l) {
            runUpdatePlayerMediaServiceListener(UPDATE_PROGRESS);
        }

        @Override
        public void onFinish() {
            start();
        }
    };

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_IDLE:
                progressUpdateTimer.cancel();
                errorCount = 0;
                break;

            case ExoPlayer.STATE_BUFFERING:
                progressUpdateTimer.cancel();
                runUpdatePlayerMediaServiceListener(STATE_BUFFERING);
                break;

            case ExoPlayer.STATE_READY:
                runUpdatePlayerMediaServiceListener(STATE_READY);
                if (playWhenReady) {
                    runUpdatePlayerMediaServiceListener(STATE_PLAYING);
                    progressUpdateTimer.start();
                }
                break;

            case ExoPlayer.STATE_ENDED:
                progressUpdateTimer.cancel();
                errorCount = 0;
                playCompleted();
                break;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
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
        Toast.makeText(getBaseContext(), errorString);
        playMediaWhenError();
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    public abstract void playMedia(int position, boolean flag);

    public abstract void playNext();

    public abstract void playPrevious();

    public abstract void playRamdom();

    public abstract void playCompleted();

    public abstract void playFinish();

    public abstract void togglePlay(boolean flag);

    public abstract void setRepeatState(int state);

    public abstract void stopMedia();

    public abstract void pauseMedia();

    public abstract void playMedia();

    public abstract void playMediaWhenError();

    public abstract void onLoadDataInfoError(int state);

    public abstract void setMediaPlaying(MediaPlaying item, int position);

    public abstract void getDetailMediaPlaying(MediaPlaying item);

    public abstract void runUpdatePlayerMediaServiceListener(int state);

    public abstract void updateWidget(MediaModel item);

    public abstract void updateNotification(MediaModel item);

    public abstract void processMediaReceiver(Intent intent);
}
