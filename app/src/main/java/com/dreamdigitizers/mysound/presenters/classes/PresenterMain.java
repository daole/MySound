package com.dreamdigitizers.mysound.presenters.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterMain;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewMain;

class PresenterMain extends PresenterBase<IViewMain> implements IPresenterMain {
    private SharedPreferences mSharedPreferences;

    public PresenterMain(IViewMain pView) {
        super(pView);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getView().getViewContext());
    }

    @Override
    public void stopMediaPlayer() {
        IViewMain view = this.getView();
        if (view != null) {
            Intent intent = new Intent(ServicePlayback.ACTION__MEDIA_COMMAND);
            Context context = view.getViewContext();
            intent.setPackage(context.getPackageName());
            intent.putExtra(ServicePlayback.COMMAND__NAME, ServicePlayback.COMMAND__STOP);
            context.startService(intent);
        }
    }

    @Override
    public void deleteAccessToken() {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_KEY__ACCESS_TOKEN);
        editor.commit();
    }
}
