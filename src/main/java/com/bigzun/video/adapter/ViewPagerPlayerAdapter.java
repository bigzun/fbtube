package com.bigzun.video.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.bigzun.video.fragment.player.OneFragment;
import com.bigzun.video.fragment.player.TwoFragment;

public class ViewPagerPlayerAdapter extends SmartFragmentStatePagerAdapter {
    private Fragment mFragment;

    public ViewPagerPlayerAdapter(Context context, FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragment = OneFragment.newInstance();
                break;
            case 1:
                mFragment = TwoFragment.newInstance();
                break;
            default:
                mFragment = TwoFragment.newInstance();
                break;
        }
        return mFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

}
