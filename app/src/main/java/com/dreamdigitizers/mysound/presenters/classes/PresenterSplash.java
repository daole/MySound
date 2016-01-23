package com.dreamdigitizers.mysound.presenters.classes;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSplash;
import com.dreamdigitizers.mysound.views.interfaces.IViewSplash;

class PresenterSplash extends PresenterBase<IViewSplash> implements IPresenterSplash {
    private SharedPreferences mSharedPreferences;

    public PresenterSplash(IViewSplash pView) {
        super(pView);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getView().getViewContext());
    }

    @Override
    public String getAccessToken() {
        String accessToken = this.mSharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY__ACCESS_TOKEN, null);
        return accessToken;
    }
}
