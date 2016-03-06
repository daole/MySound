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
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterMediaItems;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentPlaybackControls;
import com.dreamdigitizers.mysound.views.classes.support.MediaItemAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewMediaItems;

import java.util.List;

public abstract class ScreenMediaItems<P extends IPresenterMediaItems> extends ScreenBase<P>
        implements IViewMediaItems,
        FragmentMediaItems.IOnScrollEndListener,
        MediaItemAdapter.IOnItemClickListener,
        FragmentPlaybackControls.IPlaybackControlListener {
    private static final String ERROR_MESSAGE__MISSING_MEDIA_ITEMS_PLACE_HOLDER = "Missing FrameLayout with id \"placeHolderTracks\" in layout.";

    protected MenuItem mActionSearch;
    protected SearchView mSearchView;
    protected SwipeRefreshLayout mSfrRefresh;
    protected FrameLayout mPlaceHolderTracks;
    protected FragmentMediaItems mFragmentMediaItems;

    @Override
    public void onStart() {
        super.onStart();
        this.mFragmentMediaItems.setOnItemClickListener(this);
        this.mFragmentMediaItems.setPlaybackControlListener(this);
        this.mFragmentMediaItems.clearMediaItems();
        this.mPresenter.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mFragmentMediaItems.setOnItemClickListener(null);
        this.mFragmentMediaItems.setPlaybackControlListener(null);
        this.mPresenter.disconnect();
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getChildFragmentManager()
                .beginTransaction()
                .remove(this.mFragmentMediaItems)
                .commitAllowingStateLoss();
    }*/

    @Override
    protected void createOptionsMenu(Menu pMenu, MenuInflater pInflater) {
        pInflater.inflate(R.menu.menu__options_media_items_screen, pMenu);
        SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        this.mActionSearch = pMenu.findItem(R.id.option_menu_item__search);
        this.mSearchView = (SearchView) this.mActionSearch.getActionView();
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));
        this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {
                return ScreenMediaItems.this.onSearchViewQueryTextSubmitted(pQuery);
            }

            @Override
            public boolean onQueryTextChange(String pNewText) {
                return ScreenMediaItems.this.onSearchViewQueryTextChanged(pNewText);
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
            throw new NullPointerException(ScreenMediaItems.ERROR_MESSAGE__MISSING_MEDIA_ITEMS_PLACE_HOLDER);
        }
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        if (this.mSfrRefresh != null) {
            this.mSfrRefresh.setColorSchemeResources(R.color.color_primary_dark);
            this.mSfrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ScreenMediaItems.this.refresh();
                }
            });
        }

        this.mFragmentMediaItems = this.createFragmentMediaItems();
        this.mFragmentMediaItems.setOnScrollEndListener(this);
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderTracks, this.mFragmentMediaItems)
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
        this.mFragmentMediaItems.showLoadMoreProgress();
    }

    @Override
    public void hideLoadMoreProgress() {
        this.mFragmentMediaItems.hideLoadMoreProgress();
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mFragmentMediaItems.onPlaybackStateChanged(pPlaybackState);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mFragmentMediaItems.onMetadataChanged(pMediaMetadata);
    }

    @Override
    public void updateState() {
        this.mFragmentMediaItems.updateState();
    }

    @Override
    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        this.mFragmentMediaItems.addMediaItems(pMediaItems, pIsAddToTop);
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
        Bundle arguments = new Bundle();
        arguments.putString(Constants.BUNDLE_KEY__QUERY, pQuery);
        ScreenBase screenBase = new ScreenSoundsSearch();
        screenBase.setArguments(arguments);
        this.mScreenActionsListener.onChangeScreen(screenBase);
    }

    protected abstract FragmentMediaItems createFragmentMediaItems();
}
