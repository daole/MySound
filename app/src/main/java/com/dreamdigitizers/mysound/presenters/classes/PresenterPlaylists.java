package com.dreamdigitizers.mysound.presenters.classes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.mysound.views.classes.services.ServicePlayback;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlaylists;

import java.util.HashMap;

class PresenterPlaylists extends PresenterMediaItems<IViewPlaylists> implements IPresenterPlaylists {
    public PresenterPlaylists(IViewPlaylists pView) {
        super(pView);
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST, new HashMap<Integer, Object>());
    }

    @Override
    protected void onSessionEvent(String pEvent, Bundle pExtras) {
        Uri uri = Uri.parse(pEvent);
        String action = uri.getQueryParameter("action");
        switch (action) {
            case ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST:
                this.handleDeletePlaylistEvent(uri);
                break;
            default:
                break;
        }
    }

    @Override
    protected String getMediaId() {
        return ServicePlayback.MEDIA_ID__PLAYLISTS;
    }

    @Override
    protected String getMediaIdRefresh() {
        return null;
    }

    @Override
    protected String getMediaIdMore() {
        return ServicePlayback.MEDIA_ID__PLAYLISTS_MORE;
    }

    @Override
    public void deletePlaylist(final MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            IViewPlaylists view = this.getView();
            if (view != null) {
                view.showConfirmation(R.string.confirmation__delete_playlist, new UtilsDialog.IOnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Activity pActivity, String pTitle, String pMessage, boolean pIsTwoButtons, String pPositiveButtonText, String pNegativeButtonText) {
                        Bundle bundle =  pMediaItem.getDescription().getExtras();
                        Playlist playlist = (Playlist) bundle.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST);
                        PresenterPlaylists.this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST).put(playlist.getId(), pMediaItem);
                        PresenterPlaylists.this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST, bundle);
                    }

                    @Override
                    public void onNegativeButtonClick(Activity pActivity, String pTitle, String pMessage, boolean pIsTwoButtons, String pPositiveButtonText, String pNegativeButtonText) {
                    }
                });
            }
        }
    }

    private void handleDeletePlaylistEvent(Uri pUri) {
        int playlistId = Integer.parseInt(pUri.getQueryParameter("playlistId"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST);
        MediaBrowserCompat.MediaItem mediaItem = (MediaBrowserCompat.MediaItem) transactions.get(playlistId);
        transactions.remove(playlistId);
        IViewPlaylists view = this.getView();
        if (view != null) {
            view.removeMediaItem(mediaItem);
        }
    }
}
