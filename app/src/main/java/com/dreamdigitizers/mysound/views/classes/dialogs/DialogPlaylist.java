package com.dreamdigitizers.mysound.views.classes.dialogs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dreamdigitizers.androidbaselibrary.views.classes.dialogs.DialogFragmentBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentAddToPlaylist;
import com.dreamdigitizers.mysound.views.classes.fragments.FragmentCreateNewPlaylist;
import com.dreamdigitizers.mysound.views.classes.support.AdapterPlaylistDialog;

import java.util.ArrayList;
import java.util.List;

public class DialogPlaylist extends DialogFragmentBase {
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private Button mBtnCancel;

    private FragmentAddToPlaylist mFragmentAddToPlaylist;
    private FragmentCreateNewPlaylist mFragmentCreateNewPlaylist;

    private Bundle mArguments;

    private AdapterPlaylistDialog.IOnAddRemoveButtonClickListener mOnAddRemoveButtonClickListener;
    private FragmentCreateNewPlaylist.IOnOkButtonClickListener mOnOkButtonClickListener;

    public DialogPlaylist() {
        this.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    protected void handleArguments(Bundle pArguments) {
        this.mArguments = pArguments;
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.dialog__playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveItems(View pView) {
        this.mTabs = (TabLayout) pView.findViewById(R.id.tabs);
        this.mViewPager = (ViewPager) pView.findViewById(R.id.viewPager);
        this.mBtnCancel = (Button) pView.findViewById(R.id.btnCancel);
    }

    @Override
    protected void mapInformationToItems(View pView) {
        this.mViewPager.setAdapter(new ViewPagerAdapter(this.getChildFragmentManager()));
        this.mTabs.setupWithViewPager(this.mViewPager);

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                DialogPlaylist.this.buttonCancelClicked();
            }
        });
    }

    public void setOnAddRemoveButtonClickListener(AdapterPlaylistDialog.IOnAddRemoveButtonClickListener pListener) {
        this.mOnAddRemoveButtonClickListener = pListener;
    }

    public void setOnOkButtonClickListener(FragmentCreateNewPlaylist.IOnOkButtonClickListener pListener) {
        this.mOnOkButtonClickListener = pListener;
    }

    public void onAddToRemoveFromPlaylistResult() {
        this.mFragmentAddToPlaylist.onAddToRemoveFromPlaylistResult();
    }

    private void buttonCancelClicked() {
        this.dismiss();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mFragmentTitles;

        public ViewPagerAdapter(FragmentManager pFragmentManager) {
            super(pFragmentManager);
            this.mFragments = new ArrayList<>();

            DialogPlaylist.this.mFragmentAddToPlaylist = new FragmentAddToPlaylist();
            DialogPlaylist.this.mFragmentAddToPlaylist.setArguments(DialogPlaylist.this.mArguments);
            DialogPlaylist.this.mFragmentAddToPlaylist.setOnAddRemoveButtonClickListener(DialogPlaylist.this.mOnAddRemoveButtonClickListener);
            this.mFragments.add(DialogPlaylist.this.mFragmentAddToPlaylist);

            DialogPlaylist.this.mFragmentCreateNewPlaylist = new FragmentCreateNewPlaylist();
            DialogPlaylist.this.mFragmentAddToPlaylist.setArguments(DialogPlaylist.this.mArguments);
            DialogPlaylist.this.mFragmentCreateNewPlaylist.setOnOkButtonClickListener(DialogPlaylist.this.mOnOkButtonClickListener);
            this.mFragments.add(DialogPlaylist.this.mFragmentCreateNewPlaylist);

            this.mFragmentTitles = new ArrayList<>();
            this.mFragmentTitles.add(DialogPlaylist.this.getContext().getString(R.string.tab__add_to_playlist));
            this.mFragmentTitles.add(DialogPlaylist.this.getContext().getString(R.string.tab__create_new_playlist));
        }

        @Override
        public Fragment getItem(int pPosition) {
            return this.mFragments.get(pPosition);
        }

        @Override
        public int getCount() {
            return this.mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int pPosition) {
            return this.mFragmentTitles.get(pPosition);
        }
    }
}
