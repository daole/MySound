package com.dreamdigitizers.mysound.views.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;

public interface IViewRx extends IViewBase {
    void onRxStart();
    void onRxCompleted();
    void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction);
}
