package com.bigzun.video.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import com.bigzun.video.R;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @format: "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
 * @format: "EEE, MMM d, ''yy" Wed, Jul 4, '01
 * @format: "h:mm a" 12:08 PM
 * @format: "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific Daylight Time
 * @format: "K:mm a, z" 0:08 PM, PDT
 * @format: "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
 * @format: "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
 * @format: "yyMMddHHmmssZ" 010704120856-0700
 * @format: "yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
 */
public class DateTimeHelper {
    private static final String TAG = "DateTimeHelper";
    /**
     * Giá trị của 1 giây = 1000ms
     */
    public static long SECOND = 1000;
    /**
     * Giá trị của 1 phút = 1000ms*60s
     */
    public static long MINUTE = 60 * SECOND;
    /**
     * Giá trị của 1 giờ = 1000ms*60s*60m
     */
    public static long HOUR = 60 * MINUTE;
    /**
     * Giá trị của 1 ngày = 1000ms*60s*60m*24h
     */
    public static long DAY = 24 * HOUR;
    /**
     * Giá trị của 1 tuần = 1000ms*60s*60m*24h*7d
     */
    public static long WEEK = 7 * DAY;

    /**
     * @param str    15/10/1990 or 10/05/1990
     * @param format /mm/yyyy or mm/dd/yyyy
     * @return Date
     */
    public static Date stringToDate(String str, String format) {
        String mFormat = format.trim();
        if (mFormat == null)
            mFormat = "dd/MM/yyyy";
        else if (mFormat.equals("dd/mm/yyyy"))
            mFormat = "dd/mm/yyyy";
        else if (mFormat.equals("mm/dd/yyyy"))
            mFormat = "MM/dd/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(mFormat);
        } catch (ParseException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return null;
    }

    /**
     * @format: dd/mm/yyyy or HH:mm:ss or hh:mm:ss a
     */
    public static String dateToString(Date date, String format) {
        if (format == null)
            format = "dd/MM/yyyy";
        else if (format.equals("dd/mm/yyyy"))
            format = "dd/MM/yyyy";
        else if (format.equals("mm/dd/yyyy"))
            format = "MM/dd/yyyy";
        SimpleDateFormat ts = new SimpleDateFormat(format);
        return ts.format(date);
    }

    public static Date getNextDay(Date date) {
        return new Date(date.getTime() + DAY);
    }

    public static Date getNextDay(Date date, int numberDay) {
        return new Date(date.getTime() + DAY * numberDay);
    }

    public static Date getPreviousDay(Date date) {
        return new Date(date.getTime() - DAY);
    }

    public static Date getPreviousDay(Date date, int numberDay) {
        return new Date(date.getTime() - DAY * numberDay);
    }

    public static String getNow() {
        return dateToString(new Date(), "dd/MM/yyyy HH:mm:ss");
    }

    /**
     * ngay1 after ngay2 return 1.<br>
     * ngay1 before ngay2 return -1.<br>
     * ngay1 == ngay2 return 0.<br>
     */
    public static int compareDate(long lDate1, long lDate2) {
        int flag = -2;
        Date date1 = new Date(lDate1 * 1000);
        Date date2 = new Date(lDate2 * 1000);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal1.after(cal2)) {
            flag = 1;
        }
        if (cal1.before(cal2)) {
            flag = -1;
        }
        if (cal1.equals(cal2)) {
            flag = 0;
        }
        return flag;
    }

    public static boolean checkTime(long time, int hour) {
        Date dte = new Date(time);
        Log.d(TAG, "gio hien tai: " + dte.getHours());
        return dte.getHours() == hour;
    }

    /**
     * VD oldTime la ngay 20/03/2016, endTime là 21/03/2016 thi tra ve true;
     * newTime la ngay <= 20/03/2016 thi tra ve false
     *
     * @param oldTime
     * @param newTime
     * @author namnh40
     */
    public static boolean checkDateTime(long oldTime, long newTime) {
        long timeLength = Math.abs(oldTime - newTime);

        if (timeLength >= DAY)
            return true;

        try {
            Date startDte = new Date(oldTime);
            Date endDte = new Date(newTime);
            if (startDte.getMonth() != endDte.getMonth() || startDte.getDate() != endDte.getDate())
                return true;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return false;
    }

    public static String getCurrentDateString() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder date = new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year);
        return date.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormatDate(Context context, String timeStr) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        String text = sdfDate.format(cal.getTime());
        if (text.equalsIgnoreCase(timeStr)) {
//            text = context.getString(R.string.schedule_date_now);
        } else {
            text = timeStr;
        }
        return text;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatHHmm(String timeStr) {
        long time = 0;
        try {
            time = Long.parseLong(timeStr);
        } catch (Exception e) {
            Log.e(TAG, e);
            return timeStr;
        }
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        String text = sdfDate.format(time * 1000);
        return text;
    }

    /**
     * kieu time 1 phut truoc
     *
     * @param context
     * @param timeStr
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String caculateTime(Context context, String timeStr) {
        long time = 0;
        try {
            time = Long.parseLong(timeStr);
        } catch (Exception e) {
            Log.e(TAG, e);
            return timeStr;
        }

//        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM");
//        String txt = context.getString(R.string.feed_time_long) + " " + sdfDate.format(time * 1000);
//        long detal = System.currentTimeMillis() - time * 1000;
//        if (detal > 0 && detal < WEEK) {
//            if (detal > DAY) {
//                int num = (int) (detal / DAY);
//                if (num == 1)
//                    return context.getString(R.string.last_day, num);
//                return context.getString(R.string.last_days, num);
//            } else if (detal > HOUR) {
//                int num = (int) (detal / HOUR);
//                if (num == 1)
//                    return context.getString(R.string.last_hour, num);
//                return context.getString(R.string.last_hours, num);
//            } else if (detal > MINUTE) {
//                int num = (int) (detal / MINUTE);
//                if (num == 1)
//                    return context.getString(R.string.last_minute, num);
//                return context.getString(R.string.last_minutes, num);
//            }
//            return context.getString(R.string.last_minute, 1);
//        } else if (detal < 0) {
//            return context.getString(R.string.last_minute, 1);
//        }
//        return txt;
        return "";
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        int currentSeconds = (int) (currentDuration / SECOND);
        int totalSeconds = (int) (totalDuration / SECOND);

        // calculating percentage
        return currentSeconds * 100 / totalSeconds;
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        return (progress * totalDuration / 100);
    }

    public static int progressToTimer(int progress, long totalDuration) {
        return (int) (progress * totalDuration / 100);
    }

    public static String twoDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#00");
        return formatter.format(d);
    }

    /**
     * Function to convert milliseconds time to Timer Format
     * Hours:Minutes:Seconds
     */
    public static String milliSecondsToTimer(long milliseconds) {
        try {
            int hours = (int) (milliseconds / HOUR);
            int minutes = (int) ((milliseconds % HOUR) / MINUTE);
            int seconds = (int) ((milliseconds % HOUR % MINUTE) / SECOND);

            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(twoDigit(hours)).append(':');
            }
            sb.append(twoDigit(minutes)).append(':');
            sb.append(twoDigit(seconds));
            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "";
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

    public static String hidenPhoneNumber(String msisdn) {

        String sdt = Utilities.fixPhoneNumb(msisdn);
        if (sdt == null || sdt.equals("")) {
            return "";
        }
        int leng = sdt.length();
        try {
//            if (leng == 10) {
//                sdt = sdt.substring(0, 4) + "***" + sdt.substring(leng-2, leng);
//            } else {
//                sdt = sdt.substring(0, 3) + "***" + sdt.substring(leng-2, leng);
//            }

            sdt = sdt.substring(0, 3) + "***" + sdt.substring(leng - 2, leng);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "0" + sdt;
    }

    public static String isNameIsPhoneNumber(String name, String msisdn) {
        name = name == null ? "" : name;
        if ("".equals(msisdn)) {
            return name;
        }
        String sdt = Utilities.fixPhoneNumb(msisdn);
        String name2 = Utilities.fixPhoneNumb(name);
        if (sdt.equals(name2)) {
            return hidenPhoneNumber(msisdn);
        }
        return name;
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}