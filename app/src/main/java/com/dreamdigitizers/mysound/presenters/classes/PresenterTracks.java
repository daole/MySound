package com.dreamdigitizers.mysound.presenters.classes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.HashMap;
import java.util.List;

abstract class PresenterTracks<V extends IViewTracks> extends PresenterMediaItems<V> implements IPresenterTracks {
    public PresenterTracks(V pView) {
        super(pView);
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__FAVORITE, new HashMap<Integer, Object>());
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST, new HashMap<Integer, Object>());
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST, new HashMap<Integer, Object>());
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST, new HashMap<Integer, Object>());
    }

    @Override
    protected void onSessionEvent(String pEvent, Bundle pExtras) {
        Uri uri = Uri.parse(pEvent);
        String action = uri.getQueryParameter("action");
        switch (action) {
            case ServicePlayback.CUSTOM_ACTION__FAVORITE:
                this.handleFavoriteEvent(uri);
                break;
            case ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST:
                this.handleAddToPlaylistEvent(uri);
                break;
            case ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST:
                this.handleRemoveFromPlaylistEvent(uri);
                break;
            case ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST:
                this.handleCreatePlaylistEvent(uri);
            default:
                break;
        }
    }

    @Override
    protected void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
        if (UtilsString.equals(pParentId, ServicePlayback.MEDIA_ID__PLAYLISTS_ALL)) {
            V view = this.getView();
            if (view != null) {
                view.hideNetworkProgress();
                view.onAllPlaylistsLoaded(pChildren);
            }
        } else {
            super.onChildrenLoaded(pParentId, pChildren);
        }
    }

    @Override
    public void loadAllPlaylists() {
        V view = this.getView();
        if (view != null) {
            view.showNetworkProgress();
            this.load(ServicePlayback.MEDIA_ID__PLAYLISTS_ALL);
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

    @Override
    public void addToRemoveFromPlaylist(MediaBrowserCompat.MediaItem pTrack, MediaBrowserCompat.MediaItem pPlaylist, boolean pIsAdd) {
        if (this.mTransportControls != null) {
            V view = this.getView();
            if (view != null) {
                view.showNetworkProgress();
            }
            Track track = (Track) pTrack.getDescription().getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            Playlist playlist = (Playlist) pPlaylist.getDescription().getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_KEY__TRACK, track);
            bundle.putSerializable(Constants.BUNDLE_KEY__PLAYLIST, playlist);
            TrackPlaylistPair trackPlaylistPair = new TrackPlaylistPair(track, playlist);
            String customAction;
            if (pIsAdd) {
                customAction = ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST;
            } else {
                customAction = ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST;
            }
            this.mTransactionActions.get(customAction).put(playlist.getId(), trackPlaylistPair);
            this.mTransportControls.sendCustomAction(customAction, bundle);
        }
    }

    @Override
    public void createPlaylist(MediaBrowserCompat.MediaItem pTrack, String pPlaylistTitle, boolean pIsPublic) {
        int errorResourceId = this.checkPlaylistInputData(pPlaylistTitle);
        if (errorResourceId > 0) {
            V view = this.getView();
            if (view != null) {
                view.showError(errorResourceId);
            }
        } else {
            if (this.mTransportControls != null) {
                V view = this.getView();
                if (view != null) {
                    view.showNetworkProgress();
                }
                Track track = (Track) pTrack.getDescription().getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_KEY__TRACK, track);
                bundle.putString(Constants.BUNDLE_KEY__PLAYLIST_TITLE, pPlaylistTitle);
                bundle.putBoolean(Constants.BUNDLE_KEY__IS_PUBLIC, pIsPublic);
                this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST).put(track.getId(), track);
                this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST, bundle);
            }
        }
    }

    private int checkPlaylistInputData(String pPlaylistTitle) {
        if (UtilsString.isEmpty(pPlaylistTitle)) {
            return R.string.error__blank_playlist_title;
        }
        return 0;
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

    private void handleAddToPlaylistEvent(Uri pUri) {
        int playlistId = Integer.parseInt(pUri.getQueryParameter("playlistId"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST);
        TrackPlaylistPair trackPlaylistPair = (TrackPlaylistPair) transactions.get(playlistId);
        trackPlaylistPair.mPlaylist.getTracks().add(trackPlaylistPair.mTrack);
        transactions.remove(playlistId);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    private void handleRemoveFromPlaylistEvent(Uri pUri) {
        int playlistId = Integer.parseInt(pUri.getQueryParameter("playlistId"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST);
        TrackPlaylistPair trackPlaylistPair = (TrackPlaylistPair) transactions.get(playlistId);
        trackPlaylistPair.mPlaylist.getTracks().remove(trackPlaylistPair.mTrack);
        transactions.remove(playlistId);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    private void handleCreatePlaylistEvent(Uri pUri) {
        int trackId = Integer.parseInt(pUri.getQueryParameter("trackId"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST);
        transactions.remove(trackId);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onPlaylistCreated();
        }
    }

    private class TrackPlaylistPair {
        private Track mTrack;
        private Playlist mPlaylist;

        private TrackPlaylistPair(Track pTrack, Playlist pPlaylist) {
            this.mTrack = pTrack;
            this.mPlaylist = pPlaylist;
        }
    }
}
