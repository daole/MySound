package com.dreamdigitizers.mysound.views.classes.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsBitmap;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaPlayer;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.CustomQueueItem;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.PlaybackNotificationReceiver;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;

import java.util.ArrayList;
import java.util.List;

public class ServicePlayback extends ServiceMediaPlayer implements IViewPlayback {
    private static final int ART_WIDTH = 800;
    private static final int ART_HEIGHT = 480;
    private static final int ICON_WIDTH = 128;
    private static final int ICON_HEIGHT = 128;

    public static final String MEDIA_ID__ROOT = "__ROOT__";
    public static final String MEDIA_ID__NEW = "__NEW__";
    public static final String MEDIA_ID__FAVORITES = "__FAVORITES__";
    public static final String MEDIA_ID__PLAYLISTS = "__PLAYLISTS__";
    public static final String MEDIA_ID__PLAYLIST = "__PLAYLIST__";

    private static final String URI__DRAWABLE = "android.resource://com.dreamdigitizers.mysound/drawable/";
    private static final String URI__DRAWABLE_ICON_NEW = ServicePlayback.URI__DRAWABLE + "ic__sound";
    private static final String URI__DRAWABLE_ICON_FAVORITE =  ServicePlayback.URI__DRAWABLE + "ic__favorite";
    private static final String URI__DRAWABLE_ICON_PLAYLIST =  ServicePlayback.URI__DRAWABLE + "ic__playlist";

    private IPresenterPlayback mPresenter;

    private Result mNewResult;
    private Result mFavoritesResult;
    private Result mPlaylistsResult;
    private Result mPlaylistResult;

    @Override
    public void onCreate() {
        super.onCreate();
        ApiFactory.initialize(Constants.SOUNDCLOUD__CLIENT_ID);
        this.mPresenter = (IPresenterPlayback) PresenterFactory.createPresenter(IPresenterPlayback.class, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter.dispose();
    }

    @Override
    protected void fetchArt(final CustomQueueItem pCustomQueueItem) {
        UtilsImage.loadBitmap(
                this,
                pCustomQueueItem.getMediaMetadata().getDescription().getIconUri().toString(),
                R.drawable.ic__my_music,
                new UtilsImage.IOnImageLoadedListener() {
                    @Override
                    public void onPrepareLoad(Drawable pPlaceHolderDrawable) {
                    }

                    @Override
                    public void onBitmapLoaded(Bitmap pBitmap) {
                        ServicePlayback.this.publishArt(pCustomQueueItem, pBitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable pPlaceHolderDrawable) {
                    }
                });
    }

    @Override
    protected boolean isOnlineStreaming() {
        return true;
    }

    @Override
    protected MediaPlayerNotificationReceiver createMediaPlayerNotificationReceiver() {
        return new PlaybackNotificationReceiver(this);
    }

    @Override
    public BrowserRoot onGetRoot(String pClientPackageName, int pClientUid, Bundle pRootHints) {
        return new BrowserRoot(ServicePlayback.MEDIA_ID__ROOT, null);
    }

    @Override
    public void onLoadChildren(String pParentId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        switch (pParentId) {
            case ServicePlayback.MEDIA_ID__ROOT:
                this.loadChildrenRoot(pResult);
                break;
            case ServicePlayback.MEDIA_ID__NEW:
                this.loadChildrenNew(pResult);
                break;
            case ServicePlayback.MEDIA_ID__FAVORITES:
                this.loadChildrenFavorites(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS:
                this.loadChildrenPlaylists(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLIST:
                this.loadChildrenPlaylist(pResult);
                break;
            default:
                break;
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public Object getViewSystemService(String pName) {
        return this.getSystemService(pName);
    }

    @Override
    public void showMessage(int pStringResourceId) {
        Toast.makeText(this, pStringResourceId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConfirmation(int pStringResourceId, UtilsDialog.IOnDialogButtonClickListener pListener) {
    }

    @Override
    public void showError(int pStringResourceId) {
    }

    @Override
    public void showRetryableError(int pStringResourceId, boolean pIsEndActivity, UtilsDialog.IRetryAction pRetryAction) {
    }

    @Override
    public void showNetworkProgress() {
    }

    @Override
    public void hideNetworkProgress() {
    }

    @Override
    public void onRxStart() {
    }

    @Override
    public void onRxCompleted() {
    }

    @Override
    public void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
    }

    @Override
    public void onRxNewTracksNext(List<Track> pTracks) {
        if (this.mNewResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(pTracks);
            this.mNewResult.sendResult(mediaItems);
            this.mNewResult = null;
        }
    }

    @Override
    public void onRxFavoritesNext(List<Track> pTracks) {
        if (this.mFavoritesResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(pTracks);
            this.mFavoritesResult.sendResult(mediaItems);
            this.mFavoritesResult = null;
        }
    }

    @Override
    public void onRxPlaylistsNext(List<Playlist> pTracks) {
        if (this.mPlaylistsResult != null) {
            this.mPlaylistsResult = null;
        }
    }

    @Override
    public void onRxPlaylistNext(List<Track> pTracks) {
        if (this.mPlaylistResult != null) {
            this.mPlaylistResult = null;
        }
    }

    private void loadChildrenRoot(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        mediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__NEW,
                R.string.media_description_title__new,
                ServicePlayback.URI__DRAWABLE_ICON_NEW,
                R.string.media_description_subtitle__new));

        mediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__FAVORITES,
                R.string.media_description_title__favorite,
                ServicePlayback.URI__DRAWABLE_ICON_FAVORITE,
                R.string.media_description_subtitle__favorite));

        mediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__PLAYLIST,
                R.string.media_description_title__playlist,
                ServicePlayback.URI__DRAWABLE_ICON_PLAYLIST,
                R.string.media_description_subtitle__playlist));

        pResult.sendResult(mediaItems);
    }

    private void loadChildrenNew(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mNewResult = pResult;
        this.mNewResult.detach();
        this.mPresenter.tracks(null);
    }

    private void loadChildrenFavorites(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mFavoritesResult = pResult;
        this.mFavoritesResult.detach();
        this.mPresenter.userFavorites(Share.getMe().getId(), null);
    }

    private void loadChildrenPlaylists(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mPlaylistsResult = pResult;
        this.mPlaylistsResult.detach();
        this.mPresenter.playlists(Share.getMe().getId(), null);
    }

    private void loadChildrenPlaylist(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mPlaylistResult = pResult;
        this.mPlaylistResult.detach();
    }

    private MediaBrowserCompat.MediaItem buildChildrenRootMediaItem(String pMediaId, int pTitleStringId, String pUri, int pSubtitleStringId) {
        return new MediaBrowserCompat.MediaItem(new MediaDescriptionCompat.Builder()
                .setMediaId(pMediaId)
                .setTitle(this.getString(pTitleStringId))
                .setIconUri(Uri.parse(pUri))
                .setSubtitle(this.getString(pSubtitleStringId))
                .build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }

    private List<MediaBrowserCompat.MediaItem> buildPlaylist(List<Track> pTracks) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        List<CustomQueueItem> playingQueue = new ArrayList<>();

        for(Track track : pTracks) {
            MediaMetadataCompat mediaMetadata = SoundCloudMetadataBuilder.build(track);

            MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaMetadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
            mediaItems.add(mediaItem);

            MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaMetadata.getDescription(), track.getId());
            CustomQueueItem customQueueItem = new CustomQueueItem(queueItem, mediaMetadata, track.getStreamUrl());
            playingQueue.add(customQueueItem);
        }

        this.setPlayingQueue(playingQueue);
        return mediaItems;
    }

    private void publishArt(CustomQueueItem pCustomQueueItem, Bitmap pBitmap) {
        //Bitmap art = UtilsBitmap.scaleBitmap(pBitmap, ServicePlayback.ART_WIDTH, ServicePlayback.ART_HEIGHT);
        Bitmap icon = UtilsBitmap.scaleBitmap(pBitmap, ServicePlayback.ICON_WIDTH, ServicePlayback.ICON_HEIGHT);
        MediaMetadataCompat mediaMetadata = pCustomQueueItem.getMediaMetadata();
        mediaMetadata = new MediaMetadataCompat.Builder(mediaMetadata)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ART, pBitmap)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, icon)
                .build();
        pCustomQueueItem.setMediaMetadata(mediaMetadata);

        if (this.isIndexPlayable(this.getCurrentIndexOnQueue(), this.getPlayingQueue())) {
            String trackId = pCustomQueueItem.getMediaMetadata().getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            String currentTrackId = this.getPlayingQueue().get(this.getCurrentIndexOnQueue()).getMediaMetadata().getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            if (trackId.equals(currentTrackId)) {
                this.getMediaSession().setMetadata(mediaMetadata);
            }
        }
    }
}
