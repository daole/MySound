package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSoundsSearch;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewSoundsSearch;

class PresenterSoundsSearch extends PresenterTracks<IViewSoundsSearch> implements IPresenterSoundsSearch {
    public PresenterSoundsSearch(IViewSoundsSearch pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId(ServicePlayback.MEDIA_ID__SOUNDS_SEARCH);
    }

    @Override
    protected String getMediaIdRefresh() {
        return this.getMediaId();
    }

    @Override
    protected String getMediaIdMore() {
        return ServicePlayback.MEDIA_ID__SOUNDS_SEARCH_MORE;
    }

    private String buildMediaId(String pMediaId) {
        String query = "";
        IViewSoundsSearch view = this.getView();
        if (view != null) {
            query = view.getQuery();
        }
        return pMediaId + query;
    }
}
