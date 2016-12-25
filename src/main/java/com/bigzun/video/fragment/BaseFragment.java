/**
 *
 */
package com.bigzun.video.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bigzun.video.util.Log;

/**
 * @author namnh40
 */
public abstract class BaseFragment extends Fragment {

    // tuyet doi ko dc thay doi bien TAG nay
    protected final String TAG = getClass().getSimpleName();
    //TODO fragment da dc hien thi hay chua?
    protected boolean isVisibled = false;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode= " + requestCode + "; resultCode= " + resultCode + "; data= " + data + "-------");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume isVisibled: " + isVisibled + " -------");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d(TAG, "onPause-------");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy-------");
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisibled = isVisibleToUser;
    }

}
