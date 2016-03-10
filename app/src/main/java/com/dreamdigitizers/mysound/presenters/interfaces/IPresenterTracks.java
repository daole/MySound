package com.dreamdigitizers.mysound.presenters.interfaces;

import android.support.v4.media.MediaBrowserCompat;

public interface IPresenterTracks extends IPresenterMediaItems {
    void loadAllPlaylists();
    void playFromMediaId(MediaBrowserCompat.MediaItem pMediaItem);
    void favorite(MediaBrowserCompat.MediaItem pMediaItem);
    void addToRemoveFromPlaylist(MediaBrowserCompat.MediaItem pTrack, MediaBrowserCompat.MediaItem pPlaylist, boolean pIsAdd);
    void createPlaylist(MediaBrowserCompat.MediaItem pTrack, String pPlaylistTitle, boolean pIsPublic);
}
