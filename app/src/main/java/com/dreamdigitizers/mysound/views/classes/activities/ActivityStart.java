package com.dreamdigitizers.mysound.views.classes.activities;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.fragments.screens.ScreenSplash;

public class ActivityStart extends ActivityBase {
    @Override
    protected void setLayout() {
        this.setContentView(R.layout.activity__start);
    }

    @Override
    protected void retrieveItems() {
    }

    @Override
    protected void mapInformationToItems() {
    }

    @Override
    protected ScreenBase getStartScreen() {
        return new ScreenSplash();
    }

    @Override
    protected int getScreenContainerViewId() {
        return R.id.container;
    }
}
