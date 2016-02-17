package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSounds;
import com.dreamdigitizers.mysound.views.interfaces.IViewSounds;

public class ScreenSounds extends ScreenTracks<IPresenterSounds> implements IViewSounds {
    @Override
    protected IPresenterSounds createPresenter() {
        return (IPresenterSounds) PresenterFactory.createPresenter(IPresenterSounds.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__sounds, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_sounds;
    }

    @Override
    public int getScreenId() {
        return R.id.drawer_item__sounds;
    }
}
