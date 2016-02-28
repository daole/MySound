package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaybackControls;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentTracks;
import com.dreamdigitizers.mysound.views.classes.support.TrackAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewTracks;

import java.util.List;

public abstract class ScreenTracks<P extends IPresenterTracks> extends ScreenBase<P>
        implements IViewTracks, FragmentTracks.IOnScrollEndListener, FragmentPlaybackControls.IPlaybackControlListener, TrackAdapter.IOnItemClickListener {
    private static final String ERROR_MESSAGE__MISSING_TRACKS_PLACE_HOLDER = "Missing FrameLayout with id \"placeHolderTracks\" in layout.";

    protected MenuItem mActionSearch;
    protected SearchView mSearchView;
    protected SwipeRefreshLayout mSfrRefresh;
    protected FrameLayout mPlaceHolderTracks;
    protected FragmentTracks mFragmentTracks;

    @Override
    public void onStart() {
        super.onStart();
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
    public void onDestroyView() {
        super.onDestroyView();
        this.getChildFragmentManager()
                .beginTransaction()
                .remove(this.mFragmentTracks)
                .commitAllowingStateLoss();
    }

    @Override
    protected void createOptionsMenu(Menu pMenu, MenuInflater pInflater) {
        pInflater.inflate(R.menu.menu__options_tracks_screen, pMenu);

        SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        this.mActionSearch = pMenu.findItem(R.id.option_menu_item__search);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        int id = pMenuItem.getItemId();
        switch (id) {
            case R.id.option_menu_item__search:
                return true;
            default:
                return super.onOptionsItemSelected(pMenuItem);
        }
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mSfrRefresh = (SwipeRefreshLayout) pView.findViewById(R.id.sfrRefresh);
        this.mPlaceHolderTracks = (FrameLayout) pView.findViewById(R.id.placeHolderTracks);
        if (this.mPlaceHolderTracks == null) {
            throw new NullPointerException(ScreenTracks.ERROR_MESSAGE__MISSING_TRACKS_PLACE_HOLDER);
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

        this.mFragmentTracks = new FragmentTracks();
        this.mFragmentTracks.setOnScrollEndListener(this);
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderTracks, this.mFragmentTracks)
                .commit();
    }

    @Override
    public void showRefreshProgress() {
        if (this.mSfrRefresh != null && !this.mSfrRefresh.isRefreshing()) {
            this.mSfrRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshProgress() {
        if (this.mSfrRefresh != null && this.mSfrRefresh.isRefreshing()) {
            this.mSfrRefresh.setRefreshing(false);
        }
    }

    @Override
     public void showLoadMoreProgress() {
        this.mFragmentTracks.showLoadMoreProgress();
    }

    @Override
    public void hideLoadMoreProgress() {
        this.mFragmentTracks.hideLoadMoreProgress();
    }

    @Override
    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        this.mFragmentTracks.addMediaItems(pMediaItems, pIsAddToTop);
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
    public void updateState() {
        this.mFragmentTracks.updateState();
    }

    @Override
    public void onScrollEnd() {
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

    @Override
    public void onFavoriteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.favorite(pMediaItem);
    }

    protected void handleSearch(String pQuery) {
    }

    private void refresh() {
        this.mPresenter.refresh();
    }

    private boolean onSearchViewQueryTextSubmitted(String pQuery) {
        //MenuItemCompat.collapseActionView(this.mActionSearch);
        this.mSearchView.clearFocus();
        if (this instanceof ScreenSoundsSearch) {
            this.handleSearch(pQuery);
        } else {
            this.goToSoundsSearchScreen(pQuery);
        }
        return true;
    }

    private boolean onSearchViewQueryTextChanged(String pNewText) {
        return false;
    }

    private void goToSoundsSearchScreen(String pQuery) {
        ScreenBase screenBase = new ScreenSoundsSearch();
        Bundle arguments = new Bundle();
        arguments.putString(Constants.BUNDLE_KEY__QUERY, pQuery);
        screenBase.setArguments(arguments);
        this.mScreenActionsListener.onChangeScreen(screenBase);
    }
}
