package com.dreamdigitizers.mysound;

import android.os.Bundle;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;

import java.util.ArrayList;
import java.util.List;

public class Share {
    public static final String SHARE_KEY__ACCESS_TOKEN = "access_token";
    public static final String SHARE_KEY__MY_DATA = "my_data";

    private static final Bundle bundle = new Bundle();
    private static final List<IOnDataChanged> listeners = new ArrayList();

    public static void setAccessToken(String pAccessToken) {
        if (pAccessToken != null) {
            String oldAccessToken = Share.getAccessToken();
            if (!UtilsString.equals(pAccessToken, oldAccessToken)) {
                Share.bundle.putString(Share.SHARE_KEY__ACCESS_TOKEN, pAccessToken);
                for (IOnDataChanged listener : Share.listeners) {
                    listener.onAccessTokenChanged(pAccessToken, oldAccessToken);
                }
            }
        }
    }

    public static String getAccessToken() {
        return Share.bundle.getString(Share.SHARE_KEY__ACCESS_TOKEN, "");
    }

    public static void setMe(Me pMe) {
        if (pMe != null) {
            Me oldMe = Share.getMe();
            if (pMe != oldMe) {
                Share.bundle.putSerializable(Share.SHARE_KEY__MY_DATA, pMe);
                for (IOnDataChanged listener : Share.listeners) {
                    listener.onMeChanged(pMe, oldMe);
                }
            }
        }
    }

    public static Me getMe() {
        return (Me) Share.bundle.getSerializable(Share.SHARE_KEY__MY_DATA);
    }

    public static void registerListener(IOnDataChanged pListener) {
        if (pListener != null && !Share.listeners.contains(pListener)) {
            Share.listeners.add(pListener);
        }
    }

    public static void unregisterListener(IOnDataChanged pListener) {
        if (pListener != null && Share.listeners.contains(pListener)) {
            Share.listeners.remove(pListener);
        }
    }

    public static void dispose() {
        Share.bundle.clear();
        Share.listeners.clear();
    }

    public interface IOnDataChanged {
        void onAccessTokenChanged(String pNewValue, String pOldValue);
        void onMeChanged(Me pNewMe, Me pOldMe);
    }

    public static class OnDataChanged implements IOnDataChanged {
        @Override
        public void onAccessTokenChanged(String pNewValue, String pOldValue) {
        }

        @Override
        public void onMeChanged(Me pNewMe, Me pOldMe) {
        }
    }
}
