package com.dreamdigitizers.mysound.views.classes.fragments;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.support.MediaItemAdapter;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;

public class FragmentTracks extends FragmentMediaItems {
    protected FrameLayout mPlaceHolderPlaybackControls;
    protected FragmentPlaybackControls mFragmentPlaybackControls;

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getChildFragmentManager()
                .beginTransaction()
                .remove(this.mFragmentPlaybackControls)
                .commitAllowingStateLoss();
    }*/

    @Override
    protected void retrieveScreenItems(View pView) {
        super.retrieveScreenItems(pView);
        this.mPlaceHolderPlaybackControls = (FrameLayout) pView.findViewById(R.id.placeHolderPlaybackControls);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        super.mapInformationToScreenItems(pView);
        this.mFragmentPlaybackControls = new FragmentPlaybackControls();
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderPlaybackControls, this.mFragmentPlaybackControls)
                .commit();
        this.hidePlaybackControls();
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__tracks, pContainer, false);
        return rootView;
    }

    @Override
    protected MediaItemAdapter createAdapter() {
        return new TrackAdapter(this.getContext());
    }

    public void setPlaybackControlListener(FragmentPlaybackControls.IPlaybackControlListener pListener) {
        this.mFragmentPlaybackControls.setPlaybackControlListener(pListener);
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        ((TrackAdapter) this.mMediaItemAdapter).onPlaybackStateChanged(pPlaybackState);
        this.mFragmentPlaybackControls.onPlaybackStateChanged(pPlaybackState);
        if (isShowPlaybackControls(pPlaybackState)) {
            this.showPlaybackControls();
        } else {
            this.hidePlaybackControls();
        }
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        ((TrackAdapter) this.mMediaItemAdapter).onMetadataChanged(pMediaMetadata);
        this.mFragmentPlaybackControls.onMetadataChanged(pMediaMetadata);
    }

    public void updateState() {
        ((TrackAdapter) this.mMediaItemAdapter).notifyDataSetChanged();
    }

    private boolean isShowPlaybackControls(PlaybackStateCompat pPlaybackState) {
        switch (pPlaybackState.getState()) {
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
            case PlaybackStateCompat.STATE_ERROR:
                return false;
            default:
                return true;
        }
    }

    private void showPlaybackControls() {
        if (this.mFragmentPlaybackControls.isHidden()) {
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(com.dreamdigitizers.androidbaselibrary.R.anim.slide_in_from_bottom, 0)
                    .show(this.mFragmentPlaybackControls)
                    .commit();
        }
    }

    private void hidePlaybackControls() {
        if (!this.mFragmentPlaybackControls.isHidden()) {
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(0, com.dreamdigitizers.androidbaselibrary.R.anim.slide_out_to_bottom)
                    .hide(this.mFragmentPlaybackControls)
                    .commit();
        }
    }
}
