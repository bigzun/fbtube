package com.bigzun.video.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bigzun.video.R;
import com.bigzun.video.fragment.AutoPlayFragment;

/**
 * Created by namnh40 on 12/16/2016.
 */

public class CategoryActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initToolbar();
        setContentFragment(AutoPlayFragment.newInstance(), false);
    }
}
