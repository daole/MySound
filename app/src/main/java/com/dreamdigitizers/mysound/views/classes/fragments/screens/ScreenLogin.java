package com.dreamdigitizers.mysound.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.dreamdigitizers.mysound.presenters.classes.PresenterFactory;
import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.R;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterLogin;
import com.dreamdigitizers.mysound.views.interfaces.IViewLogin;
import com.dreamdigitizers.mysound.views.classes.support.SoundCloudWebViewClient;

public class ScreenLogin extends ScreenBase<IPresenterLogin> implements IViewLogin, SoundCloudWebViewClient.IOnLoginCompleteListener {
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
        this.mWbvLogin.setWebViewClient(new SoundCloudWebViewClient(this.getActivity(), this));
        this.mWbvLogin.loadUrl(Constants.SOUNDCLOUD_API__CONNECT);
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    @Override
    public void onLoginComplete(String pAccessToken) {
        this.mPresenter.saveAccessToken(pAccessToken);
        Toast.makeText(this.getContext(), pAccessToken, Toast.LENGTH_LONG).show();
    }
}
