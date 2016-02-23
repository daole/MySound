package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterHome;
import com.dreamdigitizers.mysound.views.interfaces.IViewHome;

public class ScreenHome extends ScreenTracks<IPresenterHome> implements IViewHome {
    @Override
    protected IPresenterHome createPresenter() {
        return (IPresenterHome) PresenterFactory.createPresenter(IPresenterHome.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__home, pContainer, false);
        return rootView;
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        super.mapInformationToScreenItems(pView);

        Me me = Share.getMe();
        if (me == null) {
            this.me();
        }
    }

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
        if (pRetryAction != null) {
            this.showRetryableError(
                    R.string.error__retryable_network,
                    false,
                    pRetryAction);
        }
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_home;
    }

    @Override
     public int getScreenId() {
        return R.id.drawer_item__home;
    }

    @Override
    public void onRxNext(Me pMe) {
        Share.setMe(pMe);
    }

    private void me() {
        this.mPresenter.me(Share.getAccessToken(),
                new UtilsDialog.IRetryAction() {
                    @Override
                    public void retry() {
                        ScreenHome.this.me();
                    }
                });
    }
}
