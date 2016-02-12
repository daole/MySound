package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSoundsSearch;
import com.dreamdigitizers.mysound.views.interfaces.IViewSoundsSearch;

public class ScreenSoundsSearch extends ScreenTracks<IPresenterSoundsSearch> implements IViewSoundsSearch {
    private String mQuery;

    @Override
    protected void handleArguments(Bundle pArguments) {
        this.mQuery = pArguments.getString(Constants.BUNDLE_KEY__QUERY);
    }

    @Override
    protected void recoverInstanceState(Bundle pSavedInstanceState) {
        this.mQuery = pSavedInstanceState.getString(Constants.BUNDLE_KEY__QUERY);
    }

    @Override
    public void onSaveInstanceState(Bundle pOutState) {
        super.onSaveInstanceState(pOutState);
        pOutState.putString(Constants.BUNDLE_KEY__QUERY, this.mQuery);
    }

    @Override
    protected void handleSearch(String pQuery) {
        this.showNetworkProgress();
        this.mQuery = pQuery;
        this.mFragmentTracks.clearMediaItems();
        this.mPresenter.refresh();
    }

    @Override
    protected IPresenterSoundsSearch createPresenter() {
        return (IPresenterSoundsSearch) PresenterFactory.createPresenter(IPresenterSoundsSearch.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__sounds_search, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        super.retrieveScreenItems(pView);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        super.mapInformationToScreenItems(pView);
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_sounds_search;
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    public String getQuery() {
        return this.mQuery;
    }
}
