package com.dreamdigitizers.mysound.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;

public interface IPresenterHome extends IPresenterRx {
    void me(String pAccessToken, UtilsDialog.IRetryAction pRetryAction);
}
