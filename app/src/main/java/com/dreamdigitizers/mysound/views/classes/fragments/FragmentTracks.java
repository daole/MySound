package com.dreamdigitizers.mysound.views.classes.fragments;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.mysound.views.classes.support.MediaItemAdapter;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;

public class FragmentTracks extends FragmentMediaItems {
    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        super.onPlaybackStateChanged(pPlaybackState);
        ((TrackAdapter) this.mMediaItemAdapter).onPlaybackStateChanged(pPlaybackState);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        super.onMetadataChanged(pMediaMetadata);
        ((TrackAdapter) this.mMediaItemAdapter).onMetadataChanged(pMediaMetadata);
    }

    @Override
    protected MediaItemAdapter createAdapter() {
        return new TrackAdapter(this.getContext());
    }
}
