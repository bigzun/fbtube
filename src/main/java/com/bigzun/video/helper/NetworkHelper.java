package com.bigzun.video.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Check device's network connectivity and speed
 *
 * @author emil http://stackoverflow.com/users/220710/emil
 */
public class NetworkHelper {

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        if (context == null)
            return false;
        NetworkInfo info = NetworkHelper.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context) {
        if (context == null)
            return false;
        NetworkInfo info = NetworkHelper.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        if (context == null)
            return false;
        NetworkInfo info = NetworkHelper.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        if (context == null)
            return false;
        NetworkInfo info = NetworkHelper.getNetworkInfo(context);
        return (info != null && info.isConnected() && NetworkHelper.isConnectionFast(context, info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(Context context, int type, int subType) {
        if (context == null)
            return false;
        if (type == ConnectivityManager.TYPE_WIFI) {
            /*
             * WifiManager wifiManager = (WifiManager)
			 * context.getSystemService(Context.WIFI_SERVICE); WifiInfo wifiInfo
			 * = wifiManager.getConnectionInfo(); if (wifiInfo != null) {
			 * //Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using
			 * WifiInfo.LINK_SPEED_UNITS }
			 */
            networkInfo = "Wifi";
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    networkInfo = "NETWORK_TYPE_1xRTT";
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    networkInfo = "NETWORK_TYPE_CDMA";
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    networkInfo = "NETWORK_TYPE_EDGE";
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    networkInfo = "NETWORK_TYPE_EVDO_0";
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    networkInfo = "NETWORK_TYPE_EVDO_A";
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    networkInfo = "NETWORK_TYPE_GPRS - 2G";
                    return false; // 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    networkInfo = "NETWORK_TYPE - 3.5G";
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    networkInfo = "NETWORK_TYPE - 3.5G";
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    networkInfo = "NETWORK_TYPE - 3.5G";
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    networkInfo = "NETWORK_TYPE_UMTS - 3G";
                    return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion to
			 * appropriate level to use these
			 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    networkInfo = "NETWORK_TYPE_EHRPD";
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    networkInfo = "NETWORK_TYPE_EVDO_B";
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    networkInfo = "NETWORK_TYPE_HSPAP";
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    networkInfo = "NETWORK_TYPE_IDEN";
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    networkInfo = "NETWORK_TYPE_LTE";
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    networkInfo = "Unknow";
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private static String networkInfo = "";

    public static String getNetworkStringInfo() {
        return networkInfo;
    }

}
