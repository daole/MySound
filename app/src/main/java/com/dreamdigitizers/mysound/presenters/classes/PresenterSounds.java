package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSounds;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewSounds;

class PresenterSounds extends PresenterTracks<IViewSounds> implements IPresenterSounds {
    public PresenterSounds(IViewSounds pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return ServicePlayback.MEDIA_ID__SOUNDS;
    }
}
