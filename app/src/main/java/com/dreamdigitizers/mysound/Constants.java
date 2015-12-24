package com.dreamdigitizers.mysound;

public class Constants {
    public static final int SPLASH_SCREEN_TIME = 3000;

    public static final String AMPERSAND = "&";

    public static final String SHARED_PREFERENCES_KEY__ACCESS_TOKEN = "access_token";

    public static final String SOUNDCLOUD__ACCESS_TOKEN_KEY = "access_token=";
    public static final String SOUNDCLOUD__CLIENT_ID = "3087264eee9aea2645a592f1cf600c0d";
    public static final String SOUNDCLOUD__PROTOCOL_SCHEME = "dreamdigitizers.mysound://";
    public static final String SOUNDCLOUD__REDIRECT_URL = Constants.SOUNDCLOUD__PROTOCOL_SCHEME + "soundcloud/callback";
    public static final String SOUNDCLOUD_API__CONNECT = "https://soundcloud.com/connect"
            + "?client_id=" + Constants.SOUNDCLOUD__CLIENT_ID
            + "&redirect_uri=" + Constants.SOUNDCLOUD__REDIRECT_URL
            + "&response_type=token&scope=non-expiring&display=popup";
}
