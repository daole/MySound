package com.dreamdigitizers.mysound.views.classes.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.R;

public class FragmentAddToPlaylist extends FragmentBase {
    private RadioGroup mRadioButtons;

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__add_to_playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mRadioButtons = (RadioGroup) pView.findViewById(R.id.radioButtons);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
    }

    @Override
    protected int getTitle() {
        return 0;
    }
}
