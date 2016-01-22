package com.dreamdigitizers.mysound.views.classes.services;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.widget.Toast;

import com.dreamdigitizers.androidbaselibrary.utils.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaPlayer;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.views.classes.services.support.PlaybackNotificationReceiver;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;

import java.util.ArrayList;
import java.util.List;

public class ServicePlayback extends ServiceMediaPlayer implements IViewPlayback {
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
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        switch (pParentId) {
            case ServicePlayback.MEDIA_ID__ROOT:
                this.loadChildrenRoot(mediaItems, pResult);
                break;
            case ServicePlayback.MEDIA_ID__NEW:
                this.loadChildrenNew(mediaItems, pResult);
                break;
            case ServicePlayback.MEDIA_ID__FAVORITES:
                this.loadChildrenFavorites(mediaItems, pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS:
                this.loadChildrenPlaylists(mediaItems, pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLIST:
                this.loadChildrenPlaylist(mediaItems, pResult);
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

    private void loadChildrenRoot(List<MediaBrowserCompat.MediaItem> pMediaItems, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        pMediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__NEW,
                R.string.media_description_title__new,
                ServicePlayback.URI__DRAWABLE_ICON_NEW,
                R.string.media_description_subtitle__new));

        pMediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__FAVORITES,
                R.string.media_description_title__favorite,
                ServicePlayback.URI__DRAWABLE_ICON_FAVORITE,
                R.string.media_description_subtitle__favorite));

        pMediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__PLAYLIST,
                R.string.media_description_title__playlist,
                ServicePlayback.URI__DRAWABLE_ICON_PLAYLIST,
                R.string.media_description_subtitle__playlist));

        pResult.sendResult(pMediaItems);
    }

    private void loadChildrenNew(List<MediaBrowserCompat.MediaItem> pMediaItems, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        pResult.detach();
        this.mPresenter.tracks();
    }

    private void loadChildrenFavorites(List<MediaBrowserCompat.MediaItem> pMediaItems, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        pResult.detach();
        this.mPresenter.userFavorites(Share.getMe().getId());
    }

    private void loadChildrenPlaylists(List<MediaBrowserCompat.MediaItem> pMediaItems, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        pResult.detach();
        this.mPresenter.playlists(Share.getMe().getId());
    }

    private void loadChildrenPlaylist(List<MediaBrowserCompat.MediaItem> pMediaItems, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        pResult.detach();
    }

    private MediaBrowserCompat.MediaItem buildChildrenRootMediaItem(String pMediaId, int pTitleStringId, String pUri, int pSubtitleStringId) {
        return new MediaBrowserCompat.MediaItem(new MediaDescriptionCompat.Builder()
                .setMediaId(pMediaId)
                .setTitle(this.getString(pTitleStringId))
                .setIconUri(Uri.parse(pUri))
                .setSubtitle(this.getString(pSubtitleStringId))
                .build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }
}
