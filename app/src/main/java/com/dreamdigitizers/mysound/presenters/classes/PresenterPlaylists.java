package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlaylists;

public class PresenterPlaylists extends PresenterMediaItems<IViewPlaylists> implements IPresenterPlaylists {
    public PresenterPlaylists(IViewPlaylists pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return ServicePlayback.MEDIA_ID__PLAYLISTS;
    }

    @Override
    protected String getMediaIdRefresh() {
        return null;
    }

    @Override
    protected String getMediaIdMore() {
        return ServicePlayback.MEDIA_ID__PLAYLISTS_MORE;
    }
}
