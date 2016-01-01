package com.dreamdigitizers.mysound.views.classes.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.fragments.screens.ScreenHome;

public class ActivityMain extends ActivityBase {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /*case R.id.home:
                this.mDrawerLayout.openDrawer(GravityCompat.START);
                return true;*/
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
        //ActionBar actionBar = this.getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic__drawer);
    }

    @Override
    protected ScreenBase getStartScreen() {
        return new ScreenHome();
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
                pMenuItem.setChecked(true);

                switch (pMenuItem.getItemId()){
                    case R.id.drawer_item__home:
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
        });

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
}
