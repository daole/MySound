package com.dreamdigitizers.mysound.views.classes.fragments;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;

import java.util.List;

public class FragmentTracks extends FragmentBase {
    private RecyclerView mLstTracks;
    private FragmentPlaybackControls mFragmentPlaybackControls;
    private TrackAdapter mTrackAdapter;

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__tracks, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mLstTracks = (RecyclerView) pView.findViewById(R.id.lstTracks);
        this.mFragmentPlaybackControls = (FragmentPlaybackControls) this.getChildFragmentManager().findFragmentById(R.id.fraPlaybackControls);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mTrackAdapter = new TrackAdapter(this.getContext());
        this.mLstTracks.setAdapter(this.mTrackAdapter);
        this.mLstTracks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.hidePlaybackControls();
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setPlaybackControlListener(FragmentPlaybackControls.IPlaybackControlListener pListener) {
        this.mFragmentPlaybackControls.setPlaybackControlListener(pListener);
    }

    public void setOnItemClickListener(TrackAdapter.IOnItemClickListener pListener) {
        this.mTrackAdapter.setOnItemClickListener(pListener);
    }

    public void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mTrackAdapter.setMediaItems(pMediaItems);
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mTrackAdapter.notifyDataSetChanged();
        this.mFragmentPlaybackControls.onPlaybackStateChanged(pPlaybackState);
        if (isShowPlaybackControls(pPlaybackState)) {
            this.showPlaybackControls();
        } else {
            this.hidePlaybackControls();
        }
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mTrackAdapter.notifyDataSetChanged();
        this.mFragmentPlaybackControls.onMetadataChanged(pMediaMetadata);
    }

    private boolean isShowPlaybackControls(PlaybackStateCompat pPlaybackState) {
        switch (pPlaybackState.getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }

    private void showPlaybackControls() {
        if (this.mFragmentPlaybackControls.isHidden()) {
            this.getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(com.dreamdigitizers.androidbaselibrary.R.anim.slide_in_from_bottom, 0)
                    .show(this.mFragmentPlaybackControls)
                    .commit();
        }
    }

    private void hidePlaybackControls() {
        if (!this.mFragmentPlaybackControls.isHidden()) {
            this.getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(0, com.dreamdigitizers.androidbaselibrary.R.anim.slide_out_to_bottom)
                    .hide(this.mFragmentPlaybackControls)
                    .commit();
        }
    }
}
