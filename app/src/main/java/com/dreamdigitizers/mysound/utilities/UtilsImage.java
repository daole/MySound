package com.dreamdigitizers.mysound.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class UtilsImage {
    public static void loadBitmap(final Context pContext, final String pUrl, final int pDefaultDrawableResourceId, final ImageView pImageView) {
        Picasso.with(pContext)
                .load(pUrl)
                .placeholder(ContextCompat.getDrawable(pContext, pDefaultDrawableResourceId))
                .error(ContextCompat.getDrawable(pContext, pDefaultDrawableResourceId))
                .into(pImageView);
    }

    public static void loadBitmap(final Context pContext, final String pUrl, final int pDefaultDrawableResourceId, final IOnImageLoadedListener pListener) {
        Target target = new Target() {
            @Override
            public void onPrepareLoad(Drawable pPlaceHolderDrawable) {
                if (pListener != null) {
                    pListener.onPrepareLoad(pPlaceHolderDrawable);
                }
            }

            @Override
            public void onBitmapLoaded(Bitmap pBitmap, Picasso.LoadedFrom pFrom) {
                if (pListener != null) {
                    pListener.onBitmapLoaded(pBitmap);
                }
            }

            @Override
            public void onBitmapFailed(Drawable pPlaceHolderDrawable) {
                if (pListener != null) {
                    pListener.onBitmapFailed(pPlaceHolderDrawable);
                }
            }
        };

        Picasso.with(pContext)
                .load(pUrl)
                .placeholder(ContextCompat.getDrawable(pContext, pDefaultDrawableResourceId))
                .error(ContextCompat.getDrawable(pContext, pDefaultDrawableResourceId))
                .into(target);
    }

    public interface IOnImageLoadedListener {
        void onPrepareLoad(Drawable pPlaceHolderDrawable);
        void onBitmapLoaded(Bitmap pBitmap);
        void onBitmapFailed(Drawable pPlaceHolderDrawable);
    }
}
