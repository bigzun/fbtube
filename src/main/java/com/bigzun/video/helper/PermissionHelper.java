package com.bigzun.video.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

@SuppressLint("InlinedApi")
public class PermissionHelper {

    private static RequestPermissionsResult mCallBack;

    public static boolean allowedPermission(Context context, String permission) {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            return permissionCheck == PackageManager.PERMISSION_GRANTED ? true : false;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean declinedPermission(Context context, String permission) {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            return permissionCheck == PackageManager.PERMISSION_GRANTED ? false : true;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        // app-defined int constant. The callback method gets the
        // result of the request.
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
        // app-defined int constant. The callback method gets the
        // result of the request.
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestOverlayDrawPermission(Activity act, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + act.getPackageName()));
        act.startActivityForResult(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isSystemAlertGranted(Context context) {
        try {
            return Settings.canDrawOverlays(context);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ArrayList<String> declinedPermissions(Context context, String[] permissions) {
        ArrayList<String> permissionsNeededs = new ArrayList<String>();
        for (String permission : permissions) {
            if (declinedPermission(context, permission)) {
                permissionsNeededs.add(permission);
            }
        }
        return permissionsNeededs;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mCallBack != null) {
            mCallBack.onPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static void setCallBack(RequestPermissionsResult callBack) {
        mCallBack = callBack;
    }

    public interface RequestPermissionsResult {

        void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }
}