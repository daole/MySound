package com.dreamdigitizers.mysound.views.classes.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.utilities.UtilsImage;
import com.dreamdigitizers.mysound.views.classes.services.support.SoundCloudMetadataBuilder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaylistAdapter extends MediaItemAdapter<PlaylistAdapter.PlaylistViewHolder> {
    public PlaylistAdapter(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public PlaylistAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this(pContext, pMediaItems, null);
    }

    public PlaylistAdapter(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems, MediaItemAdapter.IOnItemClickListener pListener) {
        super(pContext, pMediaItems, pListener);
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__playlist, pParent, false);
        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder pHolder, int pPosition) {
        MediaBrowserCompat.MediaItem mediaItem = this.mMediaItems.get(pPosition);
        MediaDescriptionCompat mediaDescription = mediaItem.getDescription();
        Playlist playlist = (Playlist) mediaDescription.getExtras().getSerializable(SoundCloudMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            pHolder.mImgAvatar.setImageBitmap(bitmap);
        } else {
            UtilsImage.loadBitmap(this.mContext, playlist.getArtworkUrl(), R.drawable.ic__my_music, pHolder.mImgAvatar);
        }

        pHolder.mLblUsername.setText(playlist.getUser().getUsername());
        pHolder.mLblDuration.setText(DateUtils.formatElapsedTime(playlist.getDuration() / 1000));
        pHolder.mLblTitle.setText(playlist.getTitle());
        pHolder.mMediaItem = mediaItem;
    }

    protected class PlaylistViewHolder extends MediaItemAdapter.MediaItemViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        protected CircleImageView mImgAvatar;
        protected TextView mLblUsername;
        protected TextView mLblDuration;
        protected TextView mLblTitle;
        protected ImageButton mBtnContextMenu;

        public PlaylistViewHolder(View pItemView) {
            super(pItemView);
            this.mImgAvatar = (CircleImageView) pItemView.findViewById(R.id.imgAvatar);
            this.mLblUsername = (TextView) pItemView.findViewById(R.id.lblUsername);
            this.mLblDuration = (TextView) pItemView.findViewById(R.id.lblDuration);
            this.mLblTitle = (TextView) pItemView.findViewById(R.id.lblTitle);
            this.mBtnContextMenu = (ImageButton) pItemView.findViewById(R.id.btnContextMenu);
            this.mBtnContextMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    PlaylistViewHolder.this.contextMenuButtonClicked();
                }
            });
            this.mBtnContextMenu.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenu.ContextMenuInfo pMenuInfo) {
            MenuInflater menuInflater = new MenuInflater(PlaylistAdapter.this.mContext);
            menuInflater.inflate(R.menu.menu__context_playlist_list, pContextMenu);

            MenuItem menuItem = pContextMenu.getItem(0);
            menuItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem pMenuItem) {
            int id = pMenuItem.getItemId();
            switch (id) {
                case R.id.context_menu_item__delete:
                    if(PlaylistAdapter.this.mListener != null && PlaylistAdapter.this.mListener instanceof IOnItemClickListener) {
                        ((IOnItemClickListener) PlaylistAdapter.this.mListener).onDeleteContextMenuItemClicked(this.mMediaItem);
                    }
                    return true;
                default:
                    return false;
            }
        }

        protected void contextMenuButtonClicked() {
            ((Activity) PlaylistAdapter.this.mContext).openContextMenu(this.mBtnContextMenu);
        }
    }

    public interface IOnItemClickListener extends MediaItemAdapter.IOnItemClickListener {
        void onDeleteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
    }
}
