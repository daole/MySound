package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.Presenter;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterHome;
import com.dreamdigitizers.mysound.views.interfaces.IViewHome;

class PresenterHome extends Presenter<IViewHome> implements IPresenterHome {
    public PresenterHome(IViewHome pView) {
        super(pView);
    }
}
