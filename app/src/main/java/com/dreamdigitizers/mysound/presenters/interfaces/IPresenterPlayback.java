package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;

public interface IPresenterPlayback extends IPresenterRx {
    void tracks(UtilsDialog.IRetryAction pRetryAction);
    void userFavorites(int pId, UtilsDialog.IRetryAction pRetryAction);
    void playlists(int pId, UtilsDialog.IRetryAction pRetryAction);
    void playlist(int pId, UtilsDialog.IRetryAction pRetryAction);
}
