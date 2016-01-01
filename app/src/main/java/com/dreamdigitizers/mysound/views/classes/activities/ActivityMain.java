package com.dreamdigitizers.mysound.views.classes.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.fragments.screens.ScreenHome;

public class ActivityMain extends ActivityBase {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private int mCurrentSelectedMenuId;

    public ActivityMain() {
        this.mCurrentSelectedMenuId = R.id.drawer_item__home;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.actionSearch:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setLayout() {
        this.setContentView(R.layout.activity__main);
    }

    @Override
    protected void retrieveItems() {
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        this.mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.mNavigationView = (NavigationView) this.findViewById(R.id.navigationView);
    }

    @Override
    protected void mapInformationToItems() {
        this.setSupportActionBar(this.mToolbar);
        this.setUpNavigationDrawer();
        ApiFactory.initialize(Constants.SOUNDCLOUD__CLIENT_ID);
    }

    @Override
    protected ScreenBase getStartScreen() {
        return null;
    }

    @Override
    protected int getScreenContainerViewId() {
        return R.id.container;
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

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
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

        this.mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private boolean navigationItemSelected(int pId) {
        this.mNavigationView.setCheckedItem(pId);
        this.mCurrentSelectedMenuId = pId;
        switch (pId) {
            case R.id.drawer_item__home:
                this.changeScreen(new ScreenHome());
                return true;
            case R.id.drawer_item__sounds:
                return true;
            case R.id.drawer_item__playlists:
                return true;
            case R.id.drawer_item__favorites:
                return true;
            default:
                return true;
        }
    }
}
