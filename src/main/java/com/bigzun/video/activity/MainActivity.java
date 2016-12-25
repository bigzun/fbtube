package com.bigzun.video.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.R;
import com.bigzun.video.fragment.BaseFragment;
import com.bigzun.video.fragment.MostPopularFragment;
import com.bigzun.video.fragment.VideoInfoFragment;
import com.bigzun.video.fragment.YoutubeFragment;
import com.bigzun.video.model.MediaModel;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Toast;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 10/13/2016.
 */

public class
MainActivity extends BaseActivity implements View.OnClickListener {

    protected BaseFragment mContentFragment;
    protected YoutubeFragment mTopFragment;
    protected VideoInfoFragment mBottomFragment;

    @BindView(R.id.button_setting)
    ImageView mBtnSetting;
    @BindView(R.id.button_search)
    ImageView mBtnSearch;
    @BindView(R.id.main_layout)
    View mMainView;
    @BindView(R.id.draggable_panel)
    DraggablePanel mDraggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.setDebug(BuildConfig.DEBUG);
        initToolbar();
        mBtnSetting.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        initDraggablePanel();
        mContentFragment = MostPopularFragment.newInstance();
        setContentFragment(mContentFragment, true);
    }

    private void initDraggablePanel() {
        mTopFragment = YoutubeFragment.newInstance();
        mBottomFragment = VideoInfoFragment.newInstance();
        mDraggablePanel.setFragmentManager(getSupportFragmentManager());
        mDraggablePanel.setTopFragment(mTopFragment);
        mDraggablePanel.setBottomFragment(mBottomFragment);
        hookDraggablePanelListeners();
        mDraggablePanel.initializeView();
        setSmallScreen();
    }

    private void hookDraggablePanelListeners() {
        mDraggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                Log.e(TAG, "onMaximized");
                setEnableWhenShowDraggable(false);
            }

            @Override
            public void onMinimized() {
                Log.e(TAG, "onMinimized");
                setEnableWhenShowDraggable(true);
                mDraggablePanel.disableOnTouch(false);
                if (mTopFragment != null) {
                    //mTopFragment.hideControlView();
                }
            }

            @Override
            public void onClosedToLeft() {
                Log.e(TAG, "onClosedToLeft");
                offVideo();
            }

            @Override
            public void onClosedToRight() {
                Log.e(TAG, "onClosedToRight");
                offVideo();
            }
        });
    }

    private void setEnableWhenShowDraggable(boolean enable) {
        mMainView.setEnabled(enable);
    }

    @Override
    public void onClick(View view) {
        if (view == null)
            return;

        switch (view.getId()) {
            case R.id.button_setting:
                Toast.makeText(MainActivity.this, "Button setting was clicked.");
                break;
            case R.id.button_search:
                Toast.makeText(MainActivity.this, "Button search was clicked.");
                break;
        }
    }

    public void smallVideo() {
        if (mTopFragment.isFullScreen()) {
            mTopFragment.toggleScreen();
        } else if (mDraggablePanel != null)
            mDraggablePanel.minimize();
    }

    public void toggleScreenVideo(boolean frag) {
        if (frag) {
            setSmallScreen();
            showBanner();
        } else {
            setFullScreen();
            hideBanner();
        }
    }

    public void setSmallScreen() {
        //TODO smallscreen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (mDraggablePanel != null) {
            mDraggablePanel.disableOnTouch(false);
            mDraggablePanel.setLayoutParams(metrics.widthPixels, metrics.widthPixels * 9 / 16);
        }
    }

    public void setFullScreen() {
        //TODO fullscreen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (mDraggablePanel != null) {
            mDraggablePanel.disableOnTouch(true);
            mDraggablePanel.setLayoutParams(metrics.widthPixels, metrics.heightPixels);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDraggablePanel.getVisibility() == View.VISIBLE) {
            if (mDraggablePanel.isMaximized()) {
                if (mTopFragment != null && mTopFragment.isFullScreen())
                    mTopFragment.toggleScreen();
                else
                    mDraggablePanel.minimize();
            } else if (mDraggablePanel.isMinimized())
                mDraggablePanel.closeToRight();
            else
                exitApp();
        } else
            exitApp();
    }

    private boolean isTouchTwoTimes = false;
    private static final int COUNT_DONW = 2000;

    protected void exitApp() {
        if (!isTouchTwoTimes) {
            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.exit_ask));
            isTouchTwoTimes = true;
            new CountDownTimer(COUNT_DONW, COUNT_DONW) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    isTouchTwoTimes = false;
                }
            }.start();
            return;
        }
        finish();
        System.exit(0);
    }

    public void autoPlayNextVideo() {
        try {
            if (mBottomFragment != null) {
                MediaModel item = mBottomFragment.getNextVideo();
                if (item != null) {
                    setMediaPlayingWithVideo(item, 0);
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        stopVideo();
    }

    @Override
    public void setMediaPlayingWithVideo(MediaModel item, int position) {
        //super.setMediaPlayingWithVideo(item, position);
        if (mDraggablePanel.getVisibility() != View.VISIBLE)
            mDraggablePanel.setVisibility(View.VISIBLE);
        if (mDraggablePanel.isClosed()
                || mDraggablePanel.isMinimized()) {
            mDraggablePanel.maximize();
        }
        if (mTopFragment != null) {
            mTopFragment.setVideo(item);
        }
        if (mBottomFragment != null) {
            mBottomFragment.setVideo(item);
        }
    }

    private void offVideo() {
        if (mTopFragment != null) {
            mTopFragment.setVideo(null);
        }
        if (mBottomFragment != null) {
            mBottomFragment.setVideo(null);
        }
    }

    @Override
    public void stopVideo() {
        if (!mDraggablePanel.isClosed())
            mDraggablePanel.closeToRight();
        else
            offVideo();
    }

    public void setTopControlView(View view) {
//        if (mDraggablePanel != null)
//            mDraggablePanel.setTopControlView(view);
    }

    public void setBottomControlView(View view) {
//        if (mDraggablePanel != null)
//            mDraggablePanel.setBottomControlView(view);
    }

}