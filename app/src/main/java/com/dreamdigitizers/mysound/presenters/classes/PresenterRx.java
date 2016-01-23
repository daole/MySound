package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterRx;
import com.dreamdigitizers.mysound.views.interfaces.IViewRx;

class PresenterRx<V extends IViewRx> extends PresenterBase<V> implements IPresenterRx {
    public PresenterRx(V pView) {
        super(pView);
    }

    protected void onStart() {
        IViewRx view = this.getView();
        if (view != null) {
            view.onRxStart();
        }
    }

    protected void onCompleted() {
        IViewRx view = this.getView();
        if (view != null) {
            view.onRxCompleted();
        }
    }

    protected void onError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
        IViewRx view = this.getView();
        if (view != null) {
            view.onRxError(pError, pRetryAction);
        }
    }
}
