package com.dreamdigitizers.mysound.views.classes.fragments;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.support.MediaItemAdapter;

import java.util.List;

public abstract class FragmentMediaItems extends FragmentBase {
    private static final String ERROR_MESSAGE__MISSING_RECYCLER_VIEW = "Missing RecyclerView with id \"lstMediaItems\" in layout.";
    private static final String ERROR_MESSAGE__MISSING_LOAD_MORE_PROGRESS_BAR = "Missing ProgressBar with id \"pgbLoading\" in layout.";

    protected static final int VISIBLE_THRESHOLD = 0;

    protected RecyclerView mLstMediaItems;
    protected ProgressBar mPgbLoading;

    protected LinearLayoutManager mLinearLayoutManager;
    protected MediaItemAdapter mMediaItemAdapter;

    protected IOnScrollEndListener mListener;

    protected boolean mIsLoading;
    protected int mPreviousTotalItemCount;

    public FragmentMediaItems() {
        this.mIsLoading = true;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mLstMediaItems = (RecyclerView) pView.findViewById(R.id.lstMediaItems);
        if (this.mLstMediaItems == null) {
            throw new NullPointerException(FragmentMediaItems.ERROR_MESSAGE__MISSING_RECYCLER_VIEW);
        }
        this.mPgbLoading = (ProgressBar) pView.findViewById(R.id.pgbLoading);
        if (this.mPgbLoading == null) {
            throw new NullPointerException(FragmentMediaItems.ERROR_MESSAGE__MISSING_LOAD_MORE_PROGRESS_BAR);
        }
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.mMediaItemAdapter = this.createAdapter();
        this.mLstMediaItems.setLayoutManager(this.mLinearLayoutManager);
        this.mLstMediaItems.setAdapter(this.mMediaItemAdapter);
        this.mLstMediaItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView pRecyclerView, int pDx, int pDy) {
                FragmentMediaItems.this.onTrackListScrolled();
            }
        });
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setOnScrollEndListener(IOnScrollEndListener pListener) {
        this.mListener = pListener;
    }

    public void setOnItemClickListener(MediaItemAdapter.IOnItemClickListener pListener) {
        this.mMediaItemAdapter.setOnItemClickListener(pListener);
    }

    public void clearMediaItems() {
        this.mPreviousTotalItemCount = 0;
        this.mMediaItemAdapter.clearMediaItems();
    }

    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        this.mMediaItemAdapter.addMediaItems(pMediaItems, pIsAddToTop);
    }

    public void showLoadMoreProgress() {
        this.mPgbLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoadMoreProgress() {
        this.mPgbLoading.setVisibility(View.GONE);
    }

    protected void onTrackListScrolled() {
        int visibleItemCount = this.mLinearLayoutManager.getChildCount();
        int firstVisibleItem = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        int totalItemCount = this.mLinearLayoutManager.getItemCount();

        if (this.mIsLoading) {
            if (totalItemCount > this.mPreviousTotalItemCount) {
                this.mIsLoading = false;
                this.mPreviousTotalItemCount = totalItemCount;
            }
        }

        if (!this.mIsLoading && totalItemCount - visibleItemCount <= firstVisibleItem + FragmentMediaItems.VISIBLE_THRESHOLD) {
            this.mIsLoading = true;
            if (this.mListener != null) {
                this.mListener.onScrollEnd();
            }
        }
    }

    protected abstract MediaItemAdapter createAdapter();

    public interface IOnScrollEndListener {
        void onScrollEnd();
    }
}
