package com.dreamdigitizers.mysound.views.interfaces;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;

import java.util.List;

public interface IViewTracks extends IViewMediaItems {
    void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState);
    void onMetadataChanged(MediaMetadataCompat pMediaMetadata);
    void updateState();
}
