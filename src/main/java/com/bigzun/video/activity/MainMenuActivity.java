package com.bigzun.video.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bigzun.video.R;
import com.bigzun.video.adapter.DividerItemDecoration;
import com.bigzun.video.adapter.MainMenuAdapter;
import com.bigzun.video.control.CustomLinearLayoutManager;
import com.bigzun.video.listener.ClickItemListener;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.WSRestful;
import com.bigzun.video.restful.paser.RestGuideCategories;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainMenuActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<RestGuideCategories.Item> mMenuData;
    private DrawerLayout mDrawerMenu;
    private RecyclerView mRecyclerMenu;
    private MainMenuAdapter mMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRecyclerMenu = (RecyclerView) findViewById(R.id.recycler_menu);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerMenu, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerMenu.setDrawerListener(toggle);
        toggle.syncState();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);

        initMenuAdapter(R.drawable.line_divider);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerMenu.isDrawerOpen(GravityCompat.START)) {
            mDrawerMenu.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {

        }
        mDrawerMenu.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initMenuAdapter(int divider) {
        mRecyclerMenu.setHasFixedSize(true);
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        mRecyclerMenu.setLayoutManager(layoutManager);
        if (divider > 0)
            mRecyclerMenu.addItemDecoration(new DividerItemDecoration(this, divider));

        if (mMenuData == null)
            mMenuData = new ArrayList<>();
        mMenuAdapter = new MainMenuAdapter(this, mMenuData);
        mRecyclerMenu.setAdapter(mMenuAdapter);
        mMenuAdapter.notifyDataSetChanged();
        mMenuAdapter.setClickItemListener(new ClickItemListener() {
            @Override
            public void onClickItem(View view, int position) {

            }

            @Override
            public void onLongClickItem(View view, int position) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mMenuData == null || mMenuData.size() <= 0)
//            loadGuideCategories();
    }

    private void loadGuideCategories() {
        WSRestful rest = new WSRestful(this);
        rest.loadGuideCategories(new RestCallback<RestGuideCategories>() {
            @Override
            public void onFailure(Call<RestGuideCategories> call, Throwable throwable) {
                super.onFailure(call, throwable);
            }

            @Override
            public void onResponse(Call<RestGuideCategories> call, Response<RestGuideCategories> response) {
                super.onResponse(call, response);
                if (response != null && response.body() != null) {
                    if (mMenuData == null)
                        mMenuData = new ArrayList<>();
                    else
                        mMenuData.clear();
                    mMenuData.addAll(response.body().getData());
                    mMenuAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
