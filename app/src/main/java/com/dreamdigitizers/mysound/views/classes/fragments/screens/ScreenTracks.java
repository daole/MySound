package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaybackControls;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentTracks;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.ArrayList;
import java.util.List;

public abstract class ScreenTracks<P extends IPresenterTracks> extends ScreenBase<P>
        implements IViewTracks, FragmentTracks.IOnScrollEndListener, FragmentPlaybackControls.IPlaybackControlListener, TrackAdapter.IOnItemClickListener {
    private static final String ERROR_MESSAGE__MISSING_FRAGMENT = "Missing fragment with id \"fraTracks\" in layout.";

    private static final String BUNDLE_KEY__MEDIA_ITEMS = "media_items";

    protected MenuItem mActionSearch;
    protected SearchView mSearchView;
    protected SwipeRefreshLayout mSfrRefresh;
    protected FragmentTracks mFragmentTracks;

    protected List<MediaBrowserCompat.MediaItem> mMediaItems;

    @Override
    public void onSaveInstanceState(Bundle pOutState) {
        super.onSaveInstanceState(pOutState);
        pOutState.putParcelableArrayList(ScreenTracks.BUNDLE_KEY__MEDIA_ITEMS, (ArrayList) this.mMediaItems);
    }

    @Override
    protected void recoverInstanceState(Bundle pSavedInstanceState) {
        this.mMediaItems = pSavedInstanceState.getParcelableArrayList(ScreenTracks.BUNDLE_KEY__MEDIA_ITEMS);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mFragmentTracks.setPlaybackControlListener(this);
        this.mFragmentTracks.setOnItemClickListener(this);
        if (this.mMediaItems == null || this.mMediaItems.size() <= 0) {
            this.mPresenter.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mFragmentTracks.setPlaybackControlListener(null);
        this.mFragmentTracks.setOnItemClickListener(null);
        this.mPresenter.disconnect();
    }

    @Override
    protected void createOptionsMenu(Menu pMenu, MenuInflater pInflater) {
        pInflater.inflate(R.menu.menu__tracks, pMenu);

        SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        this.mActionSearch = pMenu.findItem(R.id.actionSearch);
        this.mSearchView = (SearchView) this.mActionSearch.getActionView();
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));
        this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {
                return ScreenTracks.this.onSearchViewQueryTextSubmitted(pQuery);
            }

            @Override
            public boolean onQueryTextChange(String pNewText) {
                return ScreenTracks.this.onSearchViewQueryTextChanged(pNewText);
            }
        });
        this.mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        int id = pMenuItem.getItemId();
        switch (id) {
            case R.id.actionSearch:
                return true;
        }

        return super.onOptionsItemSelected(pMenuItem);
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mSfrRefresh = (SwipeRefreshLayout) pView.findViewById(R.id.sfrRefresh);
        this.mFragmentTracks = (FragmentTracks) this.getChildFragmentManager().findFragmentById(R.id.fraTracks);
        if (this.mFragmentTracks == null) {
            throw new NullPointerException(ScreenTracks.ERROR_MESSAGE__MISSING_FRAGMENT);
        }
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        if (this.mSfrRefresh != null) {
            this.mSfrRefresh.setColorSchemeResources(R.color.color_primary_dark);
            this.mSfrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ScreenTracks.this.refresh();
                }
            });
        }

        this.mFragmentTracks.setOnScrollEndListener(this);
        if (this.mMediaItems != null && this.mMediaItems.size() > 0) {
            this.mFragmentTracks.setMediaItems(this.mMediaItems);
        }
    }

    @Override
    public void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mMediaItems = pMediaItems;
        this.mFragmentTracks.setMediaItems(this.mMediaItems);
    }

    @Override
    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        if (this.mMediaItems == null) {
            this.mMediaItems = new ArrayList<>();
        }
        if (pIsAddToTop) {
            this.mMediaItems.addAll(0, pMediaItems);
        } else {
            this.mMediaItems.addAll(pMediaItems);
        }
        if (this.mSfrRefresh != null && this.mSfrRefresh.isRefreshing()) {
            this.mSfrRefresh.setRefreshing(false);
        }
        this.mFragmentTracks.addMediaItems(pMediaItems, pIsAddToTop);
        this.mFragmentTracks.hideLoadMoreProgress();
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
    public void onScrollEnd() {
        this.mFragmentTracks.showLoadMoreProgress();
        this.mPresenter.loadMore();
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

    private void refresh() {
        this.mSfrRefresh.setRefreshing(true);
        this.mPresenter.refresh();
    }

    private boolean onSearchViewQueryTextSubmitted(String pQuery) {
        MenuItemCompat.collapseActionView(this.mActionSearch);
        return true;
    }

    private boolean onSearchViewQueryTextChanged(String pNewText) {
        return false;
    }
}
