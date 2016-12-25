package com.bigzun.video.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bigzun.video.R;
import com.bigzun.video.business.ImageBusiness;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.service.PlayMediaService;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.SharedPref;
import com.bigzun.video.util.Utilities;
import com.google.android.exoplayer2.ExoPlayer;

/**
 * Created by Admin on 10/13/2016.
 */

public class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected SharedPref mPref;
    protected TextView mTvTitle;
    protected Toolbar mToolbar;
    protected ImageBusiness mImageBusiness;
    protected PlayMediaService mMediaService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate -------- .");
        mPref = new SharedPref(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart -------- .");
        if (!(this instanceof SplashActivity))
            startConnectService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume -------- .");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause -------- .");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop -------- .");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy -------- .");
        try {
            if (!(this instanceof SplashActivity))
                unBindServiceMedia();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed -------- .");
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult requestCode: " + requestCode + ", resultCode: " + resultCode + " -------- .");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged -------- .");
        super.onConfigurationChanged(newConfig);
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) findViewById(R.id.title);
    }

    public void setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public void gotoMain() {
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void gotoChooseRegion() {
        Intent intent = new Intent(BaseActivity.this, ChooseRegionActivity.class);
        startActivity(intent);
        finish();
    }

    public void hideBanner() {

    }

    public void showBanner() {

    }

    protected void setContentFragment(Fragment fragment, boolean slideToLeft) {
        Utilities.hideKeyboard(null, BaseActivity.this);
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (slideToLeft) {
                transaction.setCustomAnimations(R.anim.slide_right_to_left, R.anim.slide_right_to_left_out, R.anim.slide_left_to_right, R.anim.slide_left_to_right_out);
            } else {
                transaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_left_to_right_out, R.anim.slide_right_to_left, R.anim.slide_right_to_left_out);
            }
            transaction.replace(R.id.content_layout, fragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public int getCurrentSongIndex() {
        if (mMediaService != null)
            return mMediaService.getCurrentMediaPosition();
        return 0;
    }

    public MediaPlaying getMediaPlaying() {
        if (mMediaService != null)
            return mMediaService.getMediaPlaying();
        return null;
    }

    public MediaModel getCurrentMedia() {
        if (mMediaService != null)
            return mMediaService.getCurrentMedia();
        return null;
    }

    public ExoPlayer getMediaPlayer() {
        if (mMediaService != null)
            return mMediaService.getMediaPlayer();
        return null;
    }

    protected void onDisonnectServiceDone() {
        Log.d(TAG, "onDisonnectServiceDone -------- .");
    }

    /**
     * service connect xong
     */
    protected void onConnectServiceDone() {
        Log.d(TAG, "onConnectServiceDone -------- .");
    }

    /**
     * bind den service
     */
    protected void startConnectService() {
        Log.d(TAG, "startConnectService -------- .");
        Intent intent = new Intent(getBaseContext(), PlayMediaService.class);
        getApplicationContext().bindService(intent, mMediaConnection, Context.BIND_AUTO_CREATE);
    }

    protected void unBindServiceMedia() {
        Log.d(TAG, "unBindServiceMedia -------- .");
        if (mMediaService != null) {
            getApplication().unbindService(mMediaConnection);
        }
    }

    protected ServiceConnection mMediaConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected -------- ." + name);
            onDisonnectServiceDone();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected -------- ." + name);
            if (service instanceof PlayMediaService.LocalBinder) {
                mMediaService = ((PlayMediaService.LocalBinder) service).getService();
                onConnectServiceDone();
            }
        }
    };

    public void setMediaPlayingWithAudio(MediaModel item, int position) {
        Log.d(TAG, "setMediaPlayingWithAudio: " + item);
        //TODO play audio
        stopVideo();
        if (mMediaService != null) {
            MediaPlaying mPlaying = new MediaPlaying();
            mPlaying.setMedia(item, MediaPlaying.TYPE_AUDIO);
            mMediaService.setMediaPlaying(mPlaying, position);
        }
    }

    public void setMediaPlayingWithAlbum(MediaModel item, int position) {
        Log.d(TAG, "setMediaPlayingWithAlbum: " + item);
        //TODO play album
        stopVideo();
        if (mMediaService != null) {
            MediaPlaying mPlaying = new MediaPlaying();
            mPlaying.setMedia(item, MediaPlaying.TYPE_ALBUM);
            mMediaService.setMediaPlaying(mPlaying, position);
        }
    }

    public void setMediaPlayingWithPlaylist(MediaModel item, int position) {
        Log.d(TAG, "setMediaPlayingWithPlaylist: " + item);
        //TODO play playlist
        stopVideo();
        if (mMediaService != null) {
            MediaPlaying mPlaying = new MediaPlaying();
            mPlaying.setMedia(item, MediaPlaying.TYPE_PLAYLIST);
            mMediaService.setMediaPlaying(mPlaying, position);
        }
    }

    public void setMediaPlayingWithAudioList(MediaModel item, int position) {
        Log.d(TAG, "setMediaPlayingWithAudioList: " + item);
        //TODO play audio list
        stopVideo();
        if (mMediaService != null) {
            MediaPlaying mPlaying = new MediaPlaying();
            mPlaying.setMedia(item, MediaPlaying.TYPE_FULL_DATA);
            mMediaService.setMediaPlaying(mPlaying, position);
        }
    }

    public void setMediaPlayingWithVideo(MediaModel item, int position) {
//        stopAudio();
        if (mMediaService != null) {
            MediaPlaying mPlaying = new MediaPlaying();
            mPlaying.setMedia(item, MediaPlaying.TYPE_VIDEO);
            mMediaService.setMediaPlaying(mPlaying, position);
        }
    }

    public void stopVideo() {
    }

    public void stopAudio() {
        if (mMediaService != null) {
            mMediaService.stopMedia();
        }
    }

    public void clickMediaItem(MediaModel item) {
        if (item != null) {
            switch (item.getType()) {
                case song:
                    setMediaPlayingWithAudio(item, 0);
                    break;

                case album:
                    setMediaPlayingWithAlbum(item, 0);
                    break;

                case playlist:
                    setMediaPlayingWithPlaylist(item, 0);

                case video:
                    setMediaPlayingWithVideo(item, 0);
                    break;
            }
        }
    }
}
