package com.dreamdigitizers.mysound.presenters.interfaces;

import android.support.v4.media.MediaBrowserCompat;

public interface IPresenterTracks extends IPresenterMediaItems {
    void playFromMediaId(MediaBrowserCompat.MediaItem pMediaItem);
    void favorite(MediaBrowserCompat.MediaItem pMediaItem);
}
