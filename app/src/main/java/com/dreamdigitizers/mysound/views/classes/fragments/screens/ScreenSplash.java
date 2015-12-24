package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.androidbaselibrary.utils.UtilsString;
import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSplash;
import com.dreamdigitizers.mysound.views.interfaces.IViewSplash;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityInitialization;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityMain;

public class ScreenSplash extends ScreenBase<IPresenterSplash> implements IViewSplash {
    private Handler mHandler;

    public ScreenSplash() {
        this.mHandler = new Handler();
    }

    @Override
    protected IPresenterSplash createPresenter() {
        return (IPresenterSplash) PresenterFactory.createPresenter(IPresenterSplash.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__splash, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String accessToken = this.mPresenter.getAccessToken();
        this.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UtilsString.isEmpty(accessToken)) {
                    ScreenSplash.this.goToInitializationActivity();
                } else {
                    ScreenSplash.this.goToMainActivity();
                }
            }
        }, Constants.SPLASH_SCREEN_TIME);
    }

    private void goToMainActivity() {
        this.changeActivity(ActivityMain.class);
    }

    private void goToInitializationActivity() {
        this.changeActivity(ActivityInitialization.class);
    }

    private void changeActivity(Class pTargetActivityClass) {
        Intent intent = new Intent(this.getContext(), pTargetActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ((ActivityBase) this.getActivity()).changeActivity(intent, true);
    }
}
