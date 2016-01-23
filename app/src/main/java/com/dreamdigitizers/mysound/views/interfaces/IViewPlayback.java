package com.dreamdigitizers.mysound.views.interfaces;

import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;

import java.util.List;

public interface IViewPlayback extends IViewRx {
    void onRxNewTracksNext(List<Track> pTracks);
    void onRxFavoritesNext(List<Track> pTracks);
    void onRxPlaylistsNext(List<Playlist> pTracks);
    void onRxPlaylistNext(List<Track> pTracks);
}
