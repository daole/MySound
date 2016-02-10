package com.dreamdigitizers.mysound.views.classes.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private Context mContext;
    private List<MediaBrowserCompat.MediaItem> mMediaItems;
    private PlaybackStateCompat mPlaybackState;
    private MediaMetadataCompat mMediaMetadata;

    private IOnItemClickListener mListener;

    public TrackAdapter(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public TrackAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this(pContext, pMediaItems, null);
    }

    public TrackAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems, IOnItemClickListener pListener) {
        this.mContext = pContext;
        this.mMediaItems = pMediaItems;
        this.mListener = pListener;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__track, pParent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder pHolder, int pPosition) {
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
        pHolder.mLblPlaybackCount.setText(Integer.toString(track.getPlaybackCount()));
        pHolder.mMediaItem = mediaItem;

        Drawable drawable = null;
        if (this.mMediaMetadata != null) {
            Track currentTrack = (Track) this.mMediaMetadata.getBundle().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
            if (currentTrack.getId() == track.getId()) {
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
                pHolder.mImgPlaybackCount.setImageDrawable(animationDrawable);
                if (state == PlaybackStateCompat.STATE_PLAYING) {
                    animationDrawable.start();
                }
            }
        }
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(this.mContext, R.drawable.ic__play);
            pHolder.mImgPlaybackCount.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return this.mMediaItems.size();
    }

    public void setOnItemClickListener(IOnItemClickListener pListener) {
        this.mListener = pListener;
    }

    public void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mMediaItems = pMediaItems;
        this.notifyDataSetChanged();
    }

    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mMediaItems.addAll(pMediaItems);
        this.notifyDataSetChanged();
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mPlaybackState = pPlaybackState;
        this.notifyDataSetChanged();
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mMediaMetadata = pMediaMetadata;
        this.notifyDataSetChanged();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mImgAvatar;
        TextView mLblUsername;
        TextView mLblDuration;
        TextView mLblTitle;
        ImageView mImgPlaybackCount;
        TextView mLblPlaybackCount;
        MediaBrowserCompat.MediaItem mMediaItem;

        public TrackViewHolder(View pItemView) {
            super(pItemView);
            this.mImgAvatar = (CircleImageView) pItemView.findViewById(R.id.imgAvatar);
            this.mLblUsername = (TextView) pItemView.findViewById(R.id.lblUsername);
            this.mLblDuration = (TextView) pItemView.findViewById(R.id.lblDuration);
            this.mLblTitle = (TextView) pItemView.findViewById(R.id.lblTitle);
            this.mImgPlaybackCount = (ImageView) pItemView.findViewById(R.id.imgPlaybackCount);
            this.mLblPlaybackCount = (TextView) pItemView.findViewById(R.id.lblPlaybackCount);
            pItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    TrackViewHolder.this.clicked();
                }
            });
        }

        private void clicked() {
            if(TrackAdapter.this.mListener != null) {
                TrackAdapter.this.mListener.onItemClicked(this.mMediaItem);
            }
        }
    }

    public interface IOnItemClickListener {
        void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
    }
}
