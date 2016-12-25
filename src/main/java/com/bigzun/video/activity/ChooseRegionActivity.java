package com.bigzun.video.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bigzun.video.R;
import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.restful.RestCallback;
import com.bigzun.video.restful.WSRestful;
import com.bigzun.video.restful.paser.RestLanguagesRegions;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Admin on 10/12/2016.
 */

public class ChooseRegionActivity extends BaseActivity {

    protected static final String TAG = "ChooseRegionActivity";
    private AppCompatButton mBtnSubmit;
    private Spinner mSpnRegions, mSpnLanguages;
    private SpinnerRegionAdapter mRegionsAdapter, mLanguagesAdapter;
    private List<RestLanguagesRegions.Item> mLanguagesData, mRegionsData;
    private RestLanguagesRegions.Item regionItem, languageItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_region);
        mBtnSubmit = (AppCompatButton) findViewById(R.id.button_submit);
        mSpnRegions = (Spinner) findViewById(R.id.spinner_regions);
        mSpnLanguages = (Spinner) findViewById(R.id.spinner_languages);
        setListener();
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLanguagesData == null || mLanguagesData.size() <= 0)
            loadLanguage();

        if (mRegionsData == null || mRegionsData.size() <= 0)
            loadRegion();
    }

    private void initAdapter() {
        if (mRegionsData == null)
            mRegionsData = new ArrayList<>();
        mRegionsAdapter = new SpinnerRegionAdapter(this, mRegionsData);
        mSpnRegions.setAdapter(mRegionsAdapter);
        mRegionsAdapter.notifyDataSetChanged();

        if (mLanguagesData == null)
            mLanguagesData = new ArrayList<>();
        mLanguagesAdapter = new SpinnerRegionAdapter(this, mLanguagesData);
        mSpnLanguages.setAdapter(mLanguagesAdapter);
        mLanguagesAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mSpnRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    regionItem = mRegionsAdapter.getItem(i);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, e);
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpnLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    languageItem = mLanguagesAdapter.getItem(i);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, e);
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (regionItem != null) {
                        mPref.putString(Constants.PREF_REGION_CODE, regionItem.getRegionCode());
                    }
                    if (languageItem != null) {
                        mPref.putString(Constants.PREF_LANGUAGE_CODE, languageItem.getLanguageCode());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e);
                }

                mPref.putBoolean(Constants.PREF_CHOOSE_REGION, true);
                gotoMain();
            }
        });
    }

    private void loadLanguage() {
        try {
            String jsonStr = mPref.getString(Constants.PREF_LANGUAGES_LIST, "");
            if (!TextUtils.isEmpty(jsonStr)) {
                RestLanguagesRegions item = new Gson().fromJson(jsonStr, RestLanguagesRegions.class);
                if (item != null && item.getData() != null) {
                    mLanguagesData.clear();
                    mLanguagesData.addAll(item.getData());
                    mLanguagesAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }

        selectionSpinnerLanguages();

        WSRestful rest = new WSRestful(this);
        rest.loadLanguages(new RestCallback<RestLanguagesRegions>() {
            @Override
            public void onResponse(Call<RestLanguagesRegions> call, Response<RestLanguagesRegions> response) {
                super.onResponse(call, response);
                if (response != null && response.body() != null) {
                    if (mLanguagesData == null)
                        mLanguagesData = new ArrayList<>();
                    else
                        mLanguagesData.clear();
                    mLanguagesData.addAll(response.body().getData());
                    mLanguagesAdapter.notifyDataSetChanged();
                    mPref.putObject(Constants.PREF_LANGUAGES_LIST, response.body());
                }
            }

            @Override
            public void onFailure(Call<RestLanguagesRegions> call, Throwable throwable) {
                super.onFailure(call, throwable);
            }
        });
    }

    private void loadRegion() {
        try {
            String jsonStr = mPref.getString(Constants.PREF_REGIONS_LIST, "");
            if (!TextUtils.isEmpty(jsonStr)) {
                RestLanguagesRegions item = new Gson().fromJson(jsonStr, RestLanguagesRegions.class);
                if (item != null && item.getData() != null) {
                    mRegionsData.clear();
                    mRegionsData.addAll(item.getData());
                    mRegionsAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }

        selectionSpinnerRegions();

        WSRestful rest = new WSRestful(this);
        rest.loadRegions(new RestCallback<RestLanguagesRegions>() {
            @Override
            public void onResponse(Call<RestLanguagesRegions> call, Response<RestLanguagesRegions> response) {
                super.onResponse(call, response);
                if (response != null && response.body() != null) {
                    if (mRegionsData == null)
                        mRegionsData = new ArrayList<>();
                    else
                        mRegionsData.clear();
                    mRegionsData.addAll(response.body().getData());
                    mRegionsAdapter.notifyDataSetChanged();
                    mPref.putObject(Constants.PREF_REGIONS_LIST, response.body());
                }
            }

            @Override
            public void onFailure(Call<RestLanguagesRegions> call, Throwable throwable) {
                super.onFailure(call, throwable);
            }
        });
    }

    private void selectionSpinnerLanguages() {
        try {
            String code =  ApplicationController.getInstance().getLanguageCode();
            if (!TextUtils.isEmpty(code)) {
                for (int i = 0; i < mLanguagesData.size(); i++) {
                    if (mLanguagesData.get(i).getLanguageCode().equals(code)) {
                        mSpnLanguages.setSelection(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    private void selectionSpinnerRegions() {
        try {
            String code = ApplicationController.getInstance().getRegionCode();
            if (!TextUtils.isEmpty(code)) {
                for (int i = 0; i < mRegionsData.size(); i++) {
                    if (mRegionsData.get(i).getRegionCode().equals(code)) {
                        mSpnRegions.setSelection(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public static class SpinnerRegionAdapter extends BaseAdapter implements SpinnerAdapter {

        private List<RestLanguagesRegions.Item> mData;
        private LayoutInflater mInflater;

        public SpinnerRegionAdapter(Context context, List<RestLanguagesRegions.Item> data) {
            super();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mData = data;
        }

        @Override
        public int getCount() {
            if (mData != null)
                return mData.size();
            return 0;
        }

        @Override
        public RestLanguagesRegions.Item getItem(int position) {
            try {
                if (mData != null)
                    return mData.get(position);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup viewGroup) {
            return getCustomView(position, view, viewGroup);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            return getCustomView(position, view, viewGroup);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            View row = mInflater.inflate(R.layout.adapter_spinner_region, parent, false);
            TextView mTvTitle = (TextView) row.findViewById(R.id.title);
            RestLanguagesRegions.Item mItem = getItem(position);
            if (mItem != null)
                mTvTitle.setText(mItem.getName());
            return row;
        }
    }
}
