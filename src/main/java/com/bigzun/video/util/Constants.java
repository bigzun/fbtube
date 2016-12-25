package com.bigzun.video.util;

import com.bigzun.video.BuildConfig;

public class Constants {

    public static interface TIME {
        public static final int ONE_SECOND = 1000;
        public static final int ONE_MINUTE = ONE_SECOND * 60;
        public static final long ONE_HOUR = ONE_MINUTE * 60;
        public static final long ONE_DAY = ONE_HOUR * 24;
        public static final long SEVEN_DAY = ONE_DAY * 7;
    }

    public static interface STORAGE {
        public static final String ROOT_FOLDER = BuildConfig.APPLICATION_ID;
        public static final String CACHE_FOLDER = "Cache";
    }

    //TODO key gui du lieu bundle
    public static final String KEY_DATA = "key_data";
    public static final String KEY_ID = "key_id";
    public static final String KEY_URL = "key_url";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_POSITION = "key_position";

    //TODO key SharedPreferences
    public static final String PREF_CHOOSE_REGION = "PREF_CHOOSE_REGION";
    public static final String PREF_LANGUAGES_LIST = "PREF_LANGUAGES_LIST";
    public static final String PREF_REGIONS_LIST = "PREF_REGIONS_LIST";
    public static final String PREF_CLEAR_CACHE_LASTTIME = "PREF_CLEAR_CACHE_LASTTIME";
    public static final String PREF_LANGUAGE_CODE = "languageCode";
    public static final String PREF_REGION_CODE = "regionCode";

    // -------- SharedPreferences end --------

    //TODO key config firebase
    public static final String API_KEY_DEFAULT = "AIzaSyC0HjzhYbWea8s5r2fZuuBcLprlJDmoDd0";
    public static final String YOUTUBE_KEY_DEFAULT = "AIzaSyBX2nhaivfo-vcg7l5nNyvdeTf1yxbrCFE";
    public static final String API_KEY = "api_key";
    public static final String YOUTUBE_KEY = "youtube_key";



    public static final class RECEIVER_MANAGER {

        public static final String ACTION_BROADCAST = "com.bigzun.video.action";
        public static final String ACTION_NAME = "com.bigzun.video.action.name";
        public static final String ACTION_MESSAGE = "com.bigzun.video.action.message";
        public static final String MESSAGE_UPDATE_DATA = "com.bigzun.video.action.message.update.data";
        public static final String MESSAGE_UPDATE_DATA_PLAYER = "com.bigzun.video.action.message.update.data.player";
        //        public static final String MESSAGE_UPDATE_PLAYLIST_PLAYER = "com.bigzun.video.action.message.update.playlist.player";
        public static final String MESSAGE_CLICK_USER = "com.bigzun.video.action.CLICK_USER";
        public static final int VALUE_LOGOUT = 123;
        public static final int VALUE_UPDATE_NOTICE = 2;
        public static final int VALUE_RANDOM_GAME_MESSAGE = 3;
        public static final int ACTION_UPDATE_VIEW = 50;
        public static final int MESSAGE_UPDATE_VIEW_PLIST = 51;
        public static final int VALUE_FINISH = 1;
        public static final int VALUE_TEST = 100;

        public static final int VALUE_MEDIA_PLAYER_CONTROLLER = 150;
        public static final int MESS_MEDIA_PLAYER_CONTROLLER_NEXT = 151;
        public static final int MESS_MEDIA_PLAYER_CONTROLLER_PREV = 152;
        public static final int MESS_MEDIA_PLAYER_CONTROLLER_TOGLE = 153;

        public static final int MESSAGE_UPDATE_VIEW_DOWNLOAD_SONG = 52;
        public static final int MESSAGE_UPDATE_VIEW_DOWNLOAD_VIDEO = 53;
        public static final int MESSAGE_UPDATE_VIEW_DOWNLOAD_ALBUM = 54;

        //        public static final String ACTION_SHOW_MINI_CONTROL = "com.bigzun.action.show.minicontrol";
        public static final String ACTION_START_WIDGET = "com.bigzun.action.play.START_WIDGET";
        public static final String ACTION_CHANGEPLAY = "com.bigzun.action.play.CHANGE_PLAY";
        public static final String ACTION_CHANGEPLAY_BACK = "com.bigzun.action.play.CHANGE_PLAY_BACK";
        public static final String ACTION_CHANGEPLAY_NEXT = "com.bigzun.action.play.CHANGE_PLAY_NEXT";
        public static final String ACTION_CHANGEPLAY_OPEN = "com.bigzun.action.play.CHANGE_PLAY_OPEN";

        public static final int VALUE_CHANGE_PLAY = 101;
        public static final int VALUE_CHANGE_STOP = 102;
        public static final int VALUE_CHANGE_NEXT = 103;
        public static final int VALUE_CHANGE_PREVIOUS = 104;
        public static final int VALUE_CHANGE_REPEAT_STATE = 105;
        public static final int VALUE_WIDGET_OPEN = 106;
        public static final int VALUE_UPDATE_WIDGET = 107;
        public static final int VALUE_WIDGET = 108;
    }

    //TODO request code
    public static final int REQUEST_READ_EXTERNAL_STORAGE_VIDEO_PLAYER = 0x1;
    public static final int REQUEST_SWIPE_NOTIFY_MEDIA_PLAYER = 0x15;
    public static final int REQUEST_UPDATE_NOTIFY_MEDIA_PLAYER = 0x16;
}
