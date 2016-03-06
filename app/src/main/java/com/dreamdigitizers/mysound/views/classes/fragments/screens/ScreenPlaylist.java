package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlaylist;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlaylist;

public class ScreenPlaylist extends ScreenTracks<IPresenterPlaylist> implements IViewPlaylist {
    private MediaBrowserCompat.MediaItem mMediaItem;
    private Playlist mPlaylist;

    @Override
    protected void handleArguments(Bundle pArguments) {
        this.mMediaItem = pArguments.getParcelable(Constants.BUNDLE_KEY__MEDIA_ITEM);
        this.retrievePlaylist();
    }

    @Override
    protected void recoverInstanceState(Bundle pSavedInstanceState) {
        this.mMediaItem = pSavedInstanceState.getParcelable(Constants.BUNDLE_KEY__MEDIA_ITEM);
        this.retrievePlaylist();
    }

    @Override
    public void onSaveInstanceState(Bundle pOutState) {
        super.onSaveInstanceState(pOutState);
        pOutState.putParcelable(Constants.BUNDLE_KEY__MEDIA_ITEM, this.mMediaItem);
    }

    @Override
    protected String getTitleString() {
        return this.mPlaylist.getTitle();
    }

    @Override
    protected IPresenterPlaylist createPresenter() {
        return (IPresenterPlaylist) PresenterFactory.createPresenter(IPresenterPlaylist.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    @Override
    public int getScreenId() {
        return R.id.drawer_item__playlists;
    }

    @Override
    public int getPlaylistId() {
        return this.mPlaylist.getId();
    }

    private void retrievePlaylist() {
        Bundle bundle =  this.mMediaItem.getDescription().getExtras();
        this.mPlaylist = (Playlist) bundle.getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST);
    }
}
