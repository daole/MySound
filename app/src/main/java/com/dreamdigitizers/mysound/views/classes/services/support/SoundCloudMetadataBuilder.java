package com.dreamdigitizers.mysound.views.classes.services.support;

import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;

public class SoundCloudMetadataBuilder {
    public static MediaMetadataCompat build(Track pTrack) {
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, Integer.toString(pTrack.getId()))
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pTrack.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pTrack.getUser().getUsername())
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, pTrack.getGenre())
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pTrack.getArtworkUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, pTrack.getDescription())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pTrack.getDuration())
                .build();
    }
}
