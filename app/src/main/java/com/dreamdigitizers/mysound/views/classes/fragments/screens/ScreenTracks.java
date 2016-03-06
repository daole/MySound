package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaybackControls;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentTracks;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

public abstract class ScreenTracks<P extends IPresenterTracks> extends ScreenMediaItems<P> implements IViewTracks, TrackAdapter.IOnItemClickListener, FragmentPlaybackControls.IPlaybackControlListener {
    @Override
    public void onStart() {
        super.onStart();
        this.mFragmentMediaItems.setOnItemClickListener(this);
        ((FragmentTracks) this.mFragmentMediaItems).setPlaybackControlListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mFragmentMediaItems.setOnItemClickListener(null);
        ((FragmentTracks) this.mFragmentMediaItems).setPlaybackControlListener(null);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        ((FragmentTracks) this.mFragmentMediaItems).onPlaybackStateChanged(pPlaybackState);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        ((FragmentTracks) this.mFragmentMediaItems).onMetadataChanged(pMediaMetadata);
    }

    @Override
    public void updateState() {
        ((FragmentTracks) this.mFragmentMediaItems).updateState();
    }

    @Override
    public void skipToPrevious() {
        this.mPresenter.skipToPrevious();
    }

    @Override
    public void play() {
        this.mPresenter.play();
    }

    @Override
    public void pause() {
        this.mPresenter.pause();
    }

    @Override
    public void skipToNext() {
        this.mPresenter.skipToNext();
    }

    @Override
    public void seekTo(int pPosition) {
        this.mPresenter.seekTo(pPosition);
    }

    @Override
    protected FragmentMediaItems createFragmentMediaItems() {
        return new FragmentTracks();
    }

    @Override
    public void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.playFromMediaId(pMediaItem);
    }

    @Override
    public void onFavoriteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.favorite(pMediaItem);
    }
}
