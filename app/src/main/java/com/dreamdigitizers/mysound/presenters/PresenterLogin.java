package com.dreamdigitizers.mysound.presenters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dreamdigitizers.androidbaselibrary.presenters.Presenter;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.views.IViewLogin;

public class PresenterLogin extends Presenter {
    private SharedPreferences mSharedPreferences;

    public PresenterLogin(IViewLogin pView) {
        super(pView);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getView().getViewContext());
    }

    public void saveAccessToken(String pAccessToken) {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_KEY__ACCESS_TOKEN, pAccessToken);
        editor.commit();
    }
}
