package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterHome;
import com.dreamdigitizers.mysound.views.interfaces.IViewHome;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class PresenterHome extends PresenterRx<IViewHome> implements IPresenterHome {
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

    private void unsubscribe() {
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }
}
