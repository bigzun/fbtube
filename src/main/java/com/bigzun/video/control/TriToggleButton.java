package com.bigzun.video.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bigzun.video.R;
import com.bigzun.video.service.PlayMediaService;
import com.bigzun.video.util.Toast;

public class TriToggleButton extends ImageView {
    // Keeps track of the current state, 0, 1, or 2
    private int _state;

    // Constructors
    public TriToggleButton(Context context) {
        super(context);
        _state = 0;
        this.setImageResource(R.drawable.btn_repeat_all);
    }

    public TriToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        _state = 0;
        this.setImageResource(R.drawable.btn_repeat_all);
    }

    public TriToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _state = 0;
        this.setImageResource(R.drawable.btn_repeat_all);
    }

    @Override
    public boolean performClick() {
        setState(++_state, true);
        return super.performClick();
    }

    // Set current state, 0-2
    public void setState(int state, boolean showToast) {
        if (state > 2 || state < 0) {
            _state = 0;
        } else {
            _state = state;
        }
        switch (_state) {
            case PlayMediaService.REPEAT_ONE:
                this.setImageResource(R.drawable.btn_repeat_one);
                if (showToast)
                    Toast.makeText(getContext(), getContext().getString(R.string.repeat_one));
                break;

            case PlayMediaService.REPEAT_SUFF:
                this.setImageResource(R.drawable.btn_shuffle);
                if (showToast)
                    Toast.makeText(getContext(), getContext().getString(R.string.repeat_shuffle));
                break;

            default:
                this.setImageResource(R.drawable.btn_repeat_all);
                if (showToast)
                    Toast.makeText(getContext(), getContext().getString(R.string.repeat_all));
                break;
        }

    }

    public int getState() {
        return _state;
    }

}