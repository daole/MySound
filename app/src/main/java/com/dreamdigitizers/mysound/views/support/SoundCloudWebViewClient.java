package com.dreamdigitizers.mysound.views.support;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dreamdigitizers.androidbaselibrary.utils.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.utils.UtilsString;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;

import java.net.URI;

public class SoundCloudWebViewClient extends WebViewClient {
    private Activity mActivity;
    private IOnLoginCompleteListener mListener;

    public SoundCloudWebViewClient(Activity pActivity, IOnLoginCompleteListener pListener) {
        this.mActivity = pActivity;
        this.mListener = pListener;
    }

    @Override
    public void onPageStarted(WebView pWebView, String pUrl, Bitmap pFavicon){
        UtilsDialog.displayNetworkProgressDialog(
                this.mActivity,
                this.mActivity.getString(R.string.title__dialog),
                this.mActivity.getString(R.string.message__loading));
    }

    @Override
    public void onPageFinished(WebView pWebView, String pUrl) {
        UtilsDialog.closeNetworkProgressDialog();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView pWebView, String pUrl) {
        if (!UtilsString.isEmpty(pUrl) && pUrl.startsWith(Constants.SOUNDCLOUD__PROTOCOL_SCHEME)) {
            String accessToken = this.retrieveAccessToken(pUrl);
            if(this.mListener != null) {
                this.mListener.onLoginComplete(accessToken);
            }
            return true;
        }
        return false;
    }

    private String retrieveAccessToken(String pUrl) {
        String accessToken = null;

        URI uri = URI.create(pUrl);
        String fragment = uri.getFragment();
        int accessTokenKeyIndex = fragment.indexOf(Constants.SOUNDCLOUD__ACCESS_TOKEN_KEY);
        if (accessTokenKeyIndex >= 0) {
            accessToken = fragment.substring(accessTokenKeyIndex + Constants.SOUNDCLOUD__ACCESS_TOKEN_KEY.length());
            int ampersandIndex = accessToken.indexOf(Constants.AMPERSAND);
            if (ampersandIndex >= 0) {
                accessToken = accessToken.substring(0, ampersandIndex);
            }
        }

        return accessToken;
    }

    public interface IOnLoginCompleteListener {
        void onLoginComplete(String pAccessToken);
    }
}
