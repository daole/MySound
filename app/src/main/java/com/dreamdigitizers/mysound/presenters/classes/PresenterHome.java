package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.Presenter;
import com.dreamdigitizers.androidbaselibrary.utils.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterHome;
import com.dreamdigitizers.mysound.views.interfaces.IViewHome;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class PresenterHome extends Presenter<IViewHome> implements IPresenterHome {
    private Subscription mSubscription;

    public PresenterHome(IViewHome pView) {
        super(pView);
    }

    @Override
    public void dispose() {
        super.dispose();
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }

    @Override
    public void me(final String pAccessToken) {
        this.mSubscription = ApiFactory.getApiInstance().meRx(pAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Me>() {
                    @Override
                    public void onStart() {
                        IViewHome view = PresenterHome.this.getView();
                        if (view != null) {
                            view.showNetworkProgress();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        IViewHome view = PresenterHome.this.getView();
                        if (view != null) {
                            view.hideNetworkProgress();
                        }
                    }

                    @Override
                    public void onError(Throwable pError) {
                        IViewHome view = PresenterHome.this.getView();
                        if (view != null) {
                            view.hideNetworkProgress();
                            view.showRetryableError(
                                    R.string.error__retryable_network_error,
                                    false,
                                    new UtilsDialog.IRetryAction() {
                                        @Override
                                        public void retry() {
                                            PresenterHome.this.me(pAccessToken);
                                        }
                                    });
                        }

                        pError.printStackTrace();
                    }

                    @Override
                    public void onNext(Me pMe) {
                        IViewHome view = PresenterHome.this.getView();
                        if (view != null) {
                            view.dispatchMe(pMe);
                        }
                    }
                });
    }
}
