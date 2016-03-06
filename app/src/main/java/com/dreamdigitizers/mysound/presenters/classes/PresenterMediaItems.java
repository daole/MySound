package com.dreamdigitizers.mysound.presenters.classes;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterMediaItems;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewMediaItems;

import java.util.List;

abstract class PresenterMediaItems <V extends IViewMediaItems> extends PresenterBase<V> implements IPresenterMediaItems {
    protected MediaBrowserConnectionCallback mMediaBrowserConnectionCallback;
    protected MediaBrowserSubscriptionCallback mMediaBrowserSubscriptionCallback;
    protected MediaBrowserCompat mMediaBrowser;

    public PresenterMediaItems(V pView) {
        super(pView);
        this.mMediaBrowserConnectionCallback = new MediaBrowserConnectionCallback();
        this.mMediaBrowserSubscriptionCallback = new MediaBrowserSubscriptionCallback();
        this.mMediaBrowser = new MediaBrowserCompat(this.getView().getViewContext(), new ComponentName(this.getView().getViewContext(), ServicePlayback.class), this.mMediaBrowserConnectionCallback, null);
    }

    @Override
    public void connect() {
        V view = this.getView();
        if (view != null) {
            if (!this.mMediaBrowser.isConnected()) {
                view.showNetworkProgress();
                this.load(this.getMediaId());
                Intent intent = new Intent(ServicePlayback.ACTION__MEDIA_COMMAND);
                intent.setPackage(view.getViewContext().getPackageName());
                view.getViewContext().startService(intent);
                this.mMediaBrowser.connect();
            }
        }
    }

    @Override
    public void disconnect() {
        if (this.mMediaBrowser.isConnected()) {
            String mediaIdRefresh = this.getMediaIdRefresh();
            if (mediaIdRefresh != null) {
                this.mMediaBrowser.unsubscribe(mediaIdRefresh);
            }
            this.mMediaBrowser.unsubscribe(this.getMediaId());
            this.mMediaBrowser.unsubscribe(this.getMediaIdMore());
            this.mMediaBrowser.disconnect();
        }
    }

    @Override
    public void refresh() {
        V view = this.getView();
        if (view != null) {
            view.showRefreshProgress();
            this.load(this.getMediaIdRefresh());
        }
    }

    @Override
    public void loadMore() {
        V view = this.getView();
        if (view != null) {
            view.showLoadMoreProgress();
            this.load(this.getMediaIdMore());
        }
    }

    protected void load(String pMediaId) {
        this.mMediaBrowser.unsubscribe(pMediaId);
        this.mMediaBrowser.subscribe(pMediaId, this.mMediaBrowserSubscriptionCallback);
    }

    protected void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
        V view = this.getView();
        if (view != null) {
            if (pChildren.size() <= 0) {
                view.showMessage(R.string.message__no_data_to_load, R.string.blank, null);
            }
            if (UtilsString.equals(pParentId, this.getMediaIdMore())) {
                view.addMediaItems(pChildren, false);
            } else {
                view.addMediaItems(pChildren, true);
            }

            if (UtilsString.equals(pParentId, this.getMediaId())) {
                view.hideNetworkProgress();
            }
            if (UtilsString.equals(pParentId, this.getMediaIdRefresh())) {
                view.hideRefreshProgress();
            }
            if (UtilsString.equals(pParentId, this.getMediaIdMore())) {
                view.hideLoadMoreProgress();
            }
        }
    }

    protected void onError(String pParentId) {
        V view = this.getView();
        if (view != null) {
            view.showError(R.string.error__unknown);
        }
    }

    protected void onConnected() {
    }

    protected abstract String getMediaId();
    protected abstract String getMediaIdRefresh();
    protected abstract String getMediaIdMore();

    private class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
        @Override
        public void onConnected() {
            PresenterMediaItems.this.onConnected();
        }
    }

    private class MediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
            PresenterMediaItems.this.onChildrenLoaded(pParentId, pChildren);
        }

        @Override
        public void onError(String pParentId) {
            PresenterMediaItems.this.onError(pParentId);
        }
    }
}
