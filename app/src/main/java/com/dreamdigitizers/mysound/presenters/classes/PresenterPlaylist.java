package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlaylist;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlaylist;

class PresenterPlaylist extends PresenterTracks<IViewPlaylist> implements IPresenterPlaylist {
    public PresenterPlaylist(IViewPlaylist pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId(ServicePlayback.MEDIA_ID__PLAYLIST);
    }

    @Override
    protected String getMediaIdRefresh() {
        return null;
    }

    @Override
    protected String getMediaIdMore() {
        return ServicePlayback.MEDIA_ID__PLAYLIST_MORE;
    }

    private String buildMediaId(String pMediaId) {
        int playlistId = 0;
        IViewPlaylist view = this.getView();
        if (view != null) {
            playlistId = view.getPlaylistId();
        }
        return pMediaId + playlistId;
    }
}
