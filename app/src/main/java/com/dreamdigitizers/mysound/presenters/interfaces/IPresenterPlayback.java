package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;

public interface IPresenterPlayback extends IPresenterRx {
    void tracks(UtilsDialog.IRetryAction pRetryAction);
    void tracks(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset);
    void tracks(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, int pOffset, String pQ);
    void userFavorites(UtilsDialog.IRetryAction pRetryAction);
    void userFavorites(UtilsDialog.IRetryAction pRetryAction, int pLinkedPartitioning, int pLimit, String pOffset);
    void playlists(UtilsDialog.IRetryAction pRetryAction);
    void playlist(UtilsDialog.IRetryAction pRetryAction);
    void favorite(UtilsDialog.IRetryAction pRetryAction, Track pTrack);
    void unfavorite(UtilsDialog.IRetryAction pRetryAction, Track pTrack);
}
