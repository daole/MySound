package com.dreamdigitizers.mysound.views.classes.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsBitmap;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.CustomQueueItem;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlists;
import com.dreamdigitizers.androidsoundcloudapi.models.Tracks;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.androidsoundcloudapi.models.v2.Charts;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.PlaybackNotificationReceiver;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewRx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServicePlayback extends ServiceMediaBrowser {
    public static final String ERROR_CODE__MEDIA_NETWORK = "1";

    public static final String CUSTOM_ACTION__FAVORITE = "com.dreamdigitizers.mysound.views.classes.services.ServicePlayback.FAVORITE";
    public static final String CUSTOM_ACTION__DELETE_PLAYLIST = "com.dreamdigitizers.mysound.views.classes.services.ServicePlayback.DELETE_PLAYLIST";
    public static final String CUSTOM_ACTION__ADD_TO_PLAYLIST = "com.dreamdigitizers.mysound.views.classes.services.ServicePlayback.ADD_TO_PLAYLIST";
    public static final String CUSTOM_ACTION__REMOVE_FROM_PLAYLIST = "com.dreamdigitizers.mysound.views.classes.services.ServicePlayback.REMOVE_FROM_PLAYLIST";
    public static final String CUSTOM_ACTION__CREATE_PLAYLIST = "com.dreamdigitizers.mysound.views.classes.services.ServicePlayback.CREATE_PLAYLIST";

    private static final String EVENT_URI__ROOT = "media://serviceplayback?action=%s";
    private static final String EVENT_URI__FAVORITE = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&userFavorite=%s";
    private static final String EVENT_URI__DELETE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&playlistId=%s";
    private static final String EVENT_URI__ADD_TO_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__REMOVE_FROM_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__CREATE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";

    //private static final int ART_WIDTH = 800;
    //private static final int ART_HEIGHT = 480;
    private static final int ICON_WIDTH = 128;
    private static final int ICON_HEIGHT = 128;

    private static final int ACTIVE_MODE__CHARTS = 1;
    private static final int ACTIVE_MODE__SOUNDS = 2;
    private static final int ACTIVE_MODE__SOUNDS_SEARCH = 3;
    private static final int ACTIVE_MODE__FAVORITES = 4;
    private static final int ACTIVE_MODE__PLAYLISTS = 5;
    private static final int ACTIVE_MODE__PLAYLIST = 6;

    public static final String MEDIA_ID__ROOT = "__ROOT__";
    public static final String MEDIA_ID__CHARTS = "__CHARTS__";
    public static final String MEDIA_ID__CHARTS_MORE = "__CHARTS_MORE__";
    public static final String MEDIA_ID__SOUNDS = "__SOUNDS__";
    public static final String MEDIA_ID__SOUNDS_REFRESH = "__SOUNDS_REFRESH__";
    public static final String MEDIA_ID__SOUNDS_MORE = "__SOUNDS_MORE__";
    public static final String MEDIA_ID__SOUNDS_SEARCH = "__SOUNDS_SEARCH__";
    public static final String MEDIA_ID__SOUNDS_SEARCH_MORE = "__SOUNDS_SEARCH_MORE__";
    public static final String MEDIA_ID__FAVORITES = "__FAVORITES__";
    public static final String MEDIA_ID__FAVORITES_MORE = "__FAVORITES_MORE__";
    public static final String MEDIA_ID__PLAYLISTS_ALL = "__PLAYLISTS_ALL__";
    public static final String MEDIA_ID__PLAYLISTS = "__PLAYLISTS__";
    public static final String MEDIA_ID__PLAYLISTS_MORE = "__PLAYLISTS_MORE__";
    public static final String MEDIA_ID__PLAYLIST = "__PLAYLIST__";
    public static final String MEDIA_ID__PLAYLIST_MORE = "__PLAYLIST_MORE__";

    private static final String URI__DRAWABLE = "android.resource://com.dreamdigitizers.mysound/drawable/";
    private static final String URI__DRAWABLE_ICON_NEW = ServicePlayback.URI__DRAWABLE + "ic__sound";
    private static final String URI__DRAWABLE_ICON_FAVORITE =  ServicePlayback.URI__DRAWABLE + "ic__favorite";
    private static final String URI__DRAWABLE_ICON_PLAYLIST =  ServicePlayback.URI__DRAWABLE + "ic__playlist";

    private ViewPlayback mView;
    private IPresenterPlayback mPresenter;

    private Result mChartsResult;
    private Result mSoundsResult;
    private Result mSoundsSearchResult;
    private Result mFavoritesResult;
    private Result mPlaylistsResult;
    private Result mPlaylistResult;

    private List<CustomQueueItem> mActiveQueue;
    private List<CustomQueueItem> mChartsQueue;
    private List<CustomQueueItem> mSoundsQueue;
    private List<CustomQueueItem> mSoundsSearchQueue;
    private List<CustomQueueItem> mFavoritesQueue;
    private List<Playlist> mPlaylists;
    private HashMap<Integer, List<CustomQueueItem>> mPlaylistQueues;
    //private List<CustomQueueItem> mPlaylistQueue;

    private List<MediaBrowserCompat.MediaItem> mChartsMediaItems;
    private List<MediaBrowserCompat.MediaItem> mSoundsMediaItems;
    private List<MediaBrowserCompat.MediaItem> mSoundsSearchMediaItems;
    private List<MediaBrowserCompat.MediaItem> mFavoritesMediaItems;
    private List<MediaBrowserCompat.MediaItem> mPlaylistsMediaItems;
    private HashMap<Integer, List<MediaBrowserCompat.MediaItem>> mPlaylistMediaItems;
    //private List<MediaBrowserCompat.MediaItem> mPlaylistMediaItems;

    private int mChartsOffset;
    private int mSoundsOffset;
    private int mSoundsSearchOffset;
    private String mFavoritesOffset;
    private int mPlaylistsOffset;
    //private int mPlaylistOffset;

    private boolean mIsChartsMore;
    private boolean mIsSoundsMore;
    private boolean mIsSoundsSearchMore;
    private boolean mIsFavoritesMore;
    private boolean mIsPlaylistsMore;
    //private boolean mIsPlaylistMore;

    private int mActiveMode;
    private int mPlaylistId;
    private String mQuery;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mChartsQueue = new ArrayList<>();
        this.mSoundsQueue = new ArrayList<>();
        this.mSoundsSearchQueue = new ArrayList<>();
        this.mFavoritesQueue = new ArrayList<>();
        this.mPlaylists = new ArrayList<>();
        this.mPlaylistQueues = new HashMap<>();

        this.mChartsMediaItems = new ArrayList<>();
        this.mSoundsMediaItems = new ArrayList<>();
        this.mSoundsSearchMediaItems = new ArrayList<>();
        this.mFavoritesMediaItems = new ArrayList<>();
        this.mPlaylistsMediaItems = new ArrayList<>();
        this.mPlaylistMediaItems = new HashMap<>();

        this.mIsChartsMore = true;
        this.mIsSoundsMore = true;
        this.mIsSoundsSearchMore = true;
        this.mIsFavoritesMore = true;
        this.mIsPlaylistsMore = true;
        //this.mIsPlaylistMore = true;

        this.mView = new ViewPlayback();
        this.mPresenter = (IPresenterPlayback) PresenterFactory.createPresenter(IPresenterPlayback.class, this.mView);

        String accessToken = Share.getAccessToken();
        if (UtilsString.isEmpty(accessToken)) {
            accessToken = this.mPresenter.getAccessToken();
            Share.setAccessToken(accessToken);
        }
        ApiFactory.initialize(Constants.SOUNDCLOUD__CLIENT_ID, accessToken);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter.dispose();
    }

    @Override
    protected void updateMetadata() {
        if (!this.isIndexPlayable(this.getCurrentIndexOnQueue(), this.getPlayingQueue())) {
            this.updatePlaybackState(ServiceMediaBrowser.ERROR_CODE__MEDIA_INVALID_INDEX);
            return;
        }

        CustomQueueItem customQueueItem = this.getPlayingQueue().get(this.getCurrentIndexOnQueue());
        MediaMetadataCompat mediaMetadata = customQueueItem.getMediaMetadata();
        if (Build.VERSION.SDK_INT >= 21) {
            Track track = (Track) mediaMetadata.getBundle().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            Share.setCurrentTrack(track);
        }
        this.getMediaSession().setMetadata(mediaMetadata);

        if (mediaMetadata.getDescription().getIconBitmap() == null && mediaMetadata.getDescription().getIconUri() != null) {
            this.fetchArt(customQueueItem);
        }
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
    protected void processCustomActionRequest(String pAction, Bundle pExtras) {
        switch (pAction) {
            case ServicePlayback.CUSTOM_ACTION__FAVORITE:
                this.processFavoriteRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST:
                this.processDeletePlaylistRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST:
                this.processAddToPlaylistRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST:
                this.processRemoveFromPlaylistRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST:
                this.processCreatePlaylistRequest(pExtras);
                break;
            default:
                break;
        }
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

        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__PLAYLIST)) {
            String playlistId = pParentId.substring(ServicePlayback.MEDIA_ID__PLAYLIST.length());
            this.loadChildrenPlaylist(playlistId, pResult);
            return;
        }

        switch (pParentId) {
            case ServicePlayback.MEDIA_ID__ROOT:
                this.loadChildrenRoot(pResult);
                break;
            case ServicePlayback.MEDIA_ID__CHARTS:
                this.loadChildrenCharts(pResult);
                break;
            case ServicePlayback.MEDIA_ID__CHARTS_MORE:
                this.loadChildrenChartsMore(pResult);
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
            case ServicePlayback.MEDIA_ID__FAVORITES_MORE:
                this.loadChildrenFavoritesMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS_ALL:
                this.loadChildrenPlaylistsAll(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS:
                this.loadChildrenPlaylists(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS_MORE:
                this.loadChildrenPlaylistsMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLIST_MORE:
                this.loadChildrenPlaylistMore(pResult);
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

    private void loadChildrenCharts(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        /*this.mActiveMode = ServicePlayback.ACTIVE_MODE__CHARTS;
        if (this.mChartsMediaItems.size() > 0) {
            pResult.sendResult(this.mChartsMediaItems);
            this.mActiveQueue = this.mChartsQueue;
            return;
        }*/
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__CHARTS;
        this.mChartsOffset = 0;
        this.mIsChartsMore = true;
        this.mChartsQueue = new ArrayList<>();
        this.mChartsMediaItems.clear();
        this.loadChildrenChartsMore(pResult);
    }

    private void loadChildrenChartsMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mChartsResult = pResult;
        this.mChartsResult.detach();
        this.mPresenter.charts(null, Constants.SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING, Constants.SOUNDCLOUD_PARAMETER__LIMIT, this.mChartsOffset);
    }

    private void loadChildrenSounds(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        /*this.mActiveMode = ServicePlayback.ACTIVE_MODE__SOUNDS;
        if (this.mSoundsMediaItems.size() > 0) {
            pResult.sendResult(this.mSoundsMediaItems);
            this.mActiveQueue = this.mSoundsQueue;
            return;
        }*/
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__SOUNDS;
        this.mSoundsOffset = 0;
        this.mIsSoundsMore = true;
        this.mSoundsQueue = new ArrayList<>();
        this.mSoundsMediaItems.clear();
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
    }

    private void loadChildrenSoundsSearch(String pQuery, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        /*this.mActiveMode = ServicePlayback.ACTIVE_MODE__SOUNDS_SEARCH;
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
        }*/
        this.mQuery = pQuery;
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__SOUNDS_SEARCH;
        this.mSoundsSearchOffset = 0;
        this.mIsSoundsSearchMore = true;
        this.mSoundsSearchQueue = new ArrayList<>();
        this.mSoundsSearchMediaItems.clear();
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
    }

    private void loadChildrenFavorites(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        /*this.mActiveMode = ServicePlayback.ACTIVE_MODE__FAVORITES;
        if (this.mFavoritesMediaItems.size() > 0) {
            pResult.sendResult(this.mFavoritesMediaItems);
            this.mActiveQueue = this.mFavoritesQueue;
            return;
        }*/
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__FAVORITES;
        this.mFavoritesOffset = null;
        this.mIsFavoritesMore = true;
        this.mFavoritesQueue = new ArrayList<>();
        this.mFavoritesMediaItems.clear();
        this.loadChildrenFavoritesMore(pResult);
    }

    private void loadChildrenFavoritesMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mFavoritesResult = pResult;
        this.mFavoritesResult.detach();
        this.mPresenter.userFavorites(null, Constants.SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING, Constants.SOUNDCLOUD_PARAMETER__LIMIT, this.mFavoritesOffset);
    }

    private void loadChildrenPlaylistsAll(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        /*if (this.mPlaylistsMediaItems.size() > 0 && !this.mIsPlaylistsMore) {
            pResult.sendResult(this.mPlaylistsMediaItems);
            return;
        }*/
        this.mPlaylistsResult = pResult;
        this.mPlaylistsResult.detach();
        this.mPresenter.userPlaylists(null);
    }

    private void loadChildrenPlaylists(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        /*this.mActiveMode = ServicePlayback.ACTIVE_MODE__PLAYLISTS;
        if (this.mPlaylistsMediaItems.size() > 0) {
            pResult.sendResult(this.mPlaylistsMediaItems);
            return;
        }*/
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__PLAYLISTS;
        this.mPlaylistsOffset = 0;
        this.mIsPlaylistsMore = true;
        this.mPlaylists = new ArrayList<>();
        this.mPlaylistQueues = new HashMap<>();
        this.mPlaylistsMediaItems.clear();
        this.mPlaylistMediaItems.clear();
        this.loadChildrenPlaylistsMore(pResult);
    }

    private void loadChildrenPlaylistsMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mPlaylistsResult = pResult;
        this.mPlaylistsResult.detach();
        this.mPresenter.userPlaylists(null, Constants.SOUNDCLOUD_PARAMETER__LINKED_PARTITIONING, Constants.SOUNDCLOUD_PARAMETER__LIMIT, this.mPlaylistsOffset);
    }

    private void loadChildrenPlaylist(String pPlaylistId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        try {
            int playlistId = Integer.parseInt(pPlaylistId);
            if (this.mPlaylistMediaItems.containsKey(playlistId)) {
                pResult.sendResult(this.mPlaylistMediaItems.get(playlistId));
                this.mActiveQueue = this.mPlaylistQueues.get(pPlaylistId);
                return;
            }

            this.loadChildrenPlaylistMore(playlistId, pResult);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void loadChildrenPlaylistMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.loadChildrenPlaylistMore(this.mPlaylistId, pResult);
    }

    private void loadChildrenPlaylistMore(int pPlaylistId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        for (Playlist playlist : this.mPlaylists) {
            if (playlist.getId() == pPlaylistId) {
                List<Track> tracks = playlist.getTracks();
                List<CustomQueueItem> playlistQueue = new ArrayList<>();
                List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, playlistQueue, false);
                this.mPlaylistQueues.put(pPlaylistId, playlistQueue);
                this.mPlaylistMediaItems.put(pPlaylistId, mediaItems);
                pResult.sendResult(mediaItems);
                this.mActiveQueue = playlistQueue;
                return;
            }
        }
        pResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public void onRxChartsNext(Charts pCharts) {
        if (this.mChartsResult != null) {
            String nextHref = pCharts.getNextHref();
            if (UtilsString.isEmpty(nextHref)) {
                if (!this.mIsChartsMore) {
                    this.mChartsResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    this.mChartsResult = null;
                    return;
                }
                this.mIsChartsMore = false;
            } else {
                this.mChartsOffset += Constants.SOUNDCLOUD_PARAMETER__LIMIT;
                this.mIsChartsMore = true;
            }
            List<Track> tracks = new ArrayList<>();
            for (Charts.Collection collection : pCharts.getCollection()) {
                tracks.add(collection.getTrack());
            }
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, this.mChartsQueue, false);
            this.mChartsMediaItems.addAll(mediaItems);
            this.mChartsResult.sendResult(mediaItems);
            this.mChartsResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__CHARTS) {
                this.mActiveQueue = this.mChartsQueue;
            }
        }
    }

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

    public void onRxSoundsNext(Tracks pTracks) {
        if (this.mSoundsResult != null) {
            String nextHref = pTracks.getNextHref();
            if (UtilsString.isEmpty(nextHref)) {
                if (!this.mIsSoundsMore) {
                    this.mSoundsResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    this.mSoundsResult = null;
                    return;
                }
                this.mIsSoundsMore = false;
            } else {
                this.mSoundsOffset += Constants.SOUNDCLOUD_PARAMETER__LIMIT;
                this.mIsSoundsMore = true;
            }
            List<Track> tracks = pTracks.getCollection();
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, this.mSoundsQueue, false);
            this.mSoundsMediaItems.addAll(mediaItems);
            this.mSoundsResult.sendResult(mediaItems);
            this.mSoundsResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SOUNDS) {
                this.mActiveQueue = this.mSoundsQueue;
            }
        }
    }

    public void onRxSoundsSearchNext(Tracks pTracks) {
        if (this.mSoundsSearchResult != null) {
            String nextHref = pTracks.getNextHref();
            if (UtilsString.isEmpty(nextHref)) {
                if (!this.mIsSoundsSearchMore) {
                    this.mSoundsSearchResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    this.mSoundsSearchResult = null;
                    return;
                }
                this.mIsSoundsSearchMore = false;
            } else {
                this.mSoundsSearchOffset += Constants.SOUNDCLOUD_PARAMETER__LIMIT;
                this.mIsSoundsSearchMore = true;
            }
            List<Track> tracks = pTracks.getCollection();
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, this.mSoundsSearchQueue, false);
            this.mSoundsSearchMediaItems.addAll(mediaItems);
            this.mSoundsSearchResult.sendResult(mediaItems);
            this.mSoundsSearchResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SOUNDS_SEARCH) {
                this.mActiveQueue = this.mSoundsSearchQueue;
            }
        }
    }

    /*public void onRxFavoritesNext(List<Track> pTracks) {
        if (this.mFavoritesResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(pTracks, this.mFavoritesQueue, true);
            this.mFavoritesMediaItems.addAll(0, mediaItems);
            this.mFavoritesResult.sendResult(mediaItems);
            this.mFavoritesResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__FAVORITES) {
                this.mActiveQueue = this.mFavoritesQueue;
            }
        }
    }*/

    public void onRxFavoritesNext(Tracks pTracks) {
        if (this.mFavoritesResult != null) {
            String nextHref = pTracks.getNextHref();
            if (UtilsString.isEmpty(nextHref)) {
                if (!this.mIsFavoritesMore) {
                    this.mFavoritesResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    this.mFavoritesResult = null;
                    return;
                }
                this.mIsFavoritesMore = false;
            } else {
                Uri uri = Uri.parse(nextHref);
                this.mFavoritesOffset = uri.getQueryParameter("cursor");
                this.mIsFavoritesMore = true;
            }
            List<Track> tracks = pTracks.getCollection();
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylist(tracks, this.mFavoritesQueue, false);
            this.mFavoritesMediaItems.addAll(mediaItems);
            this.mFavoritesResult.sendResult(mediaItems);
            this.mFavoritesResult = null;
            if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__FAVORITES) {
                this.mActiveQueue = this.mFavoritesQueue;
            }
        }
    }

    public void onRxPlaylistsNext(List<Playlist> pPlaylists) {
        this.mPlaylists.clear();
        this.mPlaylists.addAll(pPlaylists);
        this.mIsPlaylistsMore = false;
        if (this.mPlaylistsResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylists(this.mPlaylists);
            this.mPlaylistsMediaItems.clear();
            this.mPlaylistsMediaItems.addAll(mediaItems);
            this.mPlaylistsResult.sendResult(mediaItems);
            this.mPlaylistsResult = null;
        }
    }

    public void onRxPlaylistsNext(Playlists pPlaylists) {
        if (this.mPlaylistsResult != null) {
            String nextHref = pPlaylists.getNextHref();
            if (UtilsString.isEmpty(nextHref)) {
                if (!this.mIsPlaylistsMore) {
                    this.mPlaylistsResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    this.mPlaylistsResult = null;
                    return;
                }
                this.mIsPlaylistsMore = false;
            } else {
                this.mPlaylistsOffset += Constants.SOUNDCLOUD_PARAMETER__LIMIT;
                this.mIsPlaylistsMore = true;
            }
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildPlaylists(pPlaylists);
            this.mPlaylists.addAll(pPlaylists.getCollection());
            this.mPlaylistsMediaItems.addAll(mediaItems);
            this.mPlaylistsResult.sendResult(mediaItems);
            this.mPlaylistsResult = null;
        }
    }

    /*public void onRxPlaylistNext(List<Track> pTracks) {
        if (this.mPlaylistResult != null) {
            this.mPlaylistResult = null;
        }
    }*/

    public void onRxFavoriteNext(Track pTrack) {
        pTrack.setUserFavorite(true);
        //this.updatePlaybackState(null);
        this.sendFavoriteActionResult(pTrack);
    }

    public void onRxUnfavoriteNext(Track pTrack) {
        pTrack.setUserFavorite(false);
        //this.updatePlaybackState(null);
        this.sendFavoriteActionResult(pTrack);
    }

    public void onRxDeletePlaylistNext(Playlist pPlaylist) {
        int playlistId = pPlaylist.getId();
        for (Playlist playlist : this.mPlaylists) {
            if (playlist.getId() == playlistId) {
                this.mPlaylists.remove(playlist);
                break;
            }
        }
        for (MediaBrowserCompat.MediaItem mediaItem : this.mPlaylistsMediaItems) {
            if (Integer.parseInt(mediaItem.getMediaId()) == playlistId) {
                this.mPlaylistsMediaItems.remove(mediaItem);
                break;
            }
        }
        this.mPlaylistMediaItems.remove(playlistId);
        this.mPlaylistQueues.remove(playlistId);
        this.sendDeletePlaylistActionResult(pPlaylist);
    }

    private void onRxAddToPlaylistNext(Track pTrack, Playlist pPlaylist) {
        this.sendAddToPlaylistActionResult(pTrack, pPlaylist);
    }

    private void onRxRemoveFromPlaylistNext(Track pTrack, Playlist pPlaylist) {
        this.sendRemoveFromPlaylistActionResult(pTrack, pPlaylist);
    }

    private void onRxCreatePlaylistNext(Track pTrack, Playlist pPlaylist) {
        this.sendCreatePlaylistActionResult(pTrack, pPlaylist);
    }

    private void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
        this.mSoundsResult = null;
        this.mSoundsSearchResult = null;
        this.mFavoritesResult = null;
        this.mPlaylistsResult = null;
        this.mPlaylistResult = null;
        this.updatePlaybackState(ServicePlayback.ERROR_CODE__MEDIA_NETWORK);
        pError.printStackTrace();
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

        if (customQueueItems.size() > 0) {
            if (pIsAddToTop) {
                pPlayingQueue.addAll(0, customQueueItems);
            } else {
                pPlayingQueue.addAll(customQueueItems);
            }
        }

        return mediaItems;
    }

    private List<MediaBrowserCompat.MediaItem> buildPlaylists(Playlists pPlaylists) {
        return this.buildPlaylists(pPlaylists.getCollection());
    }

    private List<MediaBrowserCompat.MediaItem> buildPlaylists(List<Playlist> pPlaylists) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

        for (Playlist playlist : pPlaylists) {
            MediaMetadataCompat mediaMetadata = SoundCloudMetadataBuilder.build(playlist);
            MediaDescriptionCompat mediaDescription = SoundCloudMetadataBuilder.build(mediaMetadata);

            MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescription, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
            mediaItems.add(mediaItem);
        }

        return  mediaItems;
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

    private void processFavoriteRequest(Bundle pExtras) {
        Track track = (Track) pExtras.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
        if (track.getUserFavorite()) {
            this.mPresenter.unfavorite(null, track);
        } else {
            this.mPresenter.favorite(null, track);
        }
    }

    private void processDeletePlaylistRequest(Bundle pExtras) {
        Playlist playlist = (Playlist) pExtras.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        this.mPresenter.deletePlaylist(null, playlist);
    }

    private void processAddToPlaylistRequest(Bundle pExtras) {
        Track track = (Track) pExtras.getSerializable(Constants.BUNDLE_KEY__TRACK);
        Playlist playlist = (Playlist) pExtras.getSerializable(Constants.BUNDLE_KEY__PLAYLIST);
        this.mPresenter.addToPlaylist(null, track, playlist);
    }

    private void processRemoveFromPlaylistRequest(Bundle pExtras) {
        Track track = (Track) pExtras.getSerializable(Constants.BUNDLE_KEY__TRACK);
        Playlist playlist = (Playlist) pExtras.getSerializable(Constants.BUNDLE_KEY__PLAYLIST);
        this.mPresenter.removeFromPlaylist(null, track, playlist);
    }

    private void processCreatePlaylistRequest(Bundle pExtras) {
        Track track = (Track) pExtras.getSerializable(Constants.BUNDLE_KEY__TRACK);
        String playlistTitle = pExtras.getString(Constants.BUNDLE_KEY__PLAYLIST_TITLE);
        boolean isPublic = pExtras.getBoolean(Constants.BUNDLE_KEY__IS_PUBLIC);
        this.mPresenter.createPlaylist(null, track, playlistTitle, isPublic);
    }

    private void sendFavoriteActionResult(Track pTrack) {
        String eventAction = String.format(ServicePlayback.EVENT_URI__FAVORITE, ServicePlayback.CUSTOM_ACTION__FAVORITE, pTrack.getId(), pTrack.getUserFavorite());
        this.getMediaSession().sendSessionEvent(eventAction, null);
    }

    private void sendDeletePlaylistActionResult(Playlist pPlaylist) {
        String eventAction = String.format(ServicePlayback.EVENT_URI__DELETE_PLAYLIST, ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST, pPlaylist.getId());
        this.getMediaSession().sendSessionEvent(eventAction, null);
    }

    private void sendAddToPlaylistActionResult(Track pTrack, Playlist pPlaylist) {
        String eventAction = String.format(ServicePlayback.EVENT_URI__ADD_TO_PLAYLIST, ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST, pTrack.getId(), pPlaylist.getId());
        this.getMediaSession().sendSessionEvent(eventAction, null);
    }

    private void sendRemoveFromPlaylistActionResult(Track pTrack, Playlist pPlaylist) {
        String eventAction = String.format(ServicePlayback.EVENT_URI__REMOVE_FROM_PLAYLIST, ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST, pTrack.getId(), pPlaylist.getId());
        this.getMediaSession().sendSessionEvent(eventAction, null);
    }

    private void sendCreatePlaylistActionResult(Track pTrack, Playlist pPlaylist) {
        String eventAction = String.format(ServicePlayback.EVENT_URI__CREATE_PLAYLIST, ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST, pTrack.getId(), pPlaylist.getId());
        this.getMediaSession().sendSessionEvent(eventAction, null);
    }

    private class ViewPlayback extends IViewRx.ViewRx implements IViewPlayback {
        @Override
        public Context getViewContext() {
            return ServicePlayback.this;
        }

        @Override
        public void onRxChartsNext(Charts pCharts) {
            ServicePlayback.this.onRxChartsNext(pCharts);
        }

        @Override
        public void onRxSoundsNext(List<Track> pTracks) {
            ServicePlayback.this.onRxSoundsNext(pTracks);
        }

        @Override
        public void onRxSoundsNext(Tracks pCollection) {
            ServicePlayback.this.onRxSoundsNext(pCollection);
        }

        @Override
        public void onRxSoundsSearchNext(Tracks pCollection) {
            ServicePlayback.this.onRxSoundsSearchNext(pCollection);
        }

        @Override
        public void onRxFavoritesNext(Tracks pCollection) {
            ServicePlayback.this.onRxFavoritesNext(pCollection);
        }

        @Override
        public void onRxPlaylistsNext(List<Playlist> pPlaylists) {
            ServicePlayback.this.onRxPlaylistsNext(pPlaylists);
        }

        @Override
        public void onRxPlaylistsNext(Playlists pPlaylists) {
            ServicePlayback.this.onRxPlaylistsNext(pPlaylists);
        }

        /*@Override
        public void onRxPlaylistNext(List<Track> pTracks) {
            ServicePlayback.this.onRxPlaylistNext(pTracks);
        }*/

        @Override
        public void onRxFavoriteNext(Track pTrack) {
            ServicePlayback.this.onRxFavoriteNext(pTrack);
        }

        @Override
        public void onRxUnfavoriteNext(Track pTrack) {
            ServicePlayback.this.onRxUnfavoriteNext(pTrack);
        }

        @Override
        public void onRxDeletePlaylistNext(Playlist pPlaylist) {
            ServicePlayback.this.onRxDeletePlaylistNext(pPlaylist);
        }

        @Override
        public void onRxAddToPlaylistNext(Track pTrack, Playlist pPlaylist) {
            ServicePlayback.this.onRxAddToPlaylistNext(pTrack, pPlaylist);
        }

        @Override
        public void onRxRemoveFromPlaylistNext(Track pTrack, Playlist pPlaylist) {
            ServicePlayback.this.onRxRemoveFromPlaylistNext(pTrack, pPlaylist);
        }

        @Override
        public void onRxCreatePlaylistNext(Track pTrack, Playlist pPlaylist) {
            ServicePlayback.this.onRxCreatePlaylistNext(pTrack, pPlaylist);
        }

        @Override
        public void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
            ServicePlayback.this.onRxError(pError, pRetryAction);
        }
    }
}
