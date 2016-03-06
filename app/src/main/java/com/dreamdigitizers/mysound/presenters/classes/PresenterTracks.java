package com.dreamdigitizers.mysound.presenters.classes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.HashMap;

abstract class PresenterTracks<V extends IViewTracks> extends PresenterMediaItems<V> implements IPresenterTracks {
    public PresenterTracks(V pView) {
        super(pView);
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__FAVORITE, new HashMap<Integer, Object>());
    }

    @Override
    protected void onSessionEvent(String pEvent, Bundle pExtras) {
        Uri uri = Uri.parse(pEvent);
        String action = uri.getQueryParameter("action");
        switch (action) {
            case ServicePlayback.CUSTOM_ACTION__FAVORITE:
                this.handleFavoriteEvent(uri);
                break;
            default:
                break;
        }
    }

    @Override
    public void playFromMediaId(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            this.mTransportControls.playFromMediaId(pMediaItem.getMediaId(), null);
        }
    }

    @Override
    public void favorite(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            Bundle bundle =  pMediaItem.getDescription().getExtras();
            Track track = (Track) bundle.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE).put(track.getId(), track);
            this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__FAVORITE, bundle);
        }
    }

    private void handleFavoriteEvent(Uri pUri) {
        int trackId = Integer.parseInt(pUri.getQueryParameter("trackId"));
        boolean userFavorite = Boolean.parseBoolean(pUri.getQueryParameter("userFavorite"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE);
        Track track = (Track) transactions.get(trackId);
        track.setUserFavorite(userFavorite);
        transactions.remove(trackId);
        V view = this.getView();
        if (view != null) {
            view.updateState();
        }
    }
}
