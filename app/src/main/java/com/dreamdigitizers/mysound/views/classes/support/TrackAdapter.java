package com.dreamdigitizers.mysound.views.classes.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends PlaylistAdapter {
    private PlaybackStateCompat mPlaybackState;
    private MediaMetadataCompat mMediaMetadata;

    public TrackAdapter(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public TrackAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this(pContext, pMediaItems, null);
    }

    public TrackAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems, MediaItemAdapter.IOnItemClickListener pListener) {
        super(pContext, pMediaItems, pListener);
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__track, pParent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder pHolder, int pPosition) {
        MediaBrowserCompat.MediaItem mediaItem = this.mMediaItems.get(pPosition);
        MediaDescriptionCompat mediaDescription = mediaItem.getDescription();
        Track track = (Track) mediaDescription.getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            pHolder.mImgAvatar.setImageBitmap(bitmap);
        } else {
            UtilsImage.loadBitmap(this.mContext, track.getArtworkUrl(), R.drawable.ic__my_music, pHolder.mImgAvatar);
        }

        pHolder.mLblUsername.setText(track.getUser().getUsername());
        pHolder.mLblDuration.setText(DateUtils.formatElapsedTime(track.getDuration() / 1000));
        pHolder.mLblTitle.setText(track.getTitle());
        ((TrackViewHolder) pHolder).mLblPlaybackCount.setText(NumberFormat.getInstance().format(track.getPlaybackCount()));
        ((TrackViewHolder) pHolder).mImgFavorite.setVisibility(track.getUserFavorite() ? View.VISIBLE : View.GONE);
        pHolder.mMediaItem = mediaItem;

        Drawable drawable = null;
        if (this.mMediaMetadata != null) {
            Track currentTrack = (Track) this.mMediaMetadata.getBundle().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            if (currentTrack == null && Build.VERSION.SDK_INT >= 21) {
                currentTrack = Share.getCurrentTrack();
            }
            if (currentTrack != null && currentTrack.getId() == track.getId()) {
                int state = PlaybackStateCompat.STATE_PAUSED;
                if (this.mPlaybackState != null) {
                    switch (this.mPlaybackState.getState()) {
                        case PlaybackStateCompat.STATE_PLAYING:
                            state = PlaybackStateCompat.STATE_PLAYING;
                            break;
                        default:
                            break;
                    }
                }

                AnimationDrawable animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(this.mContext, R.drawable.ic__equalizer);
                drawable = animationDrawable;
                ((TrackViewHolder) pHolder).mImgPlaybackCount.setImageDrawable(animationDrawable);
                if (state == PlaybackStateCompat.STATE_PLAYING) {
                    animationDrawable.start();
                }
            }
        }
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(this.mContext, R.drawable.ic__play);
            ((TrackViewHolder) pHolder).mImgPlaybackCount.setImageDrawable(drawable);
        }
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mPlaybackState = pPlaybackState;
        this.notifyDataSetChanged();
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mMediaMetadata = pMediaMetadata;
        this.notifyDataSetChanged();
    }

    protected class TrackViewHolder extends PlaylistAdapter.PlaylistViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        protected ImageView mImgPlaybackCount;
        protected TextView mLblPlaybackCount;
        protected ImageView mImgFavorite;

        public TrackViewHolder(View pItemView) {
            super(pItemView);
            this.mImgPlaybackCount = (ImageView) pItemView.findViewById(R.id.imgPlaybackCount);
            this.mLblPlaybackCount = (TextView) pItemView.findViewById(R.id.lblPlaybackCount);
            this.mImgFavorite = (ImageView) pItemView.findViewById(R.id.imgFavorite);
        }

        @Override
        public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenu.ContextMenuInfo pMenuInfo) {
            MenuInflater menuInflater = new MenuInflater(TrackAdapter.this.mContext);
            menuInflater.inflate(R.menu.menu__context_track_list, pContextMenu);

            MediaDescriptionCompat mediaDescription = this.mMediaItem.getDescription();
            Track track = (Track) mediaDescription.getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            MenuItem menuItem = pContextMenu.getItem(0);
            menuItem.setOnMenuItemClickListener(this);
            if (track.getUserFavorite()) {
                menuItem.setTitle(TrackAdapter.this.mContext.getString(R.string.context_menu_item__unfavorite));
            } else {
                menuItem.setTitle(TrackAdapter.this.mContext.getString(R.string.context_menu_item__favorite));
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem pMenuItem) {
            int id = pMenuItem.getItemId();
            switch (id) {
                case R.id.context_menu_item__favorite:
                    if(TrackAdapter.this.mListener != null && TrackAdapter.this.mListener instanceof IOnItemClickListener) {
                        ((IOnItemClickListener) TrackAdapter.this.mListener).onFavoriteContextMenuItemClicked(this.mMediaItem);
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface IOnItemClickListener extends MediaItemAdapter.IOnItemClickListener {
        void onFavoriteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
    }
}
