package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenterBase;

public interface IPresenterMain extends IPresenterBase {
    String getAccessToken();
    void deleteAccessToken();
    void stopMediaPlayer();
}
