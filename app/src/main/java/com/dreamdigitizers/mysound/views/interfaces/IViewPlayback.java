package com.dreamdigitizers.mysound.views.interfaces;

import com.dreamdigitizers.androidsoundcloudapi.models.Collection;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.androidsoundcloudapi.models.v2.Charts;

import java.util.List;

public interface IViewPlayback extends IViewRx {
    void onRxChartsNext(Charts pCharts);
    void onRxSoundsNext(List<Track> pTracks);
    void onRxSoundsNext(Collection pCollection);
    void onRxSoundsSearchNext(Collection pCollection);
    void onRxFavoritesNext(List<Track> pTracks);
    void onRxFavoritesNext(Collection pCollection);
    void onRxPlaylistsNext(List<Playlist> pTracks);
    void onRxPlaylistNext(List<Track> pTracks);
    void onRxFavoriteNext(Track pTrack);
    void onRxUnfavoriteNext(Track pTrack);
}
