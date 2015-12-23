package com.dreamdigitizers.mysound.views.activities;

import com.dreamdigitizers.androidbaselibrary.views.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.fragments.screens.ScreenLogin;

public class ActivityInitialization extends ActivityBase {
    @Override
    protected void setLayout() {
        this.setContentView(R.layout.activity__login);
    }

    @Override
    protected void retrieveItems() {
    }

    @Override
    protected void mapInformationToItems() {
    }

    @Override
    protected ScreenBase getStartScreen() {
        return new ScreenLogin();
    }

    @Override
    protected int getScreenContainerViewId() {
        return R.id.container;
    }
}
