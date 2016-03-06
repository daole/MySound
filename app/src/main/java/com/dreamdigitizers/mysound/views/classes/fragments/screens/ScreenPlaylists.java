package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaylists;
import com.dreamdigitizers.mysound.views.classes.support.PlaylistAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlaylists;

public class ScreenPlaylists extends ScreenMediaItems<IPresenterPlaylists> implements IViewPlaylists, PlaylistAdapter.IOnItemClickListener {
    @Override
    public void onStart() {
        super.onStart();
        this.mFragmentMediaItems.setOnItemClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mFragmentMediaItems.setOnItemClickListener(null);
    }

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
    }

    @Override
    public void onDeleteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
    }
}