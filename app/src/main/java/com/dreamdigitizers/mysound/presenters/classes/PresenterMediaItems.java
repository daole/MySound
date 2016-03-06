package com.dreamdigitizers.mysound.presenters.classes;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterMediaItems;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewMediaItems;

import java.util.HashMap;
import java.util.List;

abstract class PresenterMediaItems <V extends IViewMediaItems> extends PresenterBase<V> implements IPresenterMediaItems {
    protected MediaBrowserConnectionCallback mMediaBrowserConnectionCallback;
    protected MediaBrowserSubscriptionCallback mMediaBrowserSubscriptionCallback;
    protected MediaBrowserCompat mMediaBrowser;

    protected MediaControllerCallback mMediaControllerCallback;
    protected MediaControllerCompat mMediaController;
    protected MediaControllerCompat.TransportControls mTransportControls;

    protected HashMap<String, HashMap<Integer, Object>> mTransactionActions;

    public PresenterMediaItems(V pView) {
        super(pView);
        this.mMediaBrowserConnectionCallback = new MediaBrowserConnectionCallback();
        this.mMediaBrowserSubscriptionCallback = new MediaBrowserSubscriptionCallback();
        this.mMediaBrowser = new MediaBrowserCompat(this.getView().getViewContext(), new ComponentName(this.getView().getViewContext(), ServicePlayback.class), this.mMediaBrowserConnectionCallback, null);
        this.mMediaControllerCallback = new MediaControllerCallback();
        this.mTransactionActions = new HashMap<>();
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
            this.mMediaController.unregisterCallback(this.mMediaControllerCallback);
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

    @Override
    public void skipToPrevious() {
        if (this.mTransportControls != null) {
            this.mTransportControls.skipToPrevious();
        }
    }

    @Override
    public void play() {
        if (this.mTransportControls != null) {
            this.mTransportControls.play();
        }
    }

    @Override
    public void pause() {
        if (this.mTransportControls != null) {
            this.mTransportControls.pause();
        }
    }

    @Override
    public void skipToNext() {
        if (this.mTransportControls != null) {
            this.mTransportControls.skipToNext();
        }
    }

    @Override
    public void seekTo(int pPosition) {
        if (this.mTransportControls != null) {
            this.mTransportControls.seekTo(pPosition);
        }
    }

    protected void load(String pMediaId) {
        this.mMediaBrowser.unsubscribe(pMediaId);
        this.mMediaBrowser.subscribe(pMediaId, this.mMediaBrowserSubscriptionCallback);
    }

    protected void onConnected() {
        V view = this.getView();
        if (view != null) {
            try {
                this.mMediaController = new MediaControllerCompat(view.getViewContext(), this.mMediaBrowser.getSessionToken());
                this.mTransportControls = this.mMediaController.getTransportControls();
                this.mMediaController.registerCallback(this.mMediaControllerCallback);

                MediaMetadataCompat mediaMetadata = this.mMediaController.getMetadata();
                if (mediaMetadata != null) {
                    view.onMetadataChanged(mediaMetadata);
                }

                PlaybackStateCompat playbackState = this.mMediaController.getPlaybackState();
                if (playbackState != null) {
                    view.onPlaybackStateChanged(playbackState);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
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

    protected void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        V view = this.getView();
        if (view != null) {
            if (pPlaybackState.getState() == PlaybackStateCompat.STATE_ERROR) {
                view.hideNetworkProgress();
                view.hideRefreshProgress();
                view.hideLoadMoreProgress();

                int errorMessageResourceId = 0;
                String errorCode = pPlaybackState.getErrorMessage().toString();
                switch (errorCode) {
                    case ServicePlayback.ERROR_CODE__MEDIA_NETWORK:
                        errorMessageResourceId = R.string.error__media_network;
                        break;
                    case ServicePlayback.ERROR_CODE__MEDIA_UNKNOWN:
                        errorMessageResourceId = R.string.error__media_unknown;
                        break;
                    case ServicePlayback.ERROR_CODE__MEDIA_SKIP:
                        errorMessageResourceId = R.string.error__media_skip;
                        break;
                    case ServicePlayback.ERROR_CODE__MEDIA_NO_MATCHED_TRACK:
                        errorMessageResourceId = R.string.error__media_no_matched_track;
                        break;
                    case ServicePlayback.ERROR_CODE__MEDIA_UNPLAYABLE:
                        errorMessageResourceId = R.string.error__media_unplayable;
                        break;
                    case ServicePlayback.ERROR_CODE__MEDIA_INVALID_INDEX:
                        errorMessageResourceId = R.string.error__media_invalid_index;
                        break;
                    default:
                        break;
                }
                if (errorMessageResourceId > 0) {
                    view.showMessage(errorMessageResourceId, R.string.blank, null);
                }
                if (Integer.parseInt(errorCode) >= 0) {
                    return;
                }
            }
            view.onPlaybackStateChanged(pPlaybackState);
        }
    }

    protected void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        V view = this.getView();
        if (view != null) {
            view.onMetadataChanged(pMediaMetadata);
        }
    }

    protected void onSessionEvent(String pEvent, Bundle pExtras) {
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

    private class MediaControllerCallback extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
            if (pPlaybackState != null) {
                PresenterMediaItems.this.onPlaybackStateChanged(pPlaybackState);
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
            if (pMediaMetadata != null) {
                PresenterMediaItems.this.onMetadataChanged(pMediaMetadata);
            }
        }

        @Override
        public void onSessionEvent(String pEvent, Bundle pExtras) {
            PresenterMediaItems.this.onSessionEvent(pEvent, pExtras);
        }
    }
}
