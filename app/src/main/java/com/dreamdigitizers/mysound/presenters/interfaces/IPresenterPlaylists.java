package com.dreamdigitizers.mysound.presenters.interfaces;

import android.support.v4.media.MediaBrowserCompat;

public interface IPresenterPlaylists extends IPresenterMediaItems {
    void deletePlaylist(MediaBrowserCompat.MediaItem pMediaItem);
}
