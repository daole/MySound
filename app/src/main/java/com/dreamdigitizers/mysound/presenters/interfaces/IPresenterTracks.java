package com.dreamdigitizers.mysound.presenters.interfaces;

import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenterBase;

public interface IPresenterTracks extends IPresenterBase {
    void connect();
    void disconnect();
    void skipToPrevious();
    void playFromMediaId(MediaBrowserCompat.MediaItem pMediaItem);
    void play();
    void pause();
    void skipToNext();
    void seekTo(int pPosition);
}
