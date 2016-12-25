package com.bigzun.video.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.R;
import com.bigzun.video.activity.PlayerActivity;
import com.bigzun.video.activity.SplashActivity;
import com.bigzun.video.business.ImageBusiness;
import com.bigzun.video.event.MessageEvent;
import com.bigzun.video.listener.OnPlayerMediaServiceListener;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.receiver.SwipeNotifyReceiver;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.WSRestful;
import com.bigzun.video.restful.paser.RestMediaModel;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.exoplayer2.source.MediaSource;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by namnh40 on 11/17/2016.
 */

public class PlayMediaService extends PlayMediaBaseService {

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public PlayMediaService getService() {
            return PlayMediaService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "flags: " + flags + ", startId: " + startId + ", intent: " + intent + " --------");
        if (intent != null) {
            int widget = intent.getIntExtra(Constants.RECEIVER_MANAGER.ACTION_START_WIDGET, Constants.RECEIVER_MANAGER.VALUE_WIDGET);
            if (widget == Constants.RECEIVER_MANAGER.VALUE_WIDGET) {
                processMediaReceiver(intent);
            } else if (widget == Constants.RECEIVER_MANAGER.VALUE_UPDATE_WIDGET) {
                updateWidget(getCurrentMedia());
            } else if (widget == Constants.RECEIVER_MANAGER.VALUE_WIDGET_OPEN) {
                if (mMediaPlaying == null || getMediaList().isEmpty()) {
                    processMediaReceiver(intent);
                    updateWidget(getCurrentMedia());
                    Intent i = new Intent(this, SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, PlayerActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.KEY_DATA, mMediaPlaying);
                    i.putExtra(Constants.KEY_POSITION, mCurrentMediaPosition);
                    startActivity(i);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void playMedia(int position, boolean flag) {
        mCurrentMediaPosition = position;
        releasePlayer();
        initPlayer();
        try {
            MediaModel item = getCurrentMedia();
            if (item != null) {
                mCurrentMediaUrl = item.getMediaUrl();
                Log.e(TAG, "playMedia MediaUrl: " + mCurrentMediaUrl);
                runUpdatePlayerMediaServiceListener(STATE_PREPARING);
                Uri uri = Uri.parse(mCurrentMediaUrl);
                if (uri != null) {
                    MediaSource mediaSource = buildMediaSource(uri);
                    mMediaPlayer.prepare(mediaSource, true, true);
                    updateWidget(item);
                    updateNotification(item);
                } else {
                    errorCount++;
                    if (errorCount < MAX_RETRY)
                        playCompleted();
                    else
                        playFinish();
                }
            } else {
                errorCount++;
                if (errorCount < MAX_RETRY)
                    playCompleted();
                else
                    playFinish();
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void playNext() {
        try {
            int size = getMediaList().size();
            if (mCurrentMediaPosition < size - 2)
                ++mCurrentMediaPosition;
            else
                mCurrentMediaPosition = 0;
            playMedia(mCurrentMediaPosition, false);
        } catch (Exception e) {
            mCurrentMediaPosition = 0;
            Log.e(TAG, e);
        }
    }

    @Override
    public void playPrevious() {
        try {
            int size = getMediaList().size();
            if (size <= 0)
                mCurrentMediaPosition = 0;
            else if (mCurrentMediaPosition - 1 > 0)
                --mCurrentMediaPosition;
            else
                mCurrentMediaPosition = size - 1;
            playMedia(mCurrentMediaPosition, false);
        } catch (Exception e) {
            mCurrentMediaPosition = 0;
            Log.e(TAG, e);
        }
    }

    @Override
    public void playRamdom() {
        try {
            int size = getMediaList().size();
            Random mRandom = new Random();
            mCurrentMediaPosition = mRandom.nextInt(--size);
            playMedia(mCurrentMediaPosition, false);
        } catch (Exception e) {
            mCurrentMediaPosition = 0;
            Log.e(TAG, e);
        }
    }

    @Override
    public void playCompleted() {
        switch (stateRepeat) {
            case REPEAT_SUFF:
                playRamdom();
                break;
            case REPEAT_ONE:
                playMedia(mCurrentMediaPosition, false);
                break;
            case REPEAT_NONE:
                try {
                    int size = getMediaList().size();
                    if (mCurrentMediaPosition < size - 1) {
                        playNext();
                    } else {
                        playFinish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
                break;
            default:
                playNext();
                break;
        }
    }

    @Override
    public void playFinish() {
        runUpdatePlayerMediaServiceListener(STATE_FINISH_ALL);
        stopMedia();
    }

    @Override
    public void togglePlay(boolean flag) {
        if (isPlaying())
            pauseMedia();
        else
            playMedia();
    }

    @Override
    public void setRepeatState(int state) {
        runUpdatePlayerMediaServiceListener(UPDATE_REPEAT_STATE);
    }

    @Override
    public void stopMedia() {
        runUpdatePlayerMediaServiceListener(STATE_STOP_MEDIA);
        if (mMediaPlayer != null) {
            mMediaPlayer.setPlayWhenReady(false);
        }
        releasePlayer();
        Intent i = new Intent();
        i.setAction(Constants.RECEIVER_MANAGER.ACTION_BROADCAST);
        i.putExtra(Constants.RECEIVER_MANAGER.ACTION_NAME, Constants.RECEIVER_MANAGER.VALUE_FINISH);
        sendBroadcast(i);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void pauseMedia() {
        runUpdatePlayerMediaServiceListener(STATE_PAUSED);
        if (mMediaPlayer != null)
            mMediaPlayer.setPlayWhenReady(false);
    }

    @Override
    public void playMedia() {
        if (statePlaying != STATE_BUFFERING)
            runUpdatePlayerMediaServiceListener(STATE_PLAYING);
        if (mMediaPlayer != null)
            mMediaPlayer.setPlayWhenReady(true);
    }

    @Override
    public void playMediaWhenError() {
        errorCount++;
        if (errorCount < MAX_RETRY)
            playCompleted();
        else
            playFinish();
    }

    @Override
    public void onLoadDataInfoError(int state) {
        runUpdatePlayerMediaServiceListener(STATE_DATA_LOADER_FAILED);
        switch (state) {
            case STATE_LOAD_AUDIO_ERROR:
                Toast.makeText(getBaseContext(), "Load audio info error");
                break;

            case STATE_LOAD_ALBUM_ERROR:
                Toast.makeText(getBaseContext(), "Load album info error");
                break;

            case STATE_LOAD_PLAYLIST_ERROR:
                Toast.makeText(getBaseContext(), "Load playlist info error");
                break;
            case STATE_LOAD_VIDEO_ERROR:
                Toast.makeText(getBaseContext(), "Load video info error");
                break;

            default:
                Toast.makeText(getBaseContext(), "Load info error");
                break;
        }
    }

    @Override
    public void setMediaPlaying(MediaPlaying item, int position) {
        setAutoPlay(true);
        if (item == null) {
            mMediaPlaying = new MediaPlaying();
            mCurrentMediaPosition = 0;
            stopMedia();
            return;
        }
        if (mMediaPlaying != null && mMediaPlaying.getId() == item.getId())
            return;
        mMediaPlaying = item;
        mCurrentMediaPosition = position;
        if (mMediaPlaying.isEmpty()) {
            getDetailMediaPlaying(mMediaPlaying);
        } else {
            playMedia(mCurrentMediaPosition, false);
        }
    }

    @Override
    public void getDetailMediaPlaying(MediaPlaying item) {
        Log.d(TAG, "getDetailMediaPlaying: " + item);
        if (mGetInfoRequest != null)
            mGetInfoRequest.cancel();
        WSRestful rest;
        switch (item.getType()) {
            case MediaPlaying.TYPE_AUDIO:
                rest = new WSRestful(getBaseContext());
                mGetInfoRequest = rest.getAudioInfoRequest(item.getId());
                mGetInfoRequest.enqueue(mAudioCallback);
                break;

            case MediaPlaying.TYPE_ALBUM:
                rest = new WSRestful(getBaseContext());
                mGetInfoRequest = rest.getAlbumInfoRequest(item.getId());
                mGetInfoRequest.enqueue(mAlbumCallback);
                break;

            case MediaPlaying.TYPE_PLAYLIST:
                rest = new WSRestful(getBaseContext());
                mGetInfoRequest = rest.getPlaylistInfoRequest(item.getId());
                mGetInfoRequest.enqueue(mPlaylistCallback);
                break;

            case MediaPlaying.TYPE_VIDEO:
                rest = new WSRestful(getBaseContext());
                mGetInfoRequest = rest.getVideoInfoRequest(item.getId());
                mGetInfoRequest.enqueue(mVideoCallback);
                break;
        }
    }

    private RestCallback<RestMediaModel> mAudioCallback = new RestCallback<RestMediaModel>() {
        @Override
        public void onResponse(Call<RestMediaModel> call, Response<RestMediaModel> response) {
            super.onResponse(call, response);
            try {
                if (response.body() != null) {
                    MediaModel item = response.body().getData();
                    if (item != null) {
                        ArrayList<MediaModel> list = new ArrayList<>();
                        list.addAll(item.getMediaList());
                        item.setMediaList(null);
                        list.add(0, item);
                        mMediaPlaying.setMediaList(list);
                        mCurrentMediaPosition = 0;
                        runUpdatePlayerMediaServiceListener(STATE_DATA_LOADER_SUCCESSFUL);
                        playMedia(mCurrentMediaPosition, true);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onFailure(Call<RestMediaModel> call, Throwable throwable) {
            super.onFailure(call, throwable);
            onLoadDataInfoError(STATE_LOAD_AUDIO_ERROR);
        }
    };
    private RestCallback<RestMediaModel> mAlbumCallback = new RestCallback<RestMediaModel>() {
        @Override
        public void onResponse(Call<RestMediaModel> call, Response<RestMediaModel> response) {
            super.onResponse(call, response);
            try {
                if (response.body() != null) {
                    MediaModel item = response.body().getData();
                    if (item != null) {
                        ArrayList<MediaModel> list = new ArrayList<>();
                        list.addAll(item.getMediaList());
                        mMediaPlaying.setName(item.getName());
                        mMediaPlaying.setSinger(item.getSinger());
                        mMediaPlaying.setMediaList(list);
                        mCurrentMediaPosition = 0;
                        runUpdatePlayerMediaServiceListener(STATE_DATA_LOADER_SUCCESSFUL);
                        playMedia(mCurrentMediaPosition, false);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onFailure(Call<RestMediaModel> call, Throwable throwable) {
            super.onFailure(call, throwable);
            onLoadDataInfoError(STATE_LOAD_ALBUM_ERROR);
        }
    };
    private RestCallback<RestMediaModel> mPlaylistCallback = new RestCallback<RestMediaModel>() {
        @Override
        public void onResponse(Call<RestMediaModel> call, Response<RestMediaModel> response) {
            super.onResponse(call, response);
            try {
                if (response.body() != null) {
                    MediaModel item = response.body().getData();
                    if (item != null) {
                        ArrayList<MediaModel> list = new ArrayList<>();
                        list.addAll(item.getMediaList());
                        mMediaPlaying.setName(item.getName());
                        mMediaPlaying.setSinger(item.getSinger());
                        mMediaPlaying.setMediaList(list);
                        mCurrentMediaPosition = 0;
                        runUpdatePlayerMediaServiceListener(STATE_DATA_LOADER_SUCCESSFUL);
                        playMedia(mCurrentMediaPosition, false);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onFailure(Call<RestMediaModel> call, Throwable throwable) {
            super.onFailure(call, throwable);
            onLoadDataInfoError(STATE_LOAD_PLAYLIST_ERROR);
        }
    };
    private RestCallback<RestMediaModel> mVideoCallback = new RestCallback<RestMediaModel>() {
        @Override
        public void onResponse(Call<RestMediaModel> call, Response<RestMediaModel> response) {
            super.onResponse(call, response);
            try {
                if (response.body() != null) {
                    MediaModel item = response.body().getData();
                    if (item != null) {
                        ArrayList<MediaModel> list = new ArrayList<>();
                        list.addAll(item.getMediaList());
                        mMediaPlaying.setName(item.getName());
                        mMediaPlaying.setSinger(item.getSinger());
                        mMediaPlaying.setMediaList(list);
                        mCurrentMediaPosition = 0;
                        runUpdatePlayerMediaServiceListener(STATE_DATA_LOADER_SUCCESSFUL);
                        playMedia(mCurrentMediaPosition, false);
                        MessageEvent event = new MessageEvent(mMediaPlaying);
                        event.setState(STATE_DATA_LOADER_SUCCESSFUL);
                        event.setPosition(mCurrentMediaPosition);
                        EventBus.getDefault().post(event);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onFailure(Call<RestMediaModel> call, Throwable throwable) {
            super.onFailure(call, throwable);
            onLoadDataInfoError(STATE_LOAD_VIDEO_ERROR);
        }
    };

    @Override
    public void runUpdatePlayerMediaServiceListener(int state) {
        MessageEvent event;
        for (int i = 0; i < mServiceListeners.size(); i++) {
            OnPlayerMediaServiceListener item = mServiceListeners.get(i);
            if (item != null) {
                switch (state) {
                    case STATE_BUFFERING:
                        statePlaying = state;
                        item.onPlayerBufferingState();
                        break;

                    case STATE_PAUSED:
                        statePlaying = state;
                        updateNotification(getCurrentMedia());
                        item.onPlayerPausedState();
                        break;

                    case STATE_PLAYING:
                        statePlaying = state;
                        updateNotification(getCurrentMedia());
                        item.onPlayerPlayingState();
                        break;

                    case STATE_DATA_LOADER_SUCCESSFUL:
                        item.onDataLoaded(mMediaPlaying, mCurrentMediaPosition);
                        event = new MessageEvent(mMediaPlaying);
                        event.setState(STATE_DATA_LOADER_SUCCESSFUL);
                        event.setPosition(mCurrentMediaPosition);
                        EventBus.getDefault().post(event);
                        break;

                    case STATE_DATA_LOADER_FAILED:
                        item.onDataLoaderFailed();
                        event = new MessageEvent();
                        event.setState(STATE_DATA_LOADER_FAILED);
                        EventBus.getDefault().post(event);
                        break;

                    case STATE_FINISH_ALL:
                        item.onFinishAll();
                        break;

                    case UPDATE_PROGRESS:
                        item.onProgressBarChanged(getCurrenPosition(), getPositionPercentage(), getBufferedPercentage());
                        break;

                    case UPDATE_REPEAT_STATE:
                        item.onRepeatStateChanged(getRepeatState());
                        break;

                    case STATE_PREPARING:
                        item.onPlayerPreparingState(mCurrentMediaPosition);
                        break;

                    case STATE_READY:
                        item.onPlayerPreparedState(getDuration());
                        item.onDataLoaded(mMediaPlaying, mCurrentMediaPosition);
                        event = new MessageEvent(mMediaPlaying);
                        event.setState(STATE_DATA_LOADER_SUCCESSFUL);
                        event.setPosition(mCurrentMediaPosition);
                        EventBus.getDefault().post(event);
                        break;

                    case STATE_STOP_MEDIA:
                        statePlaying = STATE_IDLE;
                        item.onFinishAll();
                        break;
                }
            }
        }
    }

    @Override
    public void updateWidget(MediaModel item) {
        Log.d(TAG, "updateWidget --------");
        if (item != null) {

        }
    }

    @Override
    public void updateNotification(MediaModel item) {
        Log.d(TAG, "updateNotification --------");
        if (item != null) {
            ComponentName mComponent = new ComponentName(getBaseContext(), PlayMediaService.class);
            // Intent when click button
            // Button previous
            Intent previous = new Intent(this, PlayMediaService.class);
            previous.setAction(Constants.RECEIVER_MANAGER.ACTION_BROADCAST);
            previous.putExtra(Constants.RECEIVER_MANAGER.ACTION_NAME, Constants.RECEIVER_MANAGER.VALUE_CHANGE_PREVIOUS);
            previous.putExtra(Constants.RECEIVER_MANAGER.ACTION_START_WIDGET, Constants.RECEIVER_MANAGER.VALUE_WIDGET);
            previous.setComponent(mComponent);

            // Button play-pause
            Intent playPause = new Intent(this, PlayMediaService.class);
            playPause.setAction(Constants.RECEIVER_MANAGER.ACTION_BROADCAST);
            playPause.putExtra(Constants.RECEIVER_MANAGER.ACTION_NAME, Constants.RECEIVER_MANAGER.VALUE_CHANGE_PLAY);
            playPause.putExtra(Constants.RECEIVER_MANAGER.ACTION_START_WIDGET, Constants.RECEIVER_MANAGER.VALUE_WIDGET);
            playPause.setComponent(mComponent);

            // Button next
            Intent next = new Intent(this, PlayMediaService.class);
            next.setAction(Constants.RECEIVER_MANAGER.ACTION_BROADCAST);
            next.putExtra(Constants.RECEIVER_MANAGER.ACTION_NAME, Constants.RECEIVER_MANAGER.VALUE_CHANGE_NEXT);
            next.putExtra(Constants.RECEIVER_MANAGER.ACTION_START_WIDGET, Constants.RECEIVER_MANAGER.VALUE_WIDGET);
            next.setComponent(mComponent);

            // Button close
            Intent close = new Intent(this, PlayMediaService.class);
            close.setAction(Constants.RECEIVER_MANAGER.ACTION_BROADCAST);
            close.putExtra(Constants.RECEIVER_MANAGER.ACTION_NAME, Constants.RECEIVER_MANAGER.VALUE_CHANGE_STOP);
            close.putExtra(Constants.RECEIVER_MANAGER.ACTION_START_WIDGET, Constants.RECEIVER_MANAGER.VALUE_WIDGET);
            close.setComponent(mComponent);

            // Custom RemoteViews
            RemoteViews mBigContentView = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.layout_noti_player_big_content);
            mBigContentView.setTextViewText(R.id.title, item.getName());
            mBigContentView.setTextViewText(R.id.description, item.getSinger());
            mBigContentView.setOnClickPendingIntent(R.id.btn_previous, PendingIntent.getService(this, 1, previous, 0));
            mBigContentView.setImageViewResource(R.id.btn_play, isPlaying() ? R.drawable.noti_pause_black : R.drawable.noti_play_black);
            mBigContentView.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getService(this, 2, playPause, 0));
            mBigContentView.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getService(this, 3, next, 0));
            mBigContentView.setOnClickPendingIntent(R.id.btn_close, PendingIntent.getService(this, 4, close, 0));

            RemoteViews mContentView = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.layout_noti_player_content);
            mContentView.setTextViewText(R.id.title, item.getTitleCoverSinger());
            mContentView.setOnClickPendingIntent(R.id.btn_previous, PendingIntent.getService(this, 1, previous, 0));
            mContentView.setImageViewResource(R.id.btn_play, isPlaying() ? R.drawable.noti_pause_black : R.drawable.noti_play_black);
            mContentView.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getService(this, 2, playPause, 0));
            mContentView.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getService(this, 3, next, 0));
            mContentView.setOnClickPendingIntent(R.id.btn_close, PendingIntent.getService(this, 4, close, 0));

            try {
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.setFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
                intent.putExtra(Constants.KEY_DATA, mMediaPlaying);
                intent.putExtra(Constants.KEY_POSITION, mCurrentMediaPosition);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Swipe Notify
                Intent swipeIntent = new Intent(this, SwipeNotifyReceiver.class);
                swipeIntent.setFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
                PendingIntent deleteIntent = PendingIntent.getBroadcast(this, 0, swipeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext());
                mBuilder.setCustomContentView(mContentView)
                        .setCustomBigContentView(mBigContentView)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDeleteIntent(deleteIntent)
                        .setOngoing(false)
                        .setContentIntent(contentIntent);

                ImageBusiness mImageBusiness = new ImageBusiness(getBaseContext());
                Notification mNotification = mBuilder.build();
                NotificationTarget bigContentTarget = new NotificationTarget(getBaseContext(),
                        mBigContentView, R.id.image, mNotification, MEDIA_PLAYER_ID);
                mImageBusiness.setNotification(item.getImage(),
                        R.mipmap.ic_launcher, bigContentTarget);

                NotificationTarget contentTarget = new NotificationTarget(getBaseContext(),
                        mContentView, R.id.image, mNotification, MEDIA_PLAYER_ID);
                mImageBusiness.setNotification(item.getImage(),
                        R.mipmap.ic_launcher, contentTarget);

                startForeground(MEDIA_PLAYER_ID, mNotification);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
    }

    @Override
    public void processMediaReceiver(Intent intent) {
        int action = intent.getIntExtra(Constants.RECEIVER_MANAGER.ACTION_NAME, 0);
        Log.d(TAG, "processMediaReceiver action: " + action);

        switch (action) {
            case Constants.RECEIVER_MANAGER.VALUE_CHANGE_PLAY:
                if (isPlaying()) {
                    pauseMedia();
                } else {
                    playMedia();
                }
                updateNotification(getCurrentMedia());
                break;

            case Constants.RECEIVER_MANAGER.VALUE_CHANGE_NEXT:
                playNext();
                break;

            case Constants.RECEIVER_MANAGER.VALUE_CHANGE_STOP:
                playFinish();
                break;

            case Constants.RECEIVER_MANAGER.VALUE_CHANGE_PREVIOUS:
                playPrevious();
                break;

            case Constants.RECEIVER_MANAGER.VALUE_CHANGE_REPEAT_STATE:
                stateRepeat++;
                if (stateRepeat > REPEAT_NONE) {
                    stateRepeat = REPEAT_All;
                }
                runUpdatePlayerMediaServiceListener(UPDATE_REPEAT_STATE);
                break;
        }

    }

}
