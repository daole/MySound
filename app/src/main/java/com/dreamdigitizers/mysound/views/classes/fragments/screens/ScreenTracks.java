package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityMain;
import com.dreamdigitizers.mysound.views.classes.dialogs.DialogPlaylist;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentTracks;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

public abstract class ScreenTracks<P extends IPresenterTracks> extends ScreenMediaItems<P> implements IViewTracks, TrackAdapter.IOnItemClickListener {
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

    @Override
    public void onPlaylistContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment previousInstance = this.getChildFragmentManager().findFragmentByTag(DialogPlaylist.class.getName());
        if (previousInstance != null) {
            fragmentTransaction.remove(previousInstance);
        }
        fragmentTransaction.addToBackStack(null);
        DialogPlaylist dialogPlaylist = new DialogPlaylist();
        dialogPlaylist.show(fragmentTransaction, DialogPlaylist.class.getName());
    }
}
