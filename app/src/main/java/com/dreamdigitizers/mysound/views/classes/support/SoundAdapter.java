package com.dreamdigitizers.mysound.views.classes.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.TrackViewHolder> {
    private Context mContext;
    private List<MediaBrowserCompat.MediaItem> mMediaItems;

    public SoundAdapter(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public SoundAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mContext = pContext;
        this.mMediaItems = pMediaItems;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__sound, pParent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder pHolder, int pPosition) {
        MediaDescriptionCompat mediaDescription = this.mMediaItems.get(pPosition).getDescription();
        Track track = (Track) mediaDescription.getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);
        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            pHolder.mImgAvatar.setImageBitmap(bitmap);
        } else {
            UtilsImage.loadBitmap(this.mContext, track.getArtworkUrl(), R.drawable.ic__my_music, pHolder.mImgAvatar);
        }
        pHolder.mLblUsername.setText(track.getUser().getUsername());
        String duration = new SimpleDateFormat(Constants.FORMAT__DURATION).format(new Date(track.getDuration()));
        pHolder.mLblDuration.setText(duration);
        pHolder.mLblTitle.setText(track.getTitle());
        pHolder.mLblPlaybackCount.setText(Integer.toString(track.getPlaybackCount()));
    }

    @Override
    public int getItemCount() {
        return this.mMediaItems.size();
    }

    public void setMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mMediaItems = pMediaItems;
        this.notifyDataSetChanged();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mImgAvatar;
        TextView mLblUsername;
        TextView mLblDuration;
        TextView mLblTitle;
        TextView mLblPlaybackCount;

        public TrackViewHolder(View pItemView) {
            super(pItemView);
            this.mImgAvatar = (CircleImageView) pItemView.findViewById(R.id.imgAvatar);
            this.mLblUsername = (TextView) pItemView.findViewById(R.id.lblUsername);
            this.mLblDuration = (TextView) pItemView.findViewById(R.id.lblDuration);
            this.mLblTitle = (TextView) pItemView.findViewById(R.id.lblTitle);
            this.mLblPlaybackCount = (TextView) pItemView.findViewById(R.id.lblPlaybackCount);
        }
    }
}
