package com.bigzun.video.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bigzun.video.R;
import com.bigzun.video.adapter.ViewPagerPlayerAdapter;
import com.bigzun.video.business.ImageBusiness;
import com.bigzun.video.control.TriToggleButton;
import com.bigzun.video.helper.DateTimeHelper;
import com.bigzun.video.listener.OnPlayerMediaServiceListener;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.ui.CircleImageView;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;
import com.google.android.exoplayer2.ExoPlayer;

public class PlayerActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        OnClickListener, OnPlayerMediaServiceListener {

    private ViewPager mViewPager;
    private ImageView btnPlayMusic;
    private ImageView btnNextMusic;
    private ImageView btnPrevMusic;
    private ExoPlayer mediaPlayer;
    private TriToggleButton btnShuffle;
    private SeekBar seekbar;
    private TextView timmer;
    private TextView totalTimer;
    private ProgressBar mProgressLoading;
    private CircleImageView avatarCasi;
    private TextView txtTenBaiHat;
    private TextView txtTenCasi;
    private ImageView imvBgPlayer;
    private ImageView btnShowMiniPlayer;
    private TextView mBtnListenTogether;
    private ViewPagerPlayerAdapter mAdapter;

    public MediaModel mMedia;
    public MediaPlaying mMediaPlaying;
    private ImageView btnDownloadSong;
    private RelativeLayout layoutToolbar;
    private long currentTotalDuration = 0;
    private boolean isStartTrackingTouch = false;
    Point startPoint = new Point();
    Point endPoint = new Point();

    OnTouchListener onTouchToolbarListener = new OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPoint.x = x_cord;
                        startPoint.y = y_cord;
                        endPoint.x = x_cord;
                        endPoint.y = y_cord;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        endPoint.x = x_cord;
                        endPoint.y = y_cord;

                        break;
                    case MotionEvent.ACTION_UP:
                        if ((endPoint.y - startPoint.y) > 50) {
                            onBackPressed();
                        }
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
            return true;
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageBusiness = new ImageBusiness(getBaseContext());
        mAdapter = new ViewPagerPlayerAdapter(this, getSupportFragmentManager());
        setContentView(R.layout.activity_player);
        btnShowMiniPlayer = (ImageView) findViewById(R.id.btn_show_mini_play);
        imvBgPlayer = (ImageView) findViewById(R.id.imv_bgplayer);
        btnDownloadSong = (ImageView) findViewById(R.id.btnDownload);
        layoutToolbar = (RelativeLayout) findViewById(R.id.layoutToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        avatarCasi = (CircleImageView) findViewById(R.id.imv_casi);
        txtTenBaiHat = (TextView) findViewById(R.id.txt_ten_bai_hat);
        txtTenCasi = (TextView) findViewById(R.id.txt_ten_ca_si);
        btnPlayMusic = (ImageView) findViewById(R.id.btnplaymusic);
        btnNextMusic = (ImageView) findViewById(R.id.btnnextmusic);
        btnPrevMusic = (ImageView) findViewById(R.id.btnprevmusic);
        btnShuffle = (TriToggleButton) findViewById(R.id.btnshuffle);
        seekbar = (SeekBar) findViewById(R.id.seekbar_timer);
        timmer = (TextView) findViewById(R.id.timer_Seek);
        totalTimer = (TextView) findViewById(R.id.total_timer);
        mBtnListenTogether = (TextView) findViewById(R.id.button_listen_together);
        mProgressLoading = (ProgressBar) findViewById(R.id.loading);

        btnDownloadSong.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        btnPrevMusic.setOnClickListener(this);
        btnNextMusic.setOnClickListener(this);
        btnPlayMusic.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnShowMiniPlayer.setOnClickListener(this);
        mBtnListenTogether.setOnClickListener(this);
        layoutToolbar.setOnTouchListener(onTouchToolbarListener);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isStartTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isStartTrackingTouch = false;
                if (mediaPlayer != null) {
                    currentTotalDuration = mMediaService.getDuration();
                    int currentPosition = DateTimeHelper.progressToTimer(seekBar.getProgress(), currentTotalDuration);
                    if (currentTotalDuration >= 0 && currentPosition >= 0) {
                        mediaPlayer.seekTo(currentPosition);
                    }
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

        try {
            mMediaPlaying = (MediaPlaying) getIntent().getSerializableExtra(Constants.KEY_DATA);
            if (mMediaPlaying != null && !mMediaPlaying.isEmpty()) {
                int position = getIntent().getIntExtra(Constants.KEY_POSITION, 0);
                mMedia = mMediaPlaying.getMediaList().get(position);
                updateCurrentSong(mMedia);
            }

        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDisonnectServiceDone() {
        super.onDisonnectServiceDone();
        if (mMediaService != null) {
            mMediaService.removeOnPlayerMediaServiceListener(this);
        }
    }

    @Override
    protected void onConnectServiceDone() {
        super.onConnectServiceDone();
        if (mMediaService != null) {
            mMediaService.addOnPlayerMediaServiceListener(this);
            mediaPlayer = mMediaService.getMediaPlayer();
            updatePlayerInfo();
            mMedia = mMediaService.getCurrentMedia();
            if (mMedia != null) {
                updateCurrentSong(mMedia);
            }
        }
    }

    private void updatePlayerInfo() {
        Log.d(TAG, "updatePlayerInfo");
        updatePlayerStateRepeat(mMediaService.getRepeatState());
        setPlay(mMediaService.isPlaying());
    }

    public void changeStateRepeat(int state) {
        if (mMediaService != null)
            mMediaService.setRepeatState(state);
    }

    public void playNextMusic(int index) {
        if (mMediaService != null) {
            mMediaService.playMedia(index, false);
        }
    }

    public void playNextMusic() {
        if (mMediaService != null)
            mMediaService.playNext();
    }

    public void playPrevMusic() {
        if (mMediaService != null)
            mMediaService.playPrevious();
    }

    public void toogleMusic() {
        if (mMediaService != null) {
            mMediaService.togglePlay(true);
        }
    }

    public void updatePlayerStateRepeat(int state) {
        btnShuffle.setState(state, false);
    }

    public void setPlay(boolean flag) {
        if (btnPlayMusic != null)
            if (flag) {
                btnPlayMusic.setImageResource(R.drawable.btn_pause);
            } else {
                btnPlayMusic.setImageResource(R.drawable.btn_play);
            }
    }

    private void updateCurrentSong(MediaModel item) {
        if (mMediaPlaying == null)
            return;
        int type = mMediaPlaying.getType();
        switch (type) {
            case MediaPlaying.TYPE_ALBUM:
            case MediaPlaying.TYPE_PLAYLIST:
                setAvatarAlbum(mMediaPlaying);
                resetInfoPlayer(mMediaPlaying.getName(), mMediaPlaying.getSinger());
                break;

            default:
                if (item == null)
                    return;
                setAvatarSinger(item);
                resetInfoPlayer(item.getName(), item.getSinger());
                break;
        }
        if (item == null)
            return;
        mMedia = item;
        Intent intent = new Intent(Constants.RECEIVER_MANAGER.MESSAGE_UPDATE_DATA_PLAYER);
        sendBroadcast(intent);
    }

    private void setAvatarSinger(MediaModel song) {
        if (avatarCasi != null) {
            mImageBusiness.setAvatar(avatarCasi, song.getImage());
        }
    }

    private void setAvatarAlbum(MediaPlaying album) {
        if (avatarCasi != null) {
            mImageBusiness.setAvatar(avatarCasi, album.getImage());
        }
    }

    public void onFinishAll() {
        PlayerActivity.this.finish();
    }

    private void showProgressBar(boolean isShow) {
        if (mProgressLoading != null)
            if (isShow) {
                mProgressLoading.setVisibility(View.VISIBLE);
            } else {
                mProgressLoading.setVisibility(View.GONE);
            }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_top_to_bot, R.anim.slide_out_bottom);
    }

    @Override
    public void onClick(View view) {
        if (view == null)
            return;

        switch (view.getId()) {
            case R.id.button_listen_together:
                break;
            case R.id.btnplaymusic:
                toogleMusic();
                break;
            case R.id.btnnextmusic:
                playNextMusic();
                break;

            case R.id.btnprevmusic:
                playPrevMusic();
                break;
            case R.id.btnshuffle:
                if (view instanceof TriToggleButton) {
                    TriToggleButton b = (TriToggleButton) view;
                    if (b != null) {
                        int state = b.getState();
                        changeStateRepeat(state);
                    }
                }
                break;
            case R.id.btn_show_mini_play:
                onBackPressed();
                break;
            case R.id.btnDownload:
                break;
        }
    }

    private void resetInfoPlayer(String name, String singer) {
        try {
            txtTenBaiHat.setText(name);
            txtTenCasi.setText(singer);
            timmer.setText("00:00");
            totalTimer.setText("00:00");
            seekbar.setProgress(0);
            seekbar.setSecondaryProgress(0);
            currentTotalDuration = 0;
        } catch (Exception e) {
        }
    }

    @Override
    public void onRepeatStateChanged(int state) {
        Log.d(TAG, "onRepeatStateChanged --------------------");
        updatePlayerStateRepeat(state);
    }

    @Override
    public void onDataLoaded(final MediaPlaying currentList, final int currentIndex) {
        Log.d(TAG, "onDataLoaded --------------------");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                try {
                    mMediaPlaying = currentList;
                    if (mMediaPlaying != null && mMediaPlaying.getMediaList() != null && mMediaPlaying.getMediaList().size() > 0)
                        mMedia = mMediaPlaying.getMediaList().get(currentIndex);
                    else
                        mMedia = null;
                    updateCurrentSong(mMedia);
                    setPlay(false);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, e);
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }
        });
    }

    @Override
    public void onDataLoaderFailed() {
        Log.d(TAG, "onDataLoaderFailed --------------------");
    }

    @Override
    public void onPlayerPreparingState(final int position) {
        Log.d(TAG, "onPlayerPreparingState --------------------");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    showProgressBar(true);
                    if (mMediaPlaying != null && !mMediaPlaying.isEmpty())
                        mMedia = mMediaPlaying.getMediaList().get(position);
                    else
                        mMedia = null;
                    updateCurrentSong(mMedia);
                    setPlay(false);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, e);
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }
        });
    }

    @Override
    public void onPlayerPreparedState(final long duration) {
        Log.d(TAG, "onDataChanged --------------------");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                currentTotalDuration = duration;
                totalTimer.setText(DateTimeHelper.milliSecondsToTimer(currentTotalDuration));
            }
        });
    }

    @Override
    public void onPlayerBufferingState() {
        Log.d(TAG, "onPlayerBufferingState --------------------");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(true);
            }
        });
    }

    @Override
    public void onPlayerPlayingState() {
        Log.d(TAG, "onPlayerPlayingState --------------------");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPlay(true);
            }
        });
    }

    @Override
    public void onPlayerPausedState() {
        Log.d(TAG, "onPlayerPausedState --------------------");
        setPlay(false);
    }

    @Override
    public void onProgressBarChanged(final long currentPosition, final int currentPercentage, final int bufferedPercentage) {
        Log.d(TAG, "onProgressBarChanged --------------------");
        if (!isStartTrackingTouch) {
            timmer.setText("" + DateTimeHelper.milliSecondsToTimer(currentPosition));
            seekbar.setProgress(currentPercentage);
            seekbar.setSecondaryProgress(bufferedPercentage);
        }
    }

}