package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSplash;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityInitialization;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityMain;
import com.dreamdigitizers.mysound.views.interfaces.IViewSplash;

public class ScreenSplash extends ScreenBase<IPresenterSplash> implements IViewSplash {
    private Handler mHandler;
    private Runnable mChangeScreenRunnable;

    public ScreenSplash() {
        this.mHandler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mChangeScreenRunnable != null) {
            this.mHandler.removeCallbacks(this.mChangeScreenRunnable);
        }
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
    public int getScreenId() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String accessToken = this.mPresenter.getAccessToken();
        this.mChangeScreenRunnable = new Runnable() {
            @Override
            public void run() {
                if (UtilsString.isEmpty(accessToken)) {
                    ScreenSplash.this.goToInitializationActivity();
                } else {
                    Share.setAccessToken(accessToken);
                    ScreenSplash.this.goToMainActivity();
                }
            }
        };
        this.mHandler.postDelayed(this.mChangeScreenRunnable, Constants.SPLASH_SCREEN_TIME);
    }

    private void goToMainActivity() {
        this.changeActivityAndFinish(ActivityMain.class);
    }

    private void goToInitializationActivity() {
        this.changeActivityAndFinish(ActivityInitialization.class);
    }
}
