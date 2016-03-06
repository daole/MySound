package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterRx;
import com.dreamdigitizers.mysound.views.interfaces.IViewRx;

public abstract class ScreenRx<P extends IPresenterRx> extends ScreenBase<P> implements IViewRx {
    @Override
    public void onRxStart() {
        this.showNetworkProgress();
    }

    @Override
    public void onRxCompleted() {
        this.hideNetworkProgress();
    }

    @Override
    public void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
        pError.printStackTrace();
        this.hideNetworkProgress();
        this.showRetryableError(
                R.string.error__retryable_network,
                false,
                pRetryAction);
    }
}
