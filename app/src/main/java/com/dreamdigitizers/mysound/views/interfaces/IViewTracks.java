package com.dreamdigitizers.mysound.views.interfaces;

import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;

import java.util.List;

public interface IViewTracks extends IViewBase {
    void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems);
}
