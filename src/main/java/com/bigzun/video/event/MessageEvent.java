package com.bigzun.video.event;

import com.bigzun.video.model.MediaPlaying;
import com.bigzun.video.util.Log;

/**
 * Created by namnh40 on 11/25/2016.
 */

public class MessageEvent {

    private final String TAG = MessageEvent.class.getSimpleName();
    private MediaPlaying mediaPlaying;
    private int position = 0;
    private int state;

    public MessageEvent() {
    }

    public MessageEvent(MediaPlaying item) {
        mediaPlaying = item;
    }

    public MediaPlaying getMediaPlaying() {
        return mediaPlaying;
    }

    public void setMediaPlaying(MediaPlaying mediaPlaying) {
        this.mediaPlaying = mediaPlaying;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void showMediaPlaying() {
        Log.e(TAG, "media: " + mediaPlaying);
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "mediaPlaying=" + mediaPlaying +
                ", position=" + position +
                ", state=" + state +
                '}';
    }
}
