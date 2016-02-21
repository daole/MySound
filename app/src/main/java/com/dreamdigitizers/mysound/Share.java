package com.dreamdigitizers.mysound;

import android.os.Bundle;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class Share {
    public static final String SHARE_KEY__ACCESS_TOKEN = "access_token";
    public static final String SHARE_KEY__ME = "me";
    public static final String SHARE_KEY__CURRENT_TRACK = "current_track";

    private static final Object dummy = new Object();
    private static final Bundle bundle = new Bundle();
    private static final Map<IOnDataChangedListener, Object> listeners = Collections.synchronizedMap(new WeakHashMap<IOnDataChangedListener, Object>());

    public static String getAccessToken() {
        return Share.bundle.getString(Share.SHARE_KEY__ACCESS_TOKEN, "");
    }

    public static void setAccessToken(String pAccessToken) {
        if (pAccessToken != null) {
            String oldAccessToken = Share.getAccessToken();
            if (!UtilsString.equals(pAccessToken, oldAccessToken)) {
                Share.bundle.putString(Share.SHARE_KEY__ACCESS_TOKEN, pAccessToken);
                for (Iterator<IOnDataChangedListener> iterator = Share.listeners.keySet().iterator(); iterator.hasNext();) {
                    IOnDataChangedListener listener = iterator.next();
                    listener.onAccessTokenChanged(pAccessToken, oldAccessToken);
                }
            }
        }
    }

    public static Me getMe() {
        return (Me) Share.bundle.getSerializable(Share.SHARE_KEY__ME);
    }

    public static void setMe(Me pMe) {
        if (pMe != null) {
            Me oldMe = Share.getMe();
            if (pMe != oldMe) {
                Share.bundle.putSerializable(Share.SHARE_KEY__ME, pMe);
                for (Iterator<IOnDataChangedListener> iterator = Share.listeners.keySet().iterator(); iterator.hasNext();) {
                    IOnDataChangedListener listener = iterator.next();
                    listener.onMeChanged(pMe, oldMe);
                }
            }
        }
    }

    public static Track getCurrentTrack() {
        return (Track) Share.bundle.getSerializable(Share.SHARE_KEY__CURRENT_TRACK);
    }

    public static void setCurrentTrack(Track pTrack) {
        if (pTrack != null) {
            Track oldTrack = Share.getCurrentTrack();
            if (pTrack != oldTrack) {
                Share.bundle.putSerializable(Share.SHARE_KEY__CURRENT_TRACK, pTrack);
                for (Iterator<IOnDataChangedListener> iterator = Share.listeners.keySet().iterator(); iterator.hasNext();) {
                    IOnDataChangedListener listener = iterator.next();
                    listener.onCurrentTrackChanged(pTrack, oldTrack);
                }
            }
        }
    }

    public static void registerListener(IOnDataChangedListener pListener) {
        if (pListener != null && !Share.listeners.keySet().contains(pListener)) {
            Share.listeners.put(pListener, dummy);
        }
    }

    public static void unregisterListener(IOnDataChangedListener pListener) {
        if (pListener != null && Share.listeners.keySet().contains(pListener)) {
            Share.listeners.remove(pListener);
        }
    }

    public static void dispose() {
        Share.bundle.clear();
        Share.listeners.clear();
    }

    public interface IOnDataChangedListener {
        void onAccessTokenChanged(String pNewValue, String pOldValue);
        void onMeChanged(Me pNewMe, Me pOldMe);
        void onCurrentTrackChanged(Track pNewTrack, Track pOldTrack);
    }

    public static class OnDataChangedListener implements IOnDataChangedListener {
        @Override
        public void onAccessTokenChanged(String pNewValue, String pOldValue) {
        }

        @Override
        public void onMeChanged(Me pNewMe, Me pOldMe) {
        }

        @Override
        public void onCurrentTrackChanged(Track pNewTrack, Track pOldTrack) {
        }
    }
}
