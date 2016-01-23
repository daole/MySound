package com.dreamdigitizers.mysound.presenters.classes;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterLogin;
import com.dreamdigitizers.mysound.views.interfaces.IViewLogin;

class PresenterLogin extends PresenterBase<IViewLogin> implements IPresenterLogin {
    private SharedPreferences mSharedPreferences;

    public PresenterLogin(IViewLogin pView) {
        super(pView);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getView().getViewContext());
    }

    @Override
    public void saveAccessToken(String pAccessToken) {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_KEY__ACCESS_TOKEN, pAccessToken);
        editor.commit();
    }
}
