package com.dreamdigitizers.mysound.views.activities;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamdigitizers.androidbaselibrary.views.activities.ActivityBase;
import com.dreamdigitizers.androidbaselibrary.views.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.R;

public class ActivityMain extends ActivityBase {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
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
        this.mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    }

    @Override
    protected void mapInformationToItems() {
        this.setSupportActionBar(this.mToolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic__drawer);
    }

    @Override
    protected ScreenBase getStartScreen() {
        return null;
    }

    @Override
    protected int getScreenContainerViewId() {
        return R.id.container;
    }
}
