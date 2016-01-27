package com.dreamdigitizers.mysound.presenters.classes;

import android.content.ComponentName;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.List;

abstract class PresenterTracks<V extends IViewTracks> extends PresenterBase<V> implements IPresenterTracks {
    private MediaBrowserConnectionCallback mMediaBrowserConnectionCallback;
    private MediaBrowserSubscriptionCallback mMediaBrowserSubscriptionCallback;
    private MediaBrowserCompat mMediaBrowser;

    public PresenterTracks(V pView) {
        super(pView);
        this.mMediaBrowserConnectionCallback = new MediaBrowserConnectionCallback();
        this.mMediaBrowserSubscriptionCallback = new MediaBrowserSubscriptionCallback();
        this.mMediaBrowser = new MediaBrowserCompat(this.getView().getViewContext(), new ComponentName(this.getView().getViewContext(), ServicePlayback.class), this.mMediaBrowserConnectionCallback, null);
    }

    @Override
    public void connect() {
        V view = this.getView();
        if (view != null) {
            view.showNetworkProgress();
            this.mMediaBrowser.connect();
        }
    }

    @Override
    public void disconnect() {
        this.mMediaBrowser.disconnect();
    }

    private void onConnected() {
        String mediaId = this.getMediaId();
        this.mMediaBrowser.unsubscribe(mediaId);
        this.mMediaBrowser.subscribe(mediaId, this.mMediaBrowserSubscriptionCallback);
    }

    private void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
        V view = this.getView();
        if (view != null) {
            view.setMediaItems(pChildren);
            view.hideNetworkProgress();
        }
    }

    private void onError(String pId) {
        V view = this.getView();
        if (view != null) {
            view.showError(R.string.error__unknown_error);
        }
    }

    protected abstract String getMediaId();

    private class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
        @Override
        public void onConnected() {
            PresenterTracks.this.onConnected();
        }
    }

    private class MediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
            PresenterTracks.this.onChildrenLoaded(pParentId, pChildren);
        }

        @Override
        public void onError(String pId) {
            PresenterTracks.this.onError(pId);
        }
    }
}
