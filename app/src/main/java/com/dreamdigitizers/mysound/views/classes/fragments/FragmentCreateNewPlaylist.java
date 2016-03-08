package com.dreamdigitizers.mysound.views.classes.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.R;

public class FragmentCreateNewPlaylist extends FragmentBase {
    private EditText mTxtPlaylistTitle;
    private RadioButton mRadPublic;
    private RadioButton mRadPrivate;

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__create_new_playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mRadPublic = (RadioButton) pView.findViewById(R.id.radPublic);
        this.mRadPrivate = (RadioButton) pView.findViewById(R.id.radPrivate);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
    }

    @Override
    protected int getTitle() {
        return 0;
    }
}
