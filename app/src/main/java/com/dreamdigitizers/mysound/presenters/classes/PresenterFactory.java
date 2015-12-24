package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenter;
import com.dreamdigitizers.androidbaselibrary.views.interfaces.IView;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterLogin;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSplash;
import com.dreamdigitizers.mysound.views.interfaces.IViewLogin;
import com.dreamdigitizers.mysound.views.interfaces.IViewSplash;

public class PresenterFactory {
    private static final String ERROR_MESSAGE__PRESENTER_NOT_FOUND = "There is no such Presenter class.";

    public static IPresenter createPresenter(Class pPresenterClass, IView pView) {
        if(pPresenterClass.isAssignableFrom(IPresenterSplash.class)) {
            return new PresenterSplash((IViewSplash)pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterLogin.class)) {
            return new PresenterLogin((IViewLogin)pView);
        }

        throw new RuntimeException(PresenterFactory.ERROR_MESSAGE__PRESENTER_NOT_FOUND);
    }
}
