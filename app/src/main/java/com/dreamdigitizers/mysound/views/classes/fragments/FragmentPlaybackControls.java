package com.dreamdigitizers.mysound.views.classes.fragments;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FragmentPlaybackControls extends FragmentBase {
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private TextView mLblTrackTitle;
    private TextView mLblDurationStart;
    private SeekBar mSkbTrackProgress;
    private TextView mLblDurationEnd;
    private ImageButton mBtnSkipToPrevious;
    private ImageButton mBtnPlayPause;
    private ProgressBar mPgbLoading;
    private ImageButton mBtnSkipToNext;
    private Drawable mPlayDrawable;
    private Drawable mPauseDrawable;

    private final Handler mHandler;
    private final ScheduledExecutorService mScheduledExecutorService;
    private ScheduledFuture<?> mScheduleFuture;
    private PlaybackStateCompat mLastPlaybackState;
    private IPlaybackControlListener mListener;

    public FragmentPlaybackControls() {
        this.mHandler = new Handler();
        this.mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopSeekBarUpdate();
        this.mScheduledExecutorService.shutdown();
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__playback_controls, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mLblTrackTitle = (TextView) pView.findViewById(R.id.lblTrackTitle);
        this.mLblDurationStart = (TextView) pView.findViewById(R.id.lblDurationStart);
        this.mSkbTrackProgress = (SeekBar) pView.findViewById(R.id.skbCurrentPosition);
        this.mLblDurationEnd = (TextView) pView.findViewById(R.id.lblDurationEnd);
        this.mBtnSkipToPrevious = (ImageButton) pView.findViewById(R.id.btnSkipToPrevious);
        this.mBtnPlayPause = (ImageButton) pView.findViewById(R.id.btnPlayPause);
        this.mPgbLoading = (ProgressBar) pView.findViewById(R.id.pgbLoading);
        this.mBtnSkipToNext = (ImageButton) pView.findViewById(R.id.btnSkipToNext);
        this.mPlayDrawable = ContextCompat.getDrawable(this.getContext(), R.drawable.ic__play);
        this.mPauseDrawable = ContextCompat.getDrawable(this.getContext(), R.drawable.ic__pause);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mLblTrackTitle.setSelected(true);

        this.mSkbTrackProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar pSeekBar, int pProgress, boolean pFromUser) {
                FragmentPlaybackControls.this.onTrackProgressSeekBarProgressChanged(pProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar pSeekBar) {
                FragmentPlaybackControls.this.onTrackProgressSeekBarStartTrackingTouch();
            }

            @Override
            public void onStopTrackingTouch(SeekBar pSeekBar) {
                FragmentPlaybackControls.this.onTrackProgressSeekBarStopTrackingTouch();
            }
        });

        this.mBtnSkipToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                FragmentPlaybackControls.this.onSkipToPreviousButtonClicked();
            }
        });

        this.mBtnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                FragmentPlaybackControls.this.onPlayPauseButtonClicked();
            }
        });

        this.mBtnSkipToNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                FragmentPlaybackControls.this.onSkipToNextButtonClicked();
            }
        });
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setPlaybackControlListener(IPlaybackControlListener pListener) {
        this.mListener = pListener;
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.updatePlaybackState(pPlaybackState);
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.updateMediaMetadata(pMediaMetadata);
    }

    private void updatePlaybackState(PlaybackStateCompat pPlaybackState) {
        this.mLastPlaybackState = pPlaybackState;

        this.mBtnSkipToPrevious.setVisibility((this.mLastPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) == 0 ? View.INVISIBLE : View.VISIBLE);
        this.mBtnSkipToNext.setVisibility((this.mLastPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) == 0 ? View.INVISIBLE : View.VISIBLE);
        switch (this.mLastPlaybackState.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                this.mBtnPlayPause.setVisibility(View.VISIBLE);
                this.mPgbLoading.setVisibility(View.INVISIBLE);
                this.mBtnPlayPause.setImageDrawable(this.mPauseDrawable);
                this.scheduleSeekBarUpdate();
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                this.mBtnPlayPause.setVisibility(View.VISIBLE);
                this.mPgbLoading.setVisibility(View.INVISIBLE);
                this.mBtnPlayPause.setImageDrawable(this.mPlayDrawable);
                this.stopSeekBarUpdate();
                break;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
            case PlaybackStateCompat.STATE_ERROR:
                this.mBtnPlayPause.setVisibility(View.VISIBLE);
                this.mPgbLoading.setVisibility(View.INVISIBLE);
                this.mBtnPlayPause.setImageDrawable(this.mPlayDrawable);
                this.stopSeekBarUpdate();
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                this.mBtnPlayPause.setVisibility(View.INVISIBLE);
                this.mPgbLoading.setVisibility(View.VISIBLE);
                this.stopSeekBarUpdate();
                break;
            default:
                break;
        }
    }

    private void updateMediaMetadata(MediaMetadataCompat pMediaMetadata) {
        Track track = (Track) pMediaMetadata.getBundle().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
        int duration = track.getDuration();

        this.mLblTrackTitle.setText(track.getTitle());
        this.mSkbTrackProgress.setMax(duration);
        this.mLblDurationEnd.setText(DateUtils.formatElapsedTime(duration / 1000));
    }

    private void scheduleSeekBarUpdate() {
        this.stopSeekBarUpdate();
        if (!this.mScheduledExecutorService.isShutdown()) {
            this.mScheduleFuture = this.mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    FragmentPlaybackControls.this.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            FragmentPlaybackControls.this.updateSeekBarProgress();
                        }
                    });
                }
            }, FragmentPlaybackControls.PROGRESS_UPDATE_INITIAL_INTERVAL, FragmentPlaybackControls.PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopSeekBarUpdate() {
        if (this.mScheduleFuture != null) {
            this.mScheduleFuture.cancel(false);
        }
    }

    private void updateSeekBarProgress() {
        if (this.mLastPlaybackState == null) {
            return;
        }
        long currentPosition = this.mLastPlaybackState.getPosition();
        if (this.mLastPlaybackState.getState() != PlaybackStateCompat.STATE_PAUSED) {
            long deltaTime = SystemClock.elapsedRealtime() - this.mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) deltaTime * this.mLastPlaybackState.getPlaybackSpeed();
        }
        this.mSkbTrackProgress.setProgress((int) currentPosition);
    }

    private void onTrackProgressSeekBarProgressChanged(int pProgress) {
        this.mLblDurationStart.setText(DateUtils.formatElapsedTime(pProgress / 1000));
    }

    private void onTrackProgressSeekBarStartTrackingTouch() {
        this.stopSeekBarUpdate();
    }

    private void onTrackProgressSeekBarStopTrackingTouch() {
        if (this.mListener != null) {
            this.mListener.seekTo(this.mSkbTrackProgress.getProgress());
        }
        this.scheduleSeekBarUpdate();
    }

    private void onSkipToPreviousButtonClicked() {
        if (this.mListener != null) {
            this.mListener.skipToPrevious();
        }
    }

    private void onPlayPauseButtonClicked() {
        if (this.mListener != null) {
            if (this.mBtnPlayPause.getDrawable() == this.mPauseDrawable) {
                this.mListener.pause();
                this.scheduleSeekBarUpdate();
            } else if (this.mBtnPlayPause.getDrawable() == this.mPlayDrawable) {
                this.mListener.play();
                this.stopSeekBarUpdate();
            }
        }
    }

    private void onSkipToNextButtonClicked() {
        if (this.mListener != null) {
            this.mListener.skipToNext();
        }
    }

    public interface IPlaybackControlListener {
        void skipToPrevious();
        void play();
        void pause();
        void skipToNext();
        void seekTo(int pPosition);
    }
}
