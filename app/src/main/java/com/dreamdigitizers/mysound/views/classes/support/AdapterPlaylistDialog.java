package com.dreamdigitizers.mysound.views.classes.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPlaylistDialog extends RecyclerView.Adapter<AdapterPlaylistDialog.DialogPlaylistViewHolder> {
    private Context mContext;
    private Track mTrack;
    private MediaBrowserCompat.MediaItem mMediaItemTrack;
    private List<MediaBrowserCompat.MediaItem> mPlaylists;

    private IOnAddRemoveButtonClickListener mListener;

    public AdapterPlaylistDialog(Context pContext, MediaBrowserCompat.MediaItem pTrack, List<MediaBrowserCompat.MediaItem> pPlaylists, IOnAddRemoveButtonClickListener pListener) {
        this.mContext = pContext;
        this.mMediaItemTrack = pTrack;
        this.mTrack = (Track) pTrack.getDescription().getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__TRACK);;
        this.mPlaylists = pPlaylists;
        this.mListener = pListener;
    }

    @Override
    public DialogPlaylistViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__playlist_dialog, pParent, false);
        return new DialogPlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DialogPlaylistViewHolder pHolder, int pPosition) {
        MediaBrowserCompat.MediaItem mediaItem = this.mPlaylists.get(pPosition);
        MediaDescriptionCompat mediaDescription = mediaItem.getDescription();
        Playlist playlist = (Playlist) mediaDescription.getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            pHolder.mImgAvatar.setImageBitmap(bitmap);
        } else {
            UtilsImage.loadBitmap(this.mContext, playlist.getArtworkUrl(), R.drawable.ic__my_music, pHolder.mImgAvatar);
        }

        pHolder.mLblTitle.setText(playlist.getTitle());

        boolean isAdded = false;
        for (Track track : playlist.getTracks()) {
            if (track.getId() == this.mTrack.getId()) {
                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            pHolder.mBtnAddToRemoveFromPlaylist.setText(R.string.btn__remove);
        } else {
            pHolder.mBtnAddToRemoveFromPlaylist.setText(R.string.btn__add);
        }

        pHolder.mIsAdded = isAdded;
        pHolder.mPlaylist = mediaItem;
    }

    @Override
    public int getItemCount() {
        return this.mPlaylists.size();
    }

    public void setOnAddRemoveButtonClickListener(IOnAddRemoveButtonClickListener pListener) {
        this.mListener = pListener;
    }

    public void onAddToRemoveFromPlaylistResult() {
        this.notifyDataSetChanged();
    }

    public class DialogPlaylistViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImgAvatar;
        private TextView mLblTitle;
        private Button mBtnAddToRemoveFromPlaylist;
        private boolean mIsAdded;
        private MediaBrowserCompat.MediaItem mPlaylist;

        public DialogPlaylistViewHolder(View pItemView) {
            super(pItemView);
            this.mImgAvatar = (CircleImageView) pItemView.findViewById(R.id.imgAvatar);
            this.mLblTitle = (TextView) pItemView.findViewById(R.id.lblTitle);
            this.mBtnAddToRemoveFromPlaylist = (Button) pItemView.findViewById(R.id.btnAddToRemoveFromPlaylist);
            this.mBtnAddToRemoveFromPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    DialogPlaylistViewHolder.this.buttonAddToRemoveFromPlaylistClicked();
                }
            });
        }

        private void buttonAddToRemoveFromPlaylistClicked() {
            if (AdapterPlaylistDialog.this.mListener != null) {
                AdapterPlaylistDialog.this.mListener.onAddRemoveButtonClicked(AdapterPlaylistDialog.this.mMediaItemTrack, this.mPlaylist, !this.mIsAdded);
            }
        }
    }

    public interface IOnAddRemoveButtonClickListener {
        void onAddRemoveButtonClicked(MediaBrowserCompat.MediaItem pTrack, MediaBrowserCompat.MediaItem pPlaylist, boolean pIsAdd);
    }
}
