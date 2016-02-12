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
                view.getViewContext().startService(new Intent(ServicePlayback.ACTION__MEDIA_COMMAND));
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
        this.refresh(this.getMediaIdRefresh());
    }

    @Override
    public void loadMore() {
        String mediaId = this.getMediaIdMore();
        this.mMediaBrowser.unsubscribe(mediaId);
        this.mMediaBrowser.subscribe(mediaId, this.mMediaBrowserSubscriptionCallback);
    }

    private void refresh(String pMediaId) {
        this.mMediaBrowser.unsubscribe(pMediaId);
        this.mMediaBrowser.subscribe(pMediaId, this.mMediaBrowserSubscriptionCallback);
    }

    private void onConnected() {
        this.refresh(this.getMediaId());

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
            if (UtilsString.equals(pParentId, this.getMediaIdMore())) {
                view.addMediaItems(pChildren, false);
            } else {
                if (UtilsString.equals(pParentId, this.getMediaId())) {
                    view.setMediaItems(pChildren);
                } else {
                    view.addMediaItems(pChildren, true);
                }
                view.hideNetworkProgress();
            }
        }
    }

    private void onError(String pId) {
        V view = this.getView();
        if (view != null) {
            view.showError(R.string.error__unknown_error);
        }
    }

    private void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        V view = this.getView();
        if (view != null) {
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
        public void onError(String pId) {
            PresenterTracks.this.onError(pId);
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
