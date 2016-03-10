package com.dreamdigitizers.mysound.views.interfaces;

import android.support.v4.media.MediaBrowserCompat;

import java.util.List;

public interface IViewTracks extends IViewMediaItems {
    void onAllPlaylistsLoaded(List<MediaBrowserCompat.MediaItem> pMediaItems);
    void onAddToRemoveFromPlaylistResult();
    void onPlaylistCreated();
}
