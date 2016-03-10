package com.dreamdigitizers.mysound.views.classes.fragments;

import com.dreamdigitizers.mysound.views.classes.support.AdapterMediaItem;
import com.dreamdigitizers.mysound.views.classes.support.AdapterPlaylist;

public class FragmentPlaylists extends FragmentMediaItems {
    @Override
    protected AdapterMediaItem createAdapter() {
        return new AdapterPlaylist(this.getContext());
    }
}
