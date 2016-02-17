package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenterBase;
import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterHome;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterLogin;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSoundsSearch;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSplash;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSounds;
import com.dreamdigitizers.mysound.views.interfaces.IViewFavorites;
import com.dreamdigitizers.mysound.views.interfaces.IViewHome;
import com.dreamdigitizers.mysound.views.interfaces.IViewLogin;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewSoundsSearch;
import com.dreamdigitizers.mysound.views.interfaces.IViewSplash;
import com.dreamdigitizers.mysound.views.interfaces.IViewSounds;

public class PresenterFactory {
    private static final String ERROR_MESSAGE__PRESENTER_NOT_FOUND = "There is no such Presenter class.";

    public static IPresenterBase createPresenter(Class pPresenterClass, IViewBase pView) {
        if(pPresenterClass.isAssignableFrom(IPresenterPlayback.class)) {
            return new PresenterPlayback((IViewPlayback) pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterSplash.class)) {
            return new PresenterSplash((IViewSplash) pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterLogin.class)) {
            return new PresenterLogin((IViewLogin) pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterHome.class)) {
            return new PresenterHome((IViewHome) pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterSounds.class)) {
            return new PresenterSounds((IViewSounds) pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterSoundsSearch.class)) {
            return new PresenterSoundsSearch((IViewSoundsSearch) pView);
        }

        if(pPresenterClass.isAssignableFrom(IPresenterFavorites.class)) {
            return new PresenterFavorites((IViewFavorites) pView);
        }

        throw new RuntimeException(PresenterFactory.ERROR_MESSAGE__PRESENTER_NOT_FOUND);
    }
}
