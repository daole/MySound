package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaylists;
import com.dreamdigitizers.mysound.views.classes.support.PlaylistAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlaylists;

public class ScreenPlaylists extends ScreenMediaItems<IPresenterPlaylists> implements IViewPlaylists, PlaylistAdapter.IOnItemClickListener {
    @Override
    protected IPresenterPlaylists createPresenter() {
        return (IPresenterPlaylists) PresenterFactory.createPresenter(IPresenterPlaylists.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__playlists, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_playlists;
    }

    @Override
    public int getScreenId() {
        return R.id.drawer_item__playlists;
    }

    @Override
    protected FragmentMediaItems createFragmentMediaItems() {
        return new FragmentPlaylists();
    }

    @Override
    public void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.BUNDLE_KEY__MEDIA_ITEM, pMediaItem);
        ScreenBase screenBase = new ScreenPlaylist();
        screenBase.setArguments(arguments);
        this.mScreenActionsListener.onChangeScreen(screenBase);
    }

    @Override
    public void onDeleteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.deletePlaylist(pMediaItem);
    }
}
