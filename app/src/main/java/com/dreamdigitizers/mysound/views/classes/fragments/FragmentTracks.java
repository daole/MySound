package com.dreamdigitizers.mysound.views.classes.fragments;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;

import java.util.List;

public class FragmentTracks extends FragmentBase {
    private static final int VISIBLE_THRESHOLD = 0;

    protected RecyclerView mLstTracks;
    protected ProgressBar mPgbLoading;
    protected FrameLayout mPlaceHolderPlaybackControls;
    protected FragmentPlaybackControls mFragmentPlaybackControls;

    protected LinearLayoutManager mLinearLayoutManager;
    protected TrackAdapter mTrackAdapter;
    protected IOnScrollEndListener mListener;

    protected boolean mIsLoading;
    protected int mPreviousTotalItemCount;

    public FragmentTracks() {
        this.mIsLoading = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getChildFragmentManager()
                .beginTransaction()
                .remove(this.mFragmentPlaybackControls)
                .commitAllowingStateLoss();
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__tracks, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mLstTracks = (RecyclerView) pView.findViewById(R.id.lstTracks);
        this.mPgbLoading = (ProgressBar) pView.findViewById(R.id.pgbLoading);
        this.mPlaceHolderPlaybackControls = (FrameLayout) pView.findViewById(R.id.placeHolderPlaybackControls);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.mTrackAdapter = new TrackAdapter(this.getContext());
        this.mLstTracks.setLayoutManager(this.mLinearLayoutManager);
        this.mLstTracks.setAdapter(this.mTrackAdapter);
        this.mLstTracks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView pRecyclerView, int pDx, int pDy) {
                FragmentTracks.this.onTrackListScrolled();
            }
        });

        this.mFragmentPlaybackControls = new FragmentPlaybackControls();
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderPlaybackControls, this.mFragmentPlaybackControls)
                .commit();
        this.hidePlaybackControls();
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setOnScrollEndListener(IOnScrollEndListener pListener) {
        this.mListener = pListener;
    }

    public void setPlaybackControlListener(FragmentPlaybackControls.IPlaybackControlListener pListener) {
        this.mFragmentPlaybackControls.setPlaybackControlListener(pListener);
    }

    public void setOnItemClickListener(TrackAdapter.IOnItemClickListener pListener) {
        this.mTrackAdapter.setOnItemClickListener(pListener);
    }

    public void clearMediaItems() {
        this.mPreviousTotalItemCount = 0;
        this.mTrackAdapter.clearMediaItems();
    }

    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        this.mTrackAdapter.addMediaItems(pMediaItems, pIsAddToTop);
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mTrackAdapter.onPlaybackStateChanged(pPlaybackState);
        this.mFragmentPlaybackControls.onPlaybackStateChanged(pPlaybackState);
        if (isShowPlaybackControls(pPlaybackState)) {
            this.showPlaybackControls();
        } else {
            this.hidePlaybackControls();
        }
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mTrackAdapter.onMetadataChanged(pMediaMetadata);
        this.mFragmentPlaybackControls.onMetadataChanged(pMediaMetadata);
    }

    public void updateState() {
        this.mTrackAdapter.notifyDataSetChanged();
    }

    public void showLoadMoreProgress() {
        this.mPgbLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoadMoreProgress() {
        this.mPgbLoading.setVisibility(View.GONE);
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

    private void onTrackListScrolled() {
        int visibleItemCount = this.mLinearLayoutManager.getChildCount();
        int firstVisibleItem = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        int totalItemCount = this.mLinearLayoutManager.getItemCount();

        if (this.mIsLoading) {
            if (totalItemCount > this.mPreviousTotalItemCount) {
                this.mIsLoading = false;
                this.mPreviousTotalItemCount = totalItemCount;
            }
        }

        if (!this.mIsLoading && totalItemCount - visibleItemCount <= firstVisibleItem + FragmentTracks.VISIBLE_THRESHOLD) {
            this.mIsLoading = true;
            if (this.mListener != null) {
                this.mListener.onScrollEnd();
            }
        }
    }

    public interface IOnScrollEndListener {
        void onScrollEnd();
    }
}
