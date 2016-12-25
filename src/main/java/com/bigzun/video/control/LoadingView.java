package com.bigzun.video.control;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigzun.video.R;

/**
 * Created by namnh40 on 10/13/2016.
 */

public class LoadingView extends LinearLayout {
    private TextView btnRetry;
    private View emptyView;
    private TextView loadingError;
    private TextView loadingErrorSubtext;
    private TextView loaddingEmpty;
    private View loadingErrorView;
    private View progressBarLayout;
    private LinearLayout layoutMain;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (!isInEditMode()) {
            ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading, this, true);
            this.progressBarLayout = getRootView().findViewById(R.id.progress_bar_layout);
            this.loadingError = ((TextView) getRootView().findViewById(R.id.loadingerror));
            this.loadingErrorSubtext = ((TextView) getRootView().findViewById(R.id.loadingerror_subtext));
            this.loaddingEmpty = ((TextView) getRootView().findViewById(R.id.loading_empty));
            this.emptyView = getRootView().findViewById(R.id.empty_layout);
            this.btnRetry = ((TextView) getRootView().findViewById(R.id.btn_retry));
            this.loadingErrorView = getRootView().findViewById(R.id.loading_error_layout);
            this.layoutMain = (LinearLayout) getRootView().findViewById(R.id.layout_main);
        }
    }

    @SuppressWarnings("deprecation")
    public void loadBegin() {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(VISIBLE);
        this.loadingError.setText(getContext().getResources().getString(R.string.connection_error2));
        this.btnRetry.setText(getContext().getResources().getString(R.string.retry));
        this.loadingError.setVisibility(GONE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(GONE);
        this.emptyView.setVisibility(GONE);
        this.btnRetry.setVisibility(GONE);
    }

    @SuppressWarnings("deprecation")
    public void loadEmpty() {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(GONE);
        this.loadingError.setVisibility(VISIBLE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(VISIBLE);
        this.btnRetry.setVisibility(GONE);
        this.loadingError.setText(Html.fromHtml(getResources().getString(R.string.pull_to_load_more_no_result)));
    }

    @SuppressWarnings("deprecation")
    public void loadEmpty(String str) {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(GONE);
        this.loadingError.setVisibility(VISIBLE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(VISIBLE);
        this.btnRetry.setVisibility(GONE);
        if (!TextUtils.isEmpty(str))
            this.loadingError.setText(Html.fromHtml(str));
    }

    @SuppressWarnings("deprecation")
    public void loadError() {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(GONE);
        this.loadingError.setVisibility(VISIBLE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(VISIBLE);
        this.btnRetry.setVisibility(GONE);
    }

    @SuppressWarnings("deprecation")
    public void loadError(String paramString) {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(GONE);
        this.loadingError.setVisibility(VISIBLE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(VISIBLE);
        this.btnRetry.setVisibility(GONE);
        if (!TextUtils.isEmpty(paramString))
            this.loadingError.setText(Html.fromHtml(paramString));
    }

    @SuppressWarnings("deprecation")
    public void loadFinish() {
        setVisibility(GONE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(GONE);
        this.loadingError.setVisibility(GONE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(GONE);
        this.btnRetry.setVisibility(GONE);
    }

    @SuppressWarnings("deprecation")
    public void notice(String paramString) {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.transparent));
        emptyView.setBackgroundColor(getResources().getColor(R.color.transparent));
        loaddingEmpty.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.progressBarLayout.setVisibility(GONE);
        this.btnRetry.setVisibility(GONE);
        this.emptyView.setVisibility(View.VISIBLE);
        this.loadingError.setVisibility(GONE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(GONE);
        this.loadingError.setVisibility(View.GONE);
        this.loaddingEmpty.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(paramString)) {
            this.loaddingEmpty.setText(paramString);
        }

    }

    @SuppressWarnings("deprecation")
    public void loadLogin(String str) {
        setVisibility(VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.white));
        this.progressBarLayout.setVisibility(GONE);
        this.loadingError.setVisibility(VISIBLE);
        this.loadingErrorSubtext.setVisibility(GONE);
        this.loadingErrorView.setVisibility(VISIBLE);
        this.btnRetry.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(str))
            this.loadingError.setText(str);
        this.btnRetry.setText(Html.fromHtml(getResources().getString(R.string.sign_in)));
    }

    public void setLoadingErrorListener(View.OnClickListener paramOnClickListener) {
        if (paramOnClickListener != null)
            this.loadingError.setOnClickListener(paramOnClickListener);
    }

    public void setBtnRetryListener(View.OnClickListener paramOnClickListener) {
        if (paramOnClickListener != null)
            this.btnRetry.setOnClickListener(paramOnClickListener);
    }
}
