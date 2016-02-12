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
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaPlayer;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.CustomQueueItem;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Collection;
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
    //private static final int ART_WIDTH = 800;
    //private static final int ART_HEIGHT = 480;
    private static final int ICON_WIDTH = 128;
    private static final int ICON_HEIGHT = 128;

    private static final int ACTIVE_MODE__SOUNDS = 1;
    private static final int ACTIVE_MODE__SOUNDS_SEARCH = 2;
    private static final int ACTIVE_MODE__FAVORITES = 3;
    private static final int ACTIVE_MODE__PLAYLIST = 4;

    public static final String MEDIA_ID__ROOT = "__ROOT__";
    public static final String MEDIA_ID__SOUNDS = "__SOUNDS__";
    public static final String MEDIA_ID__SOUNDS_REFRESH = "__SOUNDS_REFRESH__";
    public static final String MEDIA_ID__SOUNDS_MORE = "__SOUNDS_MORE__";
    public static final String MEDIA_ID__SOUNDS_SEARCH = "__SOUNDS_SEARCH__";
    public static final String MEDIA_ID__SOUNDS_SEARCH_MORE = "__SOUNDS_SEARCH_MORE__";
    public static final String MEDIA_ID__FAVORITES = "__FAVORITES__";
    public static final String MEDIA_ID__FAVORITES_REFRESH = "__FAVORITES_REFRESH__";
    public static final String MEDIA_ID__PLAYLISTS = "__PLAYLISTS__";
    public static final String MEDIA_ID__PLAYLISTS_REFRESH = "__PLAYLISTS_REFRESH__";
    public static final String MEDIA_ID__PLAYLIST = "__PLAYLIST__";
    public static final String MEDIA_ID__PLAYLIST_REFRESH = "__PLAYLIST_REFRESH__";

    private static final String URI__DRAWABLE = "android.resource://com.dreamdigitizers.mysound/drawable/";
    private static final String URI__DRAWABLE_ICON_NEW = ServicePlayback.URI__DRAWABLE + "ic__sound";
    private static final String URI__DRAWABLE_ICON_FAVORITE =  ServicePlayback.URI__DRAWABLE + "ic__favorite";
    private static final String URI__DRAWABLE_ICON_PLAYLIST =  ServicePlayback.URI__DRAWABLE + "ic__playlist";

    private IPresenterPlayback mPresenter;

    private Result mSoundsResult;
    private Result mSoundsSearchResult;
    private Result mFavoritesResult;
    private Result mPlaylistsResult;
    private Result mPlaylistResult;

    private List<CustomQueueItem> mActiveQueue;
    private List<CustomQueueItem> mSoundsQueue;
    private List<CustomQueueItem> mSoundsSearchQueue;
    private List<CustomQueueItem> mFavoritesQueue;
    private List<CustomQueueItem> mPlaylistQueue;

    private List<MediaBrowserCompat.MediaItem> mSoundsMediaItems;
    private List<MediaBrowserCompat.MediaItem> mSoundsSearchMediaItems;
    private List<MediaBrowserCompat.MediaItem> mFavoritesMediaItems;
    private List<MediaBrowserCompat.MediaItem> mPlayListMediaItems;

    private int mSoundsOffset;
    private int mSoundsSearchOffset;
    private int mFavoritesOffset;
    private int mPlayListOffset;

    private int mActiveMode;
    private String mQuery;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mSoundsQueue = new ArrayList<>();
        this.mSoundsSearchQueue = new ArrayList<>();
        this.mFavoritesQueue = new ArrayList<>();

        this.mSoundsMediaItems = new ArrayList<>();
        this.mSoundsSearchMediaItems = new ArrayList<>();
        this.mFavoritesMediaItems = new ArrayList<>();

        this.mPresenter = (IPresenterPlayback) PresenterFactory.createPresenter(IPresenterPlayback.class, this);

        ApiFactory.initialize(Constants.SOUNDCLOUD__CLIENT_ID, Share.getAccessToken());
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
    protected void processPlayFromMediaIdRequest(String pMediaId, Bundle pExtras) {
        this.setPlayingQueue(this.mActiveQueue);
        super.processPlayFromMediaIdRequest(pMediaId, pExtras);
    }

    @Override
    public BrowserRoot onGetRoot(String pClientPackageName, int pClientUid, Bundle pRootHints) {
        return new BrowserRoot(ServicePlayback.MEDIA_ID__ROOT, null);
    }

    @Override
    public void onLoadChildren(String pParentId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__SOUNDS_SEARCH)) {
            String query = pParentId.substring(ServicePlayback.MEDIA_ID__SOUNDS_SEARCH.length());
            this.loadChildrenSoundsSearch(query, pResult);
            return;
        }

        switch (pParentId) {
            case ServicePlayback.MEDIA_ID__ROOT:
                this.loadChildrenRoot(pResult);
                break;
            case ServicePlayback.MEDIA_ID__SOUNDS:
                this.loadChildrenSounds(pResult);
                break;
            case ServicePlayback.MEDIA_ID__SOUNDS_REFRESH:
                this.loadChildrenSoundsRefresh(pResult);
                break;
            case ServicePlayback.MEDIA_ID__SOUNDS_MORE:
                this.loadChildrenSoundsMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__SOUNDS_SEARCH_MORE:
                this.loadChildrenSoundsSearchMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__FAVORITES:
                this.loadChildrenFavorites(pResult);
                break;
            case ServicePlayback.MEDIA_ID__FAVORITES_REFRESH:
                this.loadChildrenFavoritesRefresh(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS:
                this.loadChildrenPlaylists(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS_REFRESH:
                //this.loadChildrenPlaylistsRefresh(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLIST:
                this.loadChildrenPlaylist(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLIST_REFRESH:
                this.loadChildrenPlaylistRefresh(pResult);
                break;
            default:
                break;
        }
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
    public void onRxSoundsNext(List<Track> pTracks) {
        if (this.mSoundsResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(pTracks, this.mSoundsQueue, true);
            this.mSoundsMediaItems.addAll(0, mediaItems);
            this.mSoundsResult.sendResult(mediaItems);
            this.mSoundsResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SOUNDS) {
                this.mActiveQueue = this.mSoundsQueue;
            }
        }
    }

    @Override
    public void onRxSoundsNext(Collection pCollection) {
        if (this.mSoundsResult != null) {
            List<Track> tracks = pCollection.getCollection();
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, this.mSoundsQueue, false);
            this.mSoundsMediaItems.addAll(mediaItems);
            this.mSoundsResult.sendResult(mediaItems);
            this.mSoundsResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SOUNDS) {
                this.mActiveQueue = this.mSoundsQueue;
            }
        }
    }

    @Override
    public void onRxSoundsSearchNext(Collection pCollection) {
        if (this.mSoundsSearchResult != null) {
            List<Track> tracks = pCollection.getCollection();
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, this.mSoundsSearchQueue, false);
            this.mSoundsSearchMediaItems.addAll(mediaItems);
            this.mSoundsSearchResult.sendResult(mediaItems);
            this.mSoundsSearchResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SOUNDS_SEARCH) {
                this.mActiveQueue = this.mSoundsSearchQueue;
            }
        }
    }

    @Override
    public void onRxFavoritesNext(List<Track> pTracks) {
        if (this.mFavoritesResult != null) {
            this.mFavoritesQueue = new ArrayList<>();
            this.mFavoritesMediaItems = this.buildPlaylist(pTracks, this.mFavoritesQueue, true);
            this.mFavoritesResult.sendResult(this.mFavoritesMediaItems);
            this.mFavoritesResult = null;
            this.mActiveQueue = this.mFavoritesQueue;
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
                ServicePlayback.MEDIA_ID__SOUNDS,
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

    private void loadChildrenSounds(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__SOUNDS;
        if (this.mSoundsMediaItems.size() > 0) {
            pResult.sendResult(this.mSoundsMediaItems);
            this.mActiveQueue = this.mSoundsQueue;
            return;
        }
        this.loadChildrenSoundsMore(pResult);
    }

    private void loadChildrenSoundsRefresh(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mSoundsResult = pResult;
        this.mSoundsResult.detach();
        this.mPresenter.tracks(null);
    }

    private void loadChildrenSoundsMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mSoundsResult = pResult;
        this.mSoundsResult.detach();
        this.mPresenter.tracks(null, Constants.SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING, Constants.SOUNDCLOUD_PARAMETER__LIMIT, this.mSoundsOffset);
        this.mSoundsOffset += Constants.SOUNDCLOUD_PARAMETER__LIMIT;
    }

    private void loadChildrenSoundsSearch(String pQuery, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__SOUNDS_SEARCH;
        if (UtilsString.equals(pQuery, this.mQuery)) {
            if (this.mSoundsSearchMediaItems.size() > 0) {
                pResult.sendResult(this.mSoundsSearchMediaItems);
                this.mActiveQueue = this.mSoundsSearchQueue;
                return;
            }
        } else {
            this.mQuery = pQuery;
            this.mSoundsSearchOffset = 0;
            this.mSoundsSearchQueue = new ArrayList<>();
            this.mSoundsSearchMediaItems.clear();
        }
        this.loadChildrenSoundsSearchMore(pQuery, pResult);
    }

    private void loadChildrenSoundsSearchMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.loadChildrenSoundsSearchMore(this.mQuery, pResult);
    }

    private void loadChildrenSoundsSearchMore(String pQuery, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        if (UtilsString.isEmpty(pQuery)) {
            this.loadChildrenSounds(pResult);
            return;
        }
        this.mSoundsSearchResult = pResult;
        this.mSoundsSearchResult.detach();
        this.mPresenter.tracks(null, Constants.SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING, Constants.SOUNDCLOUD_PARAMETER__LIMIT, this.mSoundsSearchOffset, pQuery);
        this.mSoundsSearchOffset += Constants.SOUNDCLOUD_PARAMETER__LIMIT;
    }

    private void loadChildrenFavorites(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        if (this.mFavoritesMediaItems != null && this.mFavoritesMediaItems.size() > 0) {
            pResult.sendResult(this.mFavoritesMediaItems);
            return;
        }

        this.loadChildrenFavoritesRefresh(pResult);
    }

    private void loadChildrenFavoritesRefresh(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
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
        if (this.mPlayListMediaItems != null && this.mPlayListMediaItems.size() > 0) {
            pResult.sendResult(this.mPlayListMediaItems);
            return;
        }

        this.loadChildrenPlaylistRefresh(pResult);
    }

    private void loadChildrenPlaylistRefresh(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
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

    private List<MediaBrowserCompat.MediaItem> buildPlaylist(List<Track> pTracks, List<CustomQueueItem> pPlayingQueue, boolean pIsAddToTop) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        List<CustomQueueItem> customQueueItems = new ArrayList<>();

        for(Track track : pTracks) {
            MediaMetadataCompat mediaMetadata = SoundCloudMetadataBuilder.build(track);
            MediaDescriptionCompat mediaDescription = SoundCloudMetadataBuilder.build(mediaMetadata);

            MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescription, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
            mediaItems.add(mediaItem);

            MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaDescription, track.getId());
            CustomQueueItem customQueueItem = new CustomQueueItem(queueItem, mediaMetadata, this.buildStreamUrl(track.getStreamUrl()));
            customQueueItems.add(customQueueItem);
        }

        if (pIsAddToTop) {
            pPlayingQueue.addAll(0, customQueueItems);
        } else {
            pPlayingQueue.addAll(customQueueItems);
        }

        return mediaItems;
    }

    private String buildStreamUrl(String pStreamUrl) {
        return pStreamUrl + "?client_id=" + Constants.SOUNDCLOUD__CLIENT_ID;
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
