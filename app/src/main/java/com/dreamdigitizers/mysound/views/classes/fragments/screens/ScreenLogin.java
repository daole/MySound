package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.support.SoundCloudWebViewClient;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterLogin;
import com.dreamdigitizers.mysound.views.classes.activities.ActivityMain;
import com.dreamdigitizers.mysound.views.interfaces.IViewLogin;

public class ScreenLogin extends ScreenBase<IPresenterLogin> implements IViewLogin, SoundCloudWebViewClient.IOnLoginActionsListener {
    private WebView mWbvLogin;

    @Override
    protected IPresenterLogin createPresenter() {
        return (IPresenterLogin) PresenterFactory.createPresenter(IPresenterLogin.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__login, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mWbvLogin = (WebView) pView.findViewById(R.id.wbvLogin);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mWbvLogin.getSettings().setJavaScriptEnabled(true);
        this.mWbvLogin.setWebViewClient(new SoundCloudWebViewClient(
                this.getActivity(),
                Constants.SOUNDCLOUD__PROTOCOL_SCHEME,
                this));
        this.mWbvLogin.loadUrl(ApiFactory.getConnectionString(
                Constants.SOUNDCLOUD__CLIENT_ID,
                Constants.SOUNDCLOUD__REDIRECT_URI,
                Constants.SOUNDCLOUD__RESPONSE_TYPE,
                Constants.SOUNDCLOUD__SCOPE,
                Constants.SOUNDCLOUD__DISPLAY,
                Constants.SOUNDCLOUD__STATE));
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    public void onPageStarted(WebView pWebView, String pUrl, Bitmap pFavicon) {
        UtilsDialog.showNetworkProgressDialog(
                this.getActivity(),
                this.getString(R.string.title__dialog),
                this.getString(R.string.message__loading___));
    }

    @Override
    public void onPageFinished(WebView pWebView, String pUrl) {
        UtilsDialog.hideNetworkProgressDialog();
    }

    @Override
    public void onLoginComplete(String pAccessToken) {
        this.mPresenter.saveAccessToken(pAccessToken);
        Share.setAccessToken(pAccessToken);
        this.goToMainActivity();
    }

    private void goToMainActivity() {
        this.changeActivityAndFinish(ActivityMain.class);
    }
}
