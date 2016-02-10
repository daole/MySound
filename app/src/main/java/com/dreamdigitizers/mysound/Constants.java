package com.dreamdigitizers.mysound;

public class Constants {
    public static final int SPLASH_SCREEN_TIME = 3000;

    public static final String SHARED_PREFERENCES_KEY__ACCESS_TOKEN = "access_token";
    public static final String BUNDLE_KEY__SELECTED_MENU_ID = "selected_menu_id";

    public static final String SOUNDCLOUD__CLIENT_ID = "3087264eee9aea2645a592f1cf600c0d";
    public static final String SOUNDCLOUD__PROTOCOL_SCHEME = "dreamdigitizers.mysound://";
    public static final String SOUNDCLOUD__REDIRECT_URI = Constants.SOUNDCLOUD__PROTOCOL_SCHEME + "soundcloud/callback";
    public static final String SOUNDCLOUD__RESPONSE_TYPE = "token";
    public static final String SOUNDCLOUD__SCOPE = "non-expiring";
    public static final String SOUNDCLOUD__DISPLAY = "popup";
    public static final String SOUNDCLOUD__STATE = "";

    public static final int SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING = 1;
    public static final int SOUNDCLOUD_PARAMETER__LIMIT = 50;
}
