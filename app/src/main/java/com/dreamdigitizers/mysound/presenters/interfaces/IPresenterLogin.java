package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenter;

public interface IPresenterLogin extends IPresenter {
    void saveAccessToken(String pAccessToken);
}
