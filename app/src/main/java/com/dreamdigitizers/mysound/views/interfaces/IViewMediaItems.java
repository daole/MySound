package com.dreamdigitizers.mysound.views.interfaces;

import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;

import java.util.List;

public interface IViewMediaItems extends IViewBase {
    void showRefreshProgress();
    void hideRefreshProgress();
    void showLoadMoreProgress();
    void hideLoadMoreProgress();
    void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop);
}
