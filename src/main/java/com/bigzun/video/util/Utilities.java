package com.bigzun.video.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bigzun.video.R;
import com.google.gson.Gson;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
@SuppressLint("DefaultLocale")
public class Utilities {
    private static final String KEY_IMEI = "IMEI";
    private static final String KEY_MAC = "MAC";
    private static final String KEY_DEVICE_ID = "DEVICE_ID";
    private static final long ONE_MINUTE = 60000;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long SEVEN_DAY = ONE_DAY * 7;
    private static final int[] EMPTY_STATE = new int[]{};
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getFacebookProfilePicture(String url) {
        Bitmap bitmap = null;
        try {
            URL ur = new URL(url);
            bitmap = BitmapFactory.decodeStream(ur.openConnection().getInputStream());
        } catch (IOException e) {
            Log.e("Utilities", "getFacebookProfilePicture IOException", e);
        } catch (Exception e) {
            Log.e("Utilities", "getFacebookProfilePicture Exception", e);
        }
        return bitmap;
    }

    public static boolean isShowKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText())
                return true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    public static void hideKeyboard(View focusingView, Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (focusingView != null) {
                imm.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
//            Log.e("Utilities", "hideKeyboard Exception", e);
        }
    }

    public static String getLocalIpAddress() {
        try {
            String ipv4 = "";
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
                    }

                    // for getting IPV4 format
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
                        // String ip = inetAddress.getHostAddress().toString();
                        return ipv4;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Utilities", "getLocalIpAddress Exception", e);
        }
        return "";
    }

    public static void showKeyboard(View focusingView, Context context) {
        if (context == null)
            return;
        focusingView.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusingView, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String saveBitmapToFile(Bitmap finalBitmap) {
        File myDir = new File(Constants.STORAGE.ROOT_FOLDER + Constants.STORAGE.CACHE_FOLDER);
        Long time = System.currentTimeMillis();
        String fname = "avatar_" + time + ".jpg";
        File file = new File(myDir, fname);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (OutOfMemoryError e) {
            Log.e("Utilities", "saveBitmapToFile OutOfMemoryError", e);
        } catch (Exception e) {
            Log.e("Utilities", "saveBitmapToFile Exception", e);
        }
        return file.getAbsolutePath();
    }

    public static void showSnackBar(Context context, View view, String textNotify, String textAction, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, textNotify, Snackbar.LENGTH_INDEFINITE).setAction(textAction, listener);
        snackbar.setActionTextColor(context.getResources().getColor(R.color.white));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.bg_transparent));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
    }

    public static String getGoogleEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account account = null;
        try {
            Account[] accounts = accountManager.getAccountsByType("com.google");
            if (accounts.length > 0) {
                account = accounts[0];
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public static String getDeviceID(Context context) {
        String deviceIdStr = "";
        try {
            SharedPref config = new SharedPref(context);
            deviceIdStr = config.getString(KEY_DEVICE_ID, "");
            if (TextUtils.isEmpty(deviceIdStr)) {

                TelephonyManager tmp = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceIdStr = tmp.getDeviceId();
                if (TextUtils.isEmpty(deviceIdStr) || "0000000000000000000000000".contains(deviceIdStr)) {
                    deviceIdStr = getDeviceMAC(context);
                }
                config.putString(KEY_DEVICE_ID, deviceIdStr);
            }
        } catch (SecurityException e) {
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(deviceIdStr))
            deviceIdStr = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        return deviceIdStr;
    }

    public static int getWidthScreen(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int deviceWidth = dm.widthPixels;
        return deviceWidth;
    }

    public static String getIMEI(Context context) {
        String imeiStr = "";
        try {
            SharedPref pref = new SharedPref(context);
            imeiStr = pref.getString(KEY_IMEI, "");
            if (TextUtils.isEmpty(imeiStr)) {
                TelephonyManager tmp = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                imeiStr = "" + tmp.getDeviceId();
                if (TextUtils.isEmpty(imeiStr) || "0000000000000000000000000".contains(imeiStr)) {
                    WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInf = wifiMan.getConnectionInfo();
                    imeiStr = wifiInf.getMacAddress();
                }
                pref.putString(KEY_IMEI, imeiStr);
            }
        } catch (Exception e) {
            imeiStr = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        }

        Log.d("Utilities", "getIMEI: " + imeiStr);
        return imeiStr;
    }

    public static String getDeviceMAC(Context context) {
        // ANDROID MAC WIFI
        String macStr = "";
        try {
            SharedPref config = new SharedPref(context);
            macStr = config.getString(KEY_MAC, "");
            if (TextUtils.isEmpty(macStr)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    WifiInfo info = wifiManager.getConnectionInfo();
                    macStr = info.getMacAddress();
                } else {
                    wifiManager.setWifiEnabled(true);
                    WifiInfo info = wifiManager.getConnectionInfo();
                    macStr = info.getMacAddress();
                    wifiManager.setWifiEnabled(false);
                }
                config.putString(KEY_MAC, macStr);
            }
        } catch (Exception e) {
            Log.e("Utilities", "getDeviceMAC", e);
        }
        Log.i("Utilities", "getDeviceMAC: " + macStr);
        return macStr;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String toMD5(String str) {
        String hashtext = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(Charset.forName("UTF8")));
            final byte[] resultByte = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, resultByte);
            hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Utilities", "toMD5 NoSuchAlgorithmException", e);
        } catch (Exception e) {
            Log.e("Utilities", "toMD5 Exception", e);
        }
        return hashtext;
    }

    /*
     * Convert string
     */
    public static String convert(String org) {
        // convert to VNese no sign. @haidh 2008
        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }

                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        return new String(result);
    }

    // lam cho sdt co dang 84xxxxxxxxx
    public static String fixPhoneNumbTo84(String str) {
        if (str == null || str.equals("") || str.length() < 3)
            return "";
        String x = "0123456789";
        for (int i = 0; i < str.length(); i++) {
            if (x.indexOf("" + str.charAt(i)) < 0) {
                str = str.replace("" + str.charAt(i), "");
                i--;
            }
        }
        if (str.startsWith("084")) {
            str = str.substring(1);
        } else if (str.startsWith("0")) {
            str = "84" + str.substring(1);
        } else if (!str.startsWith("84")) {
            str = "84" + str;
        }
        return str.trim();
    }

    public static String fixPhoneNumb(String str) {
        String fixPhoneNumbTo84 = fixPhoneNumbTo84(str);
        if (fixPhoneNumbTo84.length() < 3) {
            return "";
        }
        return fixPhoneNumbTo84.substring(2);
    }

    public static String fixPhoneNumbTo0(String str) {
        String fixPhoneNumb = fixPhoneNumb(str);
        return "0" + fixPhoneNumb;
    }

    /**
     * Function to convert milliseconds time to Timer Format Hours:Minutes:Seconds
     */
    public static String milliSecondsToTimer(long milliseconds) {
        try {
            int hours = (int) (milliseconds / (Constants.TIME.ONE_HOUR));
            int minutes = (int) (milliseconds % (Constants.TIME.ONE_HOUR)) / (Constants.TIME.ONE_MINUTE);
            int seconds = (int) ((milliseconds % (Constants.TIME.ONE_HOUR)) % (Constants.TIME.ONE_MINUTE * 60) / Constants.TIME.ONE_SECOND);

            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(twoDigit(hours)).append(':');
            }
            sb.append(twoDigit(minutes)).append(':');
            sb.append(twoDigit(seconds));
            return sb.toString();
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
        return "";

    }

    static public String twoDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#00");
        return formatter.format(d);
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / Constants.TIME.ONE_SECOND);
        long totalSeconds = (int) (totalDuration / Constants.TIME.ONE_SECOND);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / Constants.TIME.ONE_SECOND);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);
        return currentDuration * Constants.TIME.ONE_SECOND;
    }

    /**
     * Get IP address from first non-localhost interface *
     *
     * @return address or empty string
     */
    public static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress().toUpperCase();
                    } else if (!addr.isLoopbackAddress() && addr instanceof Inet6Address) {
                        return addr.getHostAddress().toUpperCase();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
        return "";
    }

    public static boolean isPhoneNumber(String phone) {
        phone = fixPhoneNumbTo0(phone);
        String regix = "(01\\d{9})|(09\\d{8})|(08\\d{8})";
        Pattern pattern = Pattern.compile(regix);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isFileExistsInSD(String sFileName) {
        if (TextUtils.isEmpty(sFileName))
            return false;
        File file = new File(sFileName);
        return file.exists();
    }

    public static boolean deleteFileExistsInSD(String sFileName) {
        File file = new File(sFileName);
        return file.delete();
    }

    public static boolean isContactExists(Context context, String number) {
        try {
            Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = {PhoneLookup._ID, PhoneLookup.NUMBER};
            Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            if (cur != null && cur.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e("Utilities", "isContactExists", e);
        }
        return false;
    }

    public static long getTimeSecondNow() {
        Date date = new Date();
        return date.getTime() / 1000;
    }

    public static int stringToInt(String str) {
        int val = 0;
        try {
            val = Integer.parseInt(str);
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
        return val;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatHHmm(String timeStr) {
        long time = System.currentTimeMillis();
        try {
            time = Long.parseLong(timeStr);
        } catch (Exception e) {
            return timeStr;
        }
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        String text = sdfDate.format(time * 1000);
        return text;
    }

    /**
     * Xu ly khi muon vao URL
     *
     * @param ctx
     */
    public static void gotoUrl(Context ctx, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Intent intent;
                Log.i("Utilities", "gotoUrl: " + url);
                url = url.toLowerCase();
                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("Utilities gotoUrl", "Exception", e);
        }
    }

//    public static void callPhone(Context context, String sdt) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Utilities.fixPhoneNumbTo84(sdt)));
//            context.startActivity(intent);
//        } catch (Exception e) {
//            Log.e("Utilities", "Exception", e);
//        }
//    }

    public static boolean isInstalledApp(Context context, String packageId) {

        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            Log.e("Utilities", "NameNotFoundException", e);
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
        return false;
    }

    public static void saveFile(String json, String filename) {
        String path = Constants.STORAGE.ROOT_FOLDER + "/";
        File createFolder = new File(path);
        if (!createFolder.exists())
            createFolder.mkdirs();

        try {
            // Create file
            Log.i("Utilities", "saveFileCache filePath: " + path);
            FileWriter fstream = new FileWriter(path + "/" + filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(json);
            out.close();
            fstream.close();
        } catch (IOException e) {
            Log.e("Utilities", "IOException", e);
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
    }

    public static String getFileCache(String filename) {
        String path = Constants.STORAGE.ROOT_FOLDER + "/" + filename;

        String line;
        String result = "";
        File f = new File(path);
        if (!f.exists()) {
            return result;
        }
        try {
            Log.i("Utilities", "getFileCache filePath: " + path);
            FileReader file = new FileReader(path);
            BufferedReader in = new BufferedReader(file);

            while ((line = in.readLine()) != null) {
                result = result + line;
            }
            in.close();
            file.close();
        } catch (IOException e) {
            Log.e("Utilities", "IOException", e);
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }

        return result;
    }

    public static void vibrate(Context context) {
        if (getStateRinger(context) != AudioManager.RINGER_MODE_SILENT) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(300);
        }
    }

    public static int getStateRinger(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode();

    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static boolean gotoApps(Context context, String packageId) {
        try {
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(packageId);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
            return false;
        }
    }

    /**
     * Vao gotoGoogleStore
     *
     * @param activity
     * @param packageId
     */
    public static void gotoGoogleStore(Activity activity, String packageId) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageId)));
        }
    }

    public static String getGoogleStoreLink(Context context) {
        return "https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }

    public static Object parseJson(String json, Class<?> classType) {
        Object listData = new Object();
        Gson gson = new Gson();
        listData = gson.fromJson(json, classType);
        return listData;
    }

    public static void openMessage(Context context, String number, String smstext) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", smstext);
        context.startActivity(intent);
    }

    public static boolean checkUrlStupid(String url) {
        boolean result = false;
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                if (url.length() > 10) {
                    result = true;
                }
            }
        }
        return result;
    }

    public static int getMeasurement(int measureSpec, int contentSize) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int resultSize = 0;
        switch (specMode) {
            case View.MeasureSpec.UNSPECIFIED:
                // Big as we want to be
                resultSize = contentSize;
                break;
            case View.MeasureSpec.AT_MOST:
                // Big as we want to be, up to the spec
                resultSize = Math.min(contentSize, specSize);
                break;
            case View.MeasureSpec.EXACTLY:
                // Must be the spec size
                resultSize = specSize;
                break;
        }

        return resultSize;
    }

    /**
     * Convert dpi to pixels
     */
    public static int dpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = Math.round(dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     * Convert pixels to dpi
     */
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                try {
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                } catch (SecurityException e) {
                    Log.e("Utilities", "SecurityException", e);
                } catch (Exception e) {
                    Log.e("Utilities", "Exception", e);
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void clearState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(EMPTY_STATE);
        }
    }

    public static final String normalPath(String str) {
        String tmpStr = str;
        tmpStr = tmpStr.replace("%", "%25");
        tmpStr = tmpStr.replace("!", "%21");
        tmpStr = tmpStr.replace("#", "%23");
        tmpStr = tmpStr.replace("$", "%24");
        tmpStr = tmpStr.replace("@", "%40");
        tmpStr = tmpStr.replace("~", "%7E");
        tmpStr = tmpStr.replace("^", "%5E");
        return tmpStr;
    }

    public static final void openSendSms(Context context, String body, String address) {
        // Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        // sendIntent.putExtra("sms_body", body);
        // sendIntent.putExtra("address", address);
        // sendIntent.setType("vnd.android-dir/mms-sms");
        // context.startActivity(sendIntent);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("sms_body", body);
        smsIntent.setData(Uri.parse("sms:" + address));
        context.startActivity(smsIntent);
    }

    public static final String getPhoneNumberOnDevice(Context context) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return mTelephonyManager.getLine1Number();
        } catch (RuntimeException e) {
            return "";
        }

    }

    public static final String getSimOperatorName(Context context) {
        String simOperation = "";
        TelephonyManager phoneManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (phoneManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            simOperation = phoneManager.getSimOperatorName();
        }
        return simOperation;
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static int getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int densityDpi = (int) (metrics.density * 160f);
        return densityDpi;
    }

    public static String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder date = new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year);
        return date.toString();
    }

    /**
     * Count all songs in phone.
     */
    public static int getCountSongs(Context context) {
        try {
            // String selection = MediaStore.Audio.Media.IS_MUSIC + " == 1 ";
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media._ID}, null, null, MediaStore.Audio.Media._ID);
            return cursor.getCount();
        } catch (SecurityException e) {
            Log.e("Utilities", "SecurityException", e);
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
        return 0;
    }

    /**
     * Count all videos(only mp4) in phone.
     */
    public static int getCountVideos(Context context) {
        int count = 0;
        try {
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.ARTIST, MediaStore.Video.Media.DATA, MediaStore.Audio.Media.DURATION}, null, null,
                    MediaStore.Audio.Media.DATE_ADDED);
            while (cursor.moveToNext()) {
                if (cursor.getString(4).endsWith(".mp4")) {
                    count++;
                }
            }
        } catch (SecurityException e) {
            Log.e("Utilities", "SecurityException", e);
        } catch (Exception e) {
            Log.e("Utilities", "Exception", e);
        }
        return count;
    }

    /**
     * VD startTime la ngay 20/03/2016, endTime là 21/03/2016 thi tra ve true; endTime la ngay <=
     * 20/03/2016 thi tra ve false
     *
     * @param startTime
     * @param endTime
     * @author namnh40
     */
    public static boolean checkDateTime(long startTime, long endTime) {
        if (startTime >= endTime)
            return false;

        if (startTime <= (endTime - Constants.TIME.ONE_DAY))
            return true;

        try {
            Date startDte = new Date(startTime);
            Date endDte = new Date(endTime);
            if (startDte.getMonth() < endDte.getMonth())
                return true;

            if (startDte.getDate() < endDte.getDate())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getRandomDateString() {
        String str = "";
        Random random = new Random();
        str += random.nextInt(30);
        str += "/";
        str += random.nextInt(12);
        str += "/2016";
        return str;
    }

    public boolean checkTimeout(long time) {
        Date d = new Date(time);
        Date now = new Date();
        return now.before(d);
    }

    public boolean checkTimeoutEvent(long time) {
        Date d = new Date(time);
        Date now = new Date();
        now.setMinutes(0);
        now.setHours(0);
        now.setSeconds(0);
        return now.before(d);
    }

    public String getContentFileFromAssets(Context ctx, String fileName) {
        String line;
        String result = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName)));
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (IOException e) {
            Log.e("Utilities", "ReadFileContent IOException", e);
        } catch (Exception e) {
            Log.e("Utilities", "ReadFileContent Exception", e);
        }

        return result;
    }

    public static void goNextTabURL(Activity activity, String url, String title) {
//        if (activity != null && !TextUtils.isEmpty(url)) {
//            Log.i("Utilities", "goNextTabURL url= " + url + "; title= " + title);
//            Intent intent = new Intent(activity, WebViewActivity.class);
//            intent.putExtra(Constants.BUNDLE_EXTRA.KEY_TITLE, title);
//            intent.putExtra(Constants.BUNDLE_EXTRA.KEY_URL, url);
//            activity.startActivity(intent);
//        }
    }

    public static String getDeviceInfo() {
        String info = "";
        try {
            info += "MANUFACTURER: " + Build.MANUFACTURER;
            info += "; BRAND: " + Build.BRAND;
            info += "; MODEL: " + Build.MODEL;
            info += "; DEVICE: " + Build.DEVICE;
            info += "; PRODUCT: " + Build.PRODUCT;
            info += "; SDK: " + Build.VERSION.SDK;
            info += "; RELEASE: " + Build.VERSION.RELEASE;
            info += "; FINGERPRINT: " + Build.FINGERPRINT;
            info += "; SERIAL: " + Build.SERIAL;

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info.trim();
    }

    public static float getBatteryTemperature(Context context) {
        float temp = 0;
        try {
            Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static boolean isEmulator(Context context) {

        boolean result = false;
        try {
            result = Build.FINGERPRINT.toLowerCase().startsWith("generic")
                    || Build.FINGERPRINT.toLowerCase().startsWith("unknown")
                    || Build.MODEL.toLowerCase().contains("google_sdk")
                    || Build.MODEL.toLowerCase().contains("emulator")
                    || Build.MODEL.toLowerCase().contains("android sdk build for x86")
                    || Build.MANUFACTURER.toLowerCase().contains("genymotion")
                    || (Build.BRAND.toLowerCase().startsWith("generic") && Build.DEVICE.toLowerCase().startsWith("generic"))
                    || Build.PRODUCT.toLowerCase().equals("google_sdk")
                    || (getBatteryTemperature(context) == 0);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}