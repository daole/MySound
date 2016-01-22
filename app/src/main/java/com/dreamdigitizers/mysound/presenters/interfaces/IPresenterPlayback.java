package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenter;

public interface IPresenterPlayback extends IPresenter {
    void tracks();
    void userFavorites(int pId);
    void playlists(int pId);
    void playlist(int pId);
}
