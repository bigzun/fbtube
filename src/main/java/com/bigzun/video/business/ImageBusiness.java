package com.bigzun.video.business;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bigzun.video.R;
import com.bigzun.video.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.NotificationTarget;

public class ImageBusiness {
    private final String TAG = ImageBusiness.class.getSimpleName();
    private final int errorResId = R.mipmap.ic_placeholder;
    private final int errorAvatarResId = R.drawable.df_avatar;
    private final int FADE_DURATION = 1500;
    private final DiskCacheStrategy cacheType = DiskCacheStrategy.ALL;
    private Context mContext;

    public ImageBusiness(Context context) {
        mContext = context;
    }

    public void setImage(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }

    }

    public void setAvatar(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorAvatarResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void setCover(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void setSong(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void setAlbum(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void setVideo(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void setPlaylist(ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url))
                Glide.with(mContext)
                        .load(url)
                        .error(errorResId)
                        .diskCacheStrategy(cacheType)
                        .crossFade(FADE_DURATION)
                        .into(imageView);
            else
                Glide.with(mContext).load(errorResId).into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }

    }

    public void setNotification(String url, int resourceId, NotificationTarget target) {
        try {
            Glide.with(mContext).load(url).asBitmap().error(resourceId).into(target);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }

    }
}
