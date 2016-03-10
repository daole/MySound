package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;

public interface IPresenterPlayback extends IPresenterRx {
    void charts(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset);
    void tracks(UtilsDialog.IRetryAction pRetryAction);
    void tracks(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset);
    void tracks(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset, String pQ);
    //void userFavorites(UtilsDialog.IRetryAction pRetryAction);
    void userFavorites(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, String pOffset);
    void userPlaylists(UtilsDialog.IRetryAction pRetryAction);
    void userPlaylists(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset);
    //void playlist(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset);
    void favorite(UtilsDialog.IRetryAction pRetryAction, Track pTrack);
    void unfavorite(UtilsDialog.IRetryAction pRetryAction, Track pTrack);
    void deletePlaylist(UtilsDialog.IRetryAction pRetryAction, Playlist pPlaylist);
    void addToPlaylist(UtilsDialog.IRetryAction pRetryAction, Track pTrack, Playlist pPlaylist);
    void removeFromPlaylist(UtilsDialog.IRetryAction pRetryAction, Track pTrack, Playlist pPlaylist);
    void createPlaylist(UtilsDialog.IRetryAction pRetryAction, Track pTrack, String pPlaylistTitle, boolean pIsPublic);
}
