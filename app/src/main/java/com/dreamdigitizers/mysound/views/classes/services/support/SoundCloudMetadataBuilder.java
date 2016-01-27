package com.dreamdigitizers.mysound.views.classes.services.support;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;

public class SoundCloudMetadataBuilder {
    public static final String BUNDLE_KEY__TRACK = "track";

    public static MediaMetadataCompat build(Track pTrack) {
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, Integer.toString(pTrack.getId()))
                /*
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pTrack.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pTrack.getUser().getUsername())
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, pTrack.getGenre())
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pTrack.getArtworkUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, pTrack.getDescription())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pTrack.getDuration())
                */
                .build();
        mediaMetadata.getBundle().putSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK, pTrack);
        return mediaMetadata;
    }

    public static MediaDescriptionCompat build(MediaMetadataCompat pMediaMetadata) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK, pMediaMetadata.getBundle().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK));
        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        /*
        mediaDescriptionBuilder.setTitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        mediaDescriptionBuilder.setSubtitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE));
        mediaDescriptionBuilder.setDescription(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION));
        mediaDescriptionBuilder.setIconUri(Uri.parse(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI)));
        */
        mediaDescriptionBuilder.setIconBitmap(pMediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON));
        mediaDescriptionBuilder.setExtras(bundle);
        return mediaDescriptionBuilder.build();
    }
}
