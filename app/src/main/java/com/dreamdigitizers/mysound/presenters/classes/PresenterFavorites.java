package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewFavorites;

class PresenterFavorites extends PresenterTracks<IViewFavorites> implements IPresenterFavorites {
    public PresenterFavorites(IViewFavorites pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return ServicePlayback.MEDIA_ID__FAVORITES;
    }

    @Override
    protected String getMediaIdRefresh() {
        return null;
    }

    @Override
    protected String getMediaIdMore() {
        return ServicePlayback.MEDIA_ID__FAVORITES_MORE;
    }
}
