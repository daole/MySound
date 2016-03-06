package com.dreamdigitizers.mysound.presenters.classes;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.HashMap;

abstract class PresenterTracks<V extends IViewTracks> extends PresenterMediaItems<V> implements IPresenterTracks {
    private HashMap<String, HashMap<Integer, Object>> mTransactionActions;

    private MediaControllerCallback mMediaControllerCallback;
    private MediaControllerCompat mMediaController;
    private MediaControllerCompat.TransportControls mTransportControls;

    public PresenterTracks(V pView) {
        super(pView);
        this.mTransactionActions = new HashMap<>();
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__FAVORITE, new HashMap<Integer, Object>());
        this.mMediaControllerCallback = new MediaControllerCallback();
    }

    @Override
    public void disconnect() {
        if (this.mMediaBrowser.isConnected()) {
            this.mMediaController.unregisterCallback(this.mMediaControllerCallback);
        }
        super.disconnect();
    }

    @Override
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
    public void favorite(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            Bundle bundle =  pMediaItem.getDescription().getExtras();
            Track track = (Track) bundle.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE).put(track.getId(), track);
            this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__FAVORITE, bundle);
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

    private void onSessionEvent(String pEvent, Bundle pExtras) {
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

        @Override
        public void onSessionEvent(String pEvent, Bundle pExtras) {
            PresenterTracks.this.onSessionEvent(pEvent, pExtras);
        }
    }
}
