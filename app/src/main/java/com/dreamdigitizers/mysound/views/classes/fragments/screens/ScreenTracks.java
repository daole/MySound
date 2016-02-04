package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaybackControls;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentTracks;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.List;

public abstract class ScreenTracks<P extends IPresenterTracks> extends ScreenBase<P>
        implements IViewTracks, FragmentPlaybackControls.IPlaybackControlListener, TrackAdapter.IOnItemClickListener {
    private static final String ERROR_MESSAGE__MISSING_FRAGMENT = "Missing fragment with id \"fraTracks\" in layout.";

    protected FragmentTracks mFragmentTracks;

    @Override
    public void onStart() {
        super.onStart();
        if (this.mFragmentTracks == null) {
            throw new NullPointerException(ScreenTracks.ERROR_MESSAGE__MISSING_FRAGMENT);
        }
        this.mFragmentTracks.setPlaybackControlListener(this);
        this.mFragmentTracks.setOnItemClickListener(this);
        this.mPresenter.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mFragmentTracks.setPlaybackControlListener(null);
        this.mFragmentTracks.setOnItemClickListener(null);
        this.mPresenter.disconnect();
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mFragmentTracks = (FragmentTracks) this.getChildFragmentManager().findFragmentById(R.id.fraTracks);
    }

    @Override
    public void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mFragmentTracks.setMediaItems(pMediaItems);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mFragmentTracks.onPlaybackStateChanged(pPlaybackState);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mFragmentTracks.onMetadataChanged(pMediaMetadata);
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
    public void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.playFromMediaId(pMediaItem);
    }
}
