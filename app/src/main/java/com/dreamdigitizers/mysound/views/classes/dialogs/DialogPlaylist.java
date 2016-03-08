package com.dreamdigitizers.mysound.views.classes.dialogs;

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

import java.util.ArrayList;
import java.util.List;

public class DialogPlaylist extends DialogFragmentBase {
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private Button mBtnCancel;
    private Button mBtnOK;

    public DialogPlaylist() {
        this.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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
        this.mBtnOK = (Button) pView.findViewById(R.id.btnOK);
    }

    @Override
    protected void mapInformationToItems(View pView) {
        this.mViewPager.setAdapter(new ViewPagerAdapter(this.getChildFragmentManager()));
        this.mTabs.setupWithViewPager(this.mViewPager);

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                DialogPlaylist.this.onButtonCancelClicked();
            }
        });

        this.mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                DialogPlaylist.this.onButtonOKClicked();
            }
        });
    }

    private void onButtonCancelClicked() {
        this.dismiss();
    }

    private void onButtonOKClicked() {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mFragmentTitles;

        public ViewPagerAdapter(FragmentManager pFragmentManager) {
            super(pFragmentManager);

            this.mFragments = new ArrayList<>();
            this.mFragments.add(new FragmentAddToPlaylist());
            this.mFragments.add(new FragmentCreateNewPlaylist());

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
