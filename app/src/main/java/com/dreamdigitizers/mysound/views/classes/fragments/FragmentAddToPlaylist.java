package com.dreamdigitizers.mysound.views.classes.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.support.AdapterPlaylistDialog;

import java.util.List;

public class FragmentAddToPlaylist extends FragmentBase {
    private RecyclerView mLstPlaylists;

    private LinearLayoutManager mLinearLayoutManager;
    private AdapterPlaylistDialog mPlaylistDialogAdapter;

    private MediaBrowserCompat.MediaItem mTrack;
    private List<MediaBrowserCompat.MediaItem> mPlaylists;

    private AdapterPlaylistDialog.IOnAddRemoveButtonClickListener mListener;

    @Override
    protected void handleArguments(Bundle pArguments) {
        this.mTrack = pArguments.getParcelable(Constants.BUNDLE_KEY__TRACK);
        this.mPlaylists = pArguments.getParcelableArrayList(Constants.BUNDLE_KEY__PLAYLISTS);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__add_to_playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mLstPlaylists = (RecyclerView) pView.findViewById(R.id.lstPlaylists);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.mPlaylistDialogAdapter = new AdapterPlaylistDialog(this.getContext(), this.mTrack, this.mPlaylists, null);
        this.mPlaylistDialogAdapter.setOnAddRemoveButtonClickListener(this.mListener);
        this.mLstPlaylists.setLayoutManager(this.mLinearLayoutManager);
        this.mLstPlaylists.setAdapter(this.mPlaylistDialogAdapter);
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setOnAddRemoveButtonClickListener(AdapterPlaylistDialog.IOnAddRemoveButtonClickListener pListener) {
        this.mListener = pListener;
    }

    public void onAddToRemoveFromPlaylistResult() {
        this.mPlaylistDialogAdapter.onAddToRemoveFromPlaylistResult();
    }
}
