package com.dreamdigitizers.mysound.views.classes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.support.AdListener;
import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterMain;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.fragments.screens.ScreenFavorites;
import com.dreamdigitizers.mysound.views.classes.fragments.screens.ScreenHome;
import com.dreamdigitizers.mysound.views.classes.fragments.screens.ScreenSounds;
import com.dreamdigitizers.mysound.views.interfaces.IViewMain;
import com.google.android.gms.ads.AdView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMain extends ActivityBase {
    public static final String EXTRA__CURRENT_MEDIA_DESCRIPTION = "current_media_description";

    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private CircleImageView mImgAvatar;
    private TextView mLblMyName;
    private AdView mAdView;

    private ViewMain mView;
    private IPresenterMain mPresenter;

    private Share.OnDataChangedListener mShareDataChangedListener;

    private int mCurrentSelectedMenuId;

    public ActivityMain() {
        this.mCurrentSelectedMenuId = R.id.drawer_item__home;
    }

    @Override
    protected boolean handleNeededBackProcess() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        this.mView = new ViewMain();
        this.mPresenter = (IPresenterMain) PresenterFactory.createPresenter(IPresenterMain.class, this.mView);
    }

    @Override
    protected void onPostCreate(Bundle pSavedInstanceState) {
        super.onPostCreate(pSavedInstanceState);
        this.mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onSaveInstanceState(Bundle pOutState) {
        super.onSaveInstanceState(pOutState);
        pOutState.putInt(Constants.BUNDLE_KEY__SELECTED_MENU_ID, this.mCurrentSelectedMenuId);
    }

    @Override
    protected void recoverInstanceState(Bundle pSavedInstanceState) {
        super.recoverInstanceState(pSavedInstanceState);
        this.mCurrentSelectedMenuId = pSavedInstanceState.getInt(Constants.BUNDLE_KEY__SELECTED_MENU_ID);
    }

    @Override
    public void onSetCurrentScreen(ScreenBase pCurrentScreen) {
        super.onSetCurrentScreen(pCurrentScreen);
        int id = pCurrentScreen.getScreenId();
        if (id > 0) {
            this.mCurrentSelectedMenuId = id;
            this.mNavigationView.setCheckedItem(this.mCurrentSelectedMenuId);
        }
    }

    @Override
    protected CoordinatorLayout getCoordinatorLayout() {
        return this.mCoordinatorLayout;
    }

    @Override
    protected void setLayout() {
        this.setContentView(R.layout.activity__main);
    }

    @Override
    protected void retrieveItems() {
        this.mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawerLayout);
        this.mCoordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.coordinatorLayout);
        this.mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.mNavigationView = (NavigationView) this.findViewById(R.id.navigationView);
        this.mImgAvatar = (CircleImageView) this.mNavigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
        this.mLblMyName = (TextView) this.mNavigationView.getHeaderView(0).findViewById(R.id.lblMyName);
        this.mAdView = (AdView) this.findViewById(R.id.adView);
    }

    @Override
    protected void mapInformationToItems() {
        this.mAdView.setAdListener(new AdListener(this.mAdView));
        this.setSupportActionBar(this.mToolbar);
        this.setUpNavigationDrawer();
        Me me = Share.getMe();
        if (me != null) {
            this.showMe(me);
        }
        ApiFactory.initialize(Constants.SOUNDCLOUD__CLIENT_ID, Share.getAccessToken());
        this.mShareDataChangedListener = new Share.OnDataChangedListener() {
            @Override
            public void onMeChanged(final Me pNewMe, final Me pOldMe) {
                ActivityMain.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityMain.this.showMe(pNewMe);
                    }
                });
            }
        };
        Share.registerListener(this.mShareDataChangedListener);
    }

    @Override
    protected ScreenBase getStartScreen() {
        return null;
    }

    @Override
    protected int getScreenContainerViewId() {
        return R.id.placeHolderScreen;
    }

    private void setUpNavigationDrawer() {
        this.mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem pMenuItem) {
                ActivityMain.this.mDrawerLayout.closeDrawers();
                int id = pMenuItem.getItemId();
                return ActivityMain.this.navigationItemSelected(id);
            }
        });
        this.navigationItemSelected(this.mCurrentSelectedMenuId);

        this.mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                this.mDrawerLayout,
                this.mToolbar,
                R.string.desc__navigation_drawer_open,
                R.string.desc__navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        this.mDrawerLayout.setDrawerListener(this.mActionBarDrawerToggle);
    }

    private boolean navigationItemSelected(int pId) {
        this.mCurrentSelectedMenuId = pId;
        this.mNavigationView.setCheckedItem(this.mCurrentSelectedMenuId);
        ScreenBase screenBase = null;

        switch (this.mCurrentSelectedMenuId) {
            case R.id.drawer_item__home:
                if (!(this.mCurrentScreen instanceof ScreenHome)) {
                    screenBase = new ScreenHome();
                }
                break;
            case R.id.drawer_item__sounds:
                if (!(this.mCurrentScreen instanceof ScreenSounds)) {
                    screenBase = new ScreenSounds();
                }
                break;
            case R.id.drawer_item__favorites:
                if (!(this.mCurrentScreen instanceof ScreenFavorites)) {
                    screenBase = new ScreenFavorites();
                }
                break;
            /*
            case R.id.drawer_item__playlists:
                break;
            */
            case R.id.drawer_item__logout:
                this.logout();
                break;
            default:
                break;
        }

        if(screenBase != null) {
            this.changeScreen(screenBase);
        }

        return true;
    }

    private void showMe(Me pMe) {
        UtilsImage.loadBitmap(ActivityMain.this, pMe.getAvatarUrl(), R.drawable.ic__default_profile, ActivityMain.this.mImgAvatar);
        ActivityMain.this.mLblMyName.setText(pMe.getFullName());
    }

    private void logout() {
        this.mPresenter.stopMediaPlayer();
        this.mPresenter.deleteAccessToken();
        Share.dispose();
        ApiFactory.dispose();
        this.goToInitializationActivity();
    }

    private void goToInitializationActivity() {
        Intent intent = new Intent(this, ActivityInitialization.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.BUNDLE_KEY__IS_LOGOUT, true);
        this.changeActivity(intent, true);
    }

    private class ViewMain extends IViewBase.ViewBase implements IViewMain {
        @Override
        public Context getViewContext() {
            return ActivityMain.this;
        }
    }
}
