package com.dreamdigitizers.mysound;

public class Constants {
    public static final int SPLASH_SCREEN_TIME = 3000;

    public static final int SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING = 1;
    public static final int SOUNDCLOUD_PARAMETER__LIMIT = 20;
    public static final String SOUNDCLOUD_PARAMETER__KIND_TOP = "top";
    public static final String SOUNDCLOUD_PARAMETER__GENRE_ALL_MUSIC = "soundcloud:genres:all-music";
    public static final String SOUNDCLOUD_PARAMETER__PUBLIC = "public";
    public static final String SOUNDCLOUD_PARAMETER__PRIVATE = "private";
    public static final String SOUNDCLOUD_PARAMETER__UNDEFINED = "undefined";
    public static final String SOUNDCLOUD_PARAMETER__PLAYLIST = "playlist";

    public static final String SOUNDCLOUD__CLIENT_ID = "3087264eee9aea2645a592f1cf600c0d";
    public static final String SOUNDCLOUD__PROTOCOL_SCHEME = "dreamdigitizers.mysound://";
    public static final String SOUNDCLOUD__REDIRECT_URI = Constants.SOUNDCLOUD__PROTOCOL_SCHEME + "soundcloud/callback";
    public static final String SOUNDCLOUD__RESPONSE_TYPE = "token";
    public static final String SOUNDCLOUD__SCOPE = "non-expiring";
    public static final String SOUNDCLOUD__DISPLAY = "popup";
    public static final String SOUNDCLOUD__STATE = "";

    public static final String BUNDLE_KEY__TRACK = "track";
    public static final String BUNDLE_KEY__PLAYLIST = "playlist";
    public static final String BUNDLE_KEY__PLAYLISTS = "playlists";
    public static final String BUNDLE_KEY__PLAYLIST_TITLE = "playlist_title";
    public static final String BUNDLE_KEY__IS_PUBLIC = "is_public";
    public static final String BUNDLE_KEY__SELECTED_MENU_ID = "selected_menu_id";
    public static final String BUNDLE_KEY__QUERY = "query";
    public static final String BUNDLE_KEY__MEDIA_ITEM = "media_item";
    public static final String BUNDLE_KEY__IS_LOGOUT = "is_logout";

    public static final String SHARED_PREFERENCES_KEY__ACCESS_TOKEN = "access_token";
}
