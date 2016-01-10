package com.dreamdigitizers.mysound.views.classes.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaPlayer;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityMain;

public class ServicePlayback extends ServiceMediaPlayer {
    private static final long SUPPORTED_PLAYBACK_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_REWIND
                    | PlaybackStateCompat.ACTION_FAST_FORWARD;

    private IBinder mBinder;
    private NotificationCompat.Builder mNotificationBuilder;

    @Override
    protected void initialize() {
        this.mBinder = new ServiceBinder();
        this.mNotificationBuilder = new NotificationCompat.Builder(this);
    }

    @Override
    public IBinder onBind(Intent pIntent) {
        return this.mBinder;
    }

    @Override
    protected boolean isRemotePlayback() {
        return true;
    }

    @Override
    protected long getProtectedPlaybackActions() {
        return ServicePlayback.SUPPORTED_PLAYBACK_ACTIONS;
    }

    @Override
    protected Notification createNotification(Track pTrack) {
        this.mNotificationBuilder.setTicker(pTrack.getTitle());
        this.mNotificationBuilder.setSmallIcon(R.drawable.ic__notification);
        this.mNotificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic__launcher));
        this.mNotificationBuilder.setContentText(pTrack.getTitle());
        this.mNotificationBuilder.setSubText(pTrack.getArtist());
        this.mNotificationBuilder.setWhen(0);
        this.mNotificationBuilder.setShowWhen(false);
        this.mNotificationBuilder.setContentIntent(this.createNotificationIntent());
        this.mNotificationBuilder.setOngoing(true);
        this.mNotificationBuilder.setStyle(new NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(new int[] {0, 1, 2})
                .setShowCancelButton(true)
                .setCancelButtonIntent(this.getStopPendingIntent())
                .setMediaSession(this.getMediaSession().getSessionToken()));
        this.mNotificationBuilder.mActions.clear();
        this.mNotificationBuilder.addAction(R.drawable.ic__skip_to_previous, this.getString(R.string.action__skip_to_previous), this.getSkipToPreviousPendingIntent());
        if(this.getState() == ServiceMediaPlayer.STATE__PLAYING) {
            this.mNotificationBuilder.addAction(R.drawable.ic__pause, this.getString(R.string.action__pause), this.getPausePendingIntent());
        } else {
            this.mNotificationBuilder.addAction(R.drawable.ic__play, this.getString(R.string.action__play), this.getPlayPendingIntent());
        }
        this.mNotificationBuilder.addAction(R.drawable.ic__skip_to_next, this.getString(R.string.action__skip_to_next), this.getSkipToNextPendingIntent());
        return this.mNotificationBuilder.build();
    }

    private PendingIntent createNotificationIntent() {
        return PendingIntent.getActivity(this, ServiceMediaPlayer.REQUEST_CODE, new Intent(this, ActivityMain.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public class ServiceBinder extends Binder {
        public ServicePlayback getService() {
            return ServicePlayback.this;
        }
    }
}
