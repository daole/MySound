package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterHome;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewHome;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class PresenterHome extends PresenterTracks<IViewHome> implements IPresenterHome {
    private Subscription mSubscription;

    public PresenterHome(IViewHome pView) {
        super(pView);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unsubscribe();
    }

    @Override
    protected String getMediaId() {
        return ServicePlayback.MEDIA_ID__CHARTS;
    }

    @Override
    protected String getMediaIdRefresh() {
        return null;
    }

    @Override
    protected String getMediaIdMore() {
        return ServicePlayback.MEDIA_ID__CHARTS_MORE;
    }

    @Override
    public void me(final String pAccessToken, final UtilsDialog.IRetryAction pRetryAction) {
        this.unsubscribe();
        this.mSubscription = ApiFactory.getApiInstance()
                .meRx(pAccessToken)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Me>() {
                    @Override
                    public void onStart() {
                        PresenterHome.this.onStart();
                    }

                    @Override
                    public void onNext(Me pMe) {
                        IViewHome view = PresenterHome.this.getView();
                        if (view != null) {
                            view.onRxNext(pMe);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterHome.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterHome.this.onError(pError, pRetryAction);
                    }
                });
    }

    private void onStart() {
        IViewHome view = this.getView();
        if (view != null) {
            view.onRxStart();
        }
    }

    private void onCompleted() {
        IViewHome view = this.getView();
        if (view != null) {
            view.onRxCompleted();
        }
    }

    private void onError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
        IViewHome view = this.getView();
        if (view != null) {
            view.onRxError(pError, pRetryAction);
        }
    }

    private void unsubscribe() {
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }
}
