package com.dreamdigitizers.mysound.views.classes.services.support;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;

public class SoundCloudMetadataBuilder {
    public static final String BUNDLE_KEY__TRACK = "track";
    public static final String BUNDLE_KEY__PLAYLIST = "playlist";

    public static MediaMetadataCompat build(Track pTrack) {
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, Integer.toString(pTrack.getId()))
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pTrack.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pTrack.getUser().getUsername())
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, pTrack.getGenre())
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pTrack.getArtworkUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, pTrack.getDescription())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pTrack.getDuration())
                .build();
        mediaMetadata.getBundle().putSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK, pTrack);
        return mediaMetadata;
    }

    public static MediaMetadataCompat build(Playlist pPlaylist) {
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, Integer.toString(pPlaylist.getId()))
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pPlaylist.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pPlaylist.getUser().getUsername())
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, pPlaylist.getGenre())
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pPlaylist.getArtworkUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, pPlaylist.getDescription())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pPlaylist.getDuration())
                .build();
        mediaMetadata.getBundle().putSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST, pPlaylist);
        return mediaMetadata;
    }

    public static MediaDescriptionCompat build(MediaMetadataCompat pMediaMetadata) {
        Bundle bundle = new Bundle();
        Bundle mediaMetadataBundle = pMediaMetadata.getBundle();
        if (mediaMetadataBundle.containsKey(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK)) {
            bundle.putSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK, mediaMetadataBundle.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK));
        } else if (mediaMetadataBundle.containsKey(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST)) {
            bundle.putSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST, mediaMetadataBundle.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST));
        }
        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        mediaDescriptionBuilder.setTitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        mediaDescriptionBuilder.setSubtitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR));
        mediaDescriptionBuilder.setDescription(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION));
        String artUri = pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
        mediaDescriptionBuilder.setIconUri(artUri == null ? null : Uri.parse(artUri));
        mediaDescriptionBuilder.setIconBitmap(pMediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON));
        mediaDescriptionBuilder.setExtras(bundle);
        return mediaDescriptionBuilder.build();
    }
}
