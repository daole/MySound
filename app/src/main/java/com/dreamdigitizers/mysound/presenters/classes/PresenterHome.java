package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.Presenter;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.User;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.Share;
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

    public void retrieveUserData() {
        this.mSubscription = ApiFactory.getApiInstance().meRx(Share.bundle.getString(Constants.SHARE_KEY__ACCESS_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onStart() {
                        IViewHome view = PresenterHome.this.getView();
                        if(view != null) {
                            view.showProgress();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        IViewHome view = PresenterHome.this.getView();
                        if(view != null) {
                            view.hideProgress();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IViewHome view = PresenterHome.this.getView();
                        if(view != null) {
                            view.hideProgress();
                        }
                    }

                    @Override
                    public void onNext(User pUser) {
                        IViewHome view = PresenterHome.this.getView();
                        if(view != null) {
                            view.hideProgress();
                        }
                    }
                });
    }
}
