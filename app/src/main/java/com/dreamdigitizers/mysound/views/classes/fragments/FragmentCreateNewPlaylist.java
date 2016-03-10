package com.dreamdigitizers.mysound.views.classes.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;

public class FragmentCreateNewPlaylist extends FragmentBase {
    private EditText mTxtPlaylistTitle;
    private RadioButton mRadPublic;
    private RadioButton mRadPrivate;
    private Button mBtnOK;

    private MediaBrowserCompat.MediaItem mTrack;

    private IOnOkButtonClickListener mListener;

    @Override
    protected void handleArguments(Bundle pArguments) {
        this.mTrack = pArguments.getParcelable(Constants.BUNDLE_KEY__TRACK);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__create_new_playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mTxtPlaylistTitle = (EditText) pView.findViewById(R.id.txtPlaylistTitle);
        this.mRadPublic = (RadioButton) pView.findViewById(R.id.radPublic);
        this.mRadPrivate = (RadioButton) pView.findViewById(R.id.radPrivate);
        this.mBtnOK = (Button) pView.findViewById(R.id.btnOK);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                FragmentCreateNewPlaylist.this.buttonOKClicked();
            }
        });
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setOnOkButtonClickListener(IOnOkButtonClickListener pListener) {
        this.mListener = pListener;
    }

    private void buttonOKClicked() {
        if (this.mListener != null) {
            String playlistTitle = this.mTxtPlaylistTitle.getText().toString();
            boolean isPublic = this.mRadPublic.isChecked();
            this.mListener.onOkButtonClicked(this.mTrack, playlistTitle, isPublic);
        }
    }

    public interface IOnOkButtonClickListener {
        void onOkButtonClicked(MediaBrowserCompat.MediaItem pTrack, String pPlaylistTitle, boolean pIsPublic);
    }
}
