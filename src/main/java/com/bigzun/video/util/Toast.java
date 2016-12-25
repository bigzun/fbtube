package com.bigzun.video.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigzun.video.R;

public class Toast {
    public static void makeText(Context context, String text) {
        makeText(context, text, android.widget.Toast.LENGTH_LONG);
    }

    public static void makeText(Context context, int resId) {
        makeText(context, resId, android.widget.Toast.LENGTH_LONG);
    }

    private static void makeText(Context context, String text, int dur) {
        if (context == null || TextUtils.isEmpty(text))
            return;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.layout_toast, null);
        TextView tv = (TextView) toastRoot.findViewById(R.id.text);
        tv.setText(text);
        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setView(toastRoot);

        // toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
        // 0, 100);

        toast.setDuration(dur);
        toast.show();
    }

    private static void makeText(Context context, int resId, int dur) {
        if (context == null)
            return;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.layout_toast, null);
        TextView tv = (TextView) toastRoot.findViewById(R.id.text);
        tv.setText(resId);
        android.widget.Toast toast = new android.widget.Toast(context);

        toast.setView(toastRoot);
        // toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
        // 0, 100);

        toast.setDuration(dur);
        toast.show();
    }
}
