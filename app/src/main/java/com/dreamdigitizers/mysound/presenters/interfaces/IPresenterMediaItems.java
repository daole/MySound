package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenterBase;

public interface IPresenterMediaItems extends IPresenterBase {
    void connect();
    void disconnect();
    void refresh();
    void loadMore();
    void skipToPrevious();
    void play();
    void pause();
    void skipToNext();
    void seekTo(int pPosition);
}
