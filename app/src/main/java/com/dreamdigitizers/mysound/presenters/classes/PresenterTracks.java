package com.dreamdigitizers.mysound.presenters.classes;

import android.content.ComponentName;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.List;

abstract class PresenterTracks<V extends IViewTracks> extends PresenterBase<V> implements IPresenterTracks {
    private MediaBrowserConnectionCallback mMediaBrowserConnectionCallback;
    private MediaBrowserSubscriptionCallback mMediaBrowserSubscriptionCallback;
    private MediaControllerCallback mMediaControllerCallback;
    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mMediaController;
    private MediaControllerCompat.TransportControls mTransportControls;

    public PresenterTracks(V pView) {
        super(pView);
        this.mMediaBrowserConnectionCallback = new MediaBrowserConnectionCallback();
        this.mMediaBrowserSubscriptionCallback = new MediaBrowserSubscriptionCallback();
        this.mMediaControllerCallback = new MediaControllerCallback();
        this.mMediaBrowser = new MediaBrowserCompat(this.getView().getViewContext(), new ComponentName(this.getView().getViewContext(), ServicePlayback.class), this.mMediaBrowserConnectionCallback, null);
    }

    @Override
    public void connect() {
        V view = this.getView();
        if (view != null) {
            if (!this.mMediaBrowser.isConnected()) {
                view.showNetworkProgress();
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
            String mediaId = this.getMediaId();
            this.mMediaBrowser.unsubscribe(mediaId);
            this.mMediaBrowser.disconnect();
            this.mMediaController.unregisterCallback(this.mMediaControllerCallback);
        }
    }

    @Override
    public void skipToPrevious() {
        if (this.mTransportControls != null) {
            this.mTransportControls.skipToPrevious();
        }
    }

    @Override
    public void playFromMediaId(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            this.mTransportControls.playFromMediaId(pMediaItem.getMediaId(), null);
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
            String mediaId = this.getMediaIdMore();
            this.mMediaBrowser.unsubscribe(mediaId);
            this.mMediaBrowser.subscribe(mediaId, this.mMediaBrowserSubscriptionCallback);
        }
    }

    @Override
    public void favorite(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__FAVORITE, pMediaItem.getDescription().getExtras());
        }
    }

    protected void load(String pMediaId) {
        this.mMediaBrowser.unsubscribe(pMediaId);
        this.mMediaBrowser.subscribe(pMediaId, this.mMediaBrowserSubscriptionCallback);
    }

    private void onConnected() {
        this.load(this.getMediaId());

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

    private void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
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

    private void onError(String pParentId) {
        V view = this.getView();
        if (view != null) {
            view.showError(R.string.error__unknown);
        }
    }

    private void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
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

    private void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        V view = this.getView();
        if (view != null) {
            view.onMetadataChanged(pMediaMetadata);
        }
    }

    protected abstract String getMediaId();
    protected abstract String getMediaIdRefresh();
    protected abstract String getMediaIdMore();

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
        public void onError(String pParentId) {
            PresenterTracks.this.onError(pParentId);
        }
    }

    private class MediaControllerCallback extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
            if (pPlaybackState != null) {
                PresenterTracks.this.onPlaybackStateChanged(pPlaybackState);
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
            if (pMediaMetadata != null) {
                PresenterTracks.this.onMetadataChanged(pMediaMetadata);
            }
        }
    }
}
