package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterSounds;
import com.dreamdigitizers.mysound.views.classes.support.SoundAdapter;
import com.dreamdigitizers.mysound.views.interfaces.IViewSounds;

import java.util.List;

public class ScreenSounds extends ScreenBase<IPresenterSounds> implements IViewSounds {
    private RecyclerView mLstSounds;
    private SoundAdapter mSoundAdapter;

    @Override
    public void onStart() {
        super.onStart();
        this.mPresenter.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mPresenter.disconnect();
    }

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
    protected void retrieveScreenItems(View pView) {
        this.mLstSounds = (RecyclerView) pView.findViewById(R.id.lstSounds);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mSoundAdapter = new SoundAdapter(this.getContext());
        this.mLstSounds.setAdapter(this.mSoundAdapter);
        this.mLstSounds.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_sounds;
    }

    @Override
    public void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mSoundAdapter.setMediaItems(pMediaItems);
    }
}
