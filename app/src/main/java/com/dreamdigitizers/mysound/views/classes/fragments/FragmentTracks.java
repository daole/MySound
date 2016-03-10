package com.dreamdigitizers.mysound.views.classes.fragments;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.mysound.views.classes.support.AdapterMediaItem;
import com.dreamdigitizers.mysound.views.classes.support.AdapterTrack;

public class FragmentTracks extends FragmentMediaItems {
    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        super.onPlaybackStateChanged(pPlaybackState);
        ((AdapterTrack) this.mMediaItemAdapter).onPlaybackStateChanged(pPlaybackState);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        super.onMetadataChanged(pMediaMetadata);
        ((AdapterTrack) this.mMediaItemAdapter).onMetadataChanged(pMediaMetadata);
    }

    @Override
    protected AdapterMediaItem createAdapter() {
        return new AdapterTrack(this.getContext());
    }
}
