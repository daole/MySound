package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.mysound.views.classes.support.MediaItemAdapter;
import com.dreamdigitizers.mysound.views.classes.support.PlaylistAdapter;

public class FragmentPlaylists extends FragmentMediaItems {
    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__playlists, pContainer, false);
        return rootView;
    }

    @Override
    protected MediaItemAdapter createAdapter() {
        return new PlaylistAdapter(this.getContext());
    }
}
