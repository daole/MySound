package com.dreamdigitizers.mysound.views.classes.services.support;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;

public class SoundCloudMetadataBuilder {
    public static final String CUSTOM_METADATA__STREAM_URL = "android.media.custommetadata.STREAM_URL";

    public static MediaMetadataCompat build(Track pTrack) {
        return new MediaMetadataCompat.Builder()
                .putString(SoundCloudMetadataBuilder.CUSTOM_METADATA__STREAM_URL, pTrack.getStreamUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, Integer.toString(pTrack.getId()))
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pTrack.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pTrack.getUser().getUsername())
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, pTrack.getGenre())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, pTrack.getArtworkUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, pTrack.getDescription())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pTrack.getDuration())
                .build();
    }
}
