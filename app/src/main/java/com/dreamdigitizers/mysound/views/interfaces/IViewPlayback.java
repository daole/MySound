package com.dreamdigitizers.mysound.views.interfaces;

import com.dreamdigitizers.androidsoundcloudapi.models.Playlists;
import com.dreamdigitizers.androidsoundcloudapi.models.Tracks;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.androidsoundcloudapi.models.v2.Charts;

import java.util.List;

public interface IViewPlayback extends IViewRx {
    void onRxChartsNext(Charts pCharts);
    void onRxSoundsNext(List<Track> pTracks);
    void onRxSoundsNext(Tracks pCollection);
    void onRxSoundsSearchNext(Tracks pCollection);
    //void onRxFavoritesNext(List<Track> pTracks);
    void onRxFavoritesNext(Tracks pCollection);
    void onRxPlaylistsNext(List<Playlist> pPlaylists);
    void onRxPlaylistsNext(Playlists pPlaylists);
    //void onRxPlaylistNext(List<Track> pTracks);
    void onRxFavoriteNext(Track pTrack);
    void onRxUnfavoriteNext(Track pTrack);
    void onRxDeletePlaylistNext(Playlist pPlaylist);
    void onRxAddToPlaylistNext(Track pTrack, Playlist pPlaylist);
    void onRxRemoveFromPlaylistNext(Track pTrack, Playlist pPlaylist);
    void onRxCreatePlaylistNext(Track pTrack, Playlist pPlaylist);
}
