package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.mysound.views.interfaces.IViewFavorites;

public class ScreenFavorites extends ScreenTracks<IPresenterFavorites> implements IViewFavorites {
    @Override
    protected IPresenterFavorites createPresenter() {
        return (IPresenterFavorites) PresenterFactory.createPresenter(IPresenterFavorites.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__favorites, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_favorites;
    }

    @Override
    public int getScreenId() {
        return R.id.drawer_item__favorites;
    }
}
