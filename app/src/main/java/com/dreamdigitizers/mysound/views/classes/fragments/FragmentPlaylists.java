package com.dreamdigitizers.mysound.views.classes.fragments;

import com.dreamdigitizers.mysound.views.classes.support.MediaItemAdapter;
import com.dreamdigitizers.mysound.views.classes.support.PlaylistAdapter;

public class FragmentPlaylists extends FragmentMediaItems {
    @Override
    protected MediaItemAdapter createAdapter() {
        return new PlaylistAdapter(this.getContext());
    }
}
