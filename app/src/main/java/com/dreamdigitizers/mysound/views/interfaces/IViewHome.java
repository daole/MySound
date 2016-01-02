package com.dreamdigitizers.mysound.views.interfaces;

import com.dreamdigitizers.androidbaselibrary.views.interfaces.IView;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;

public interface IViewHome extends IView {
    void dispatchMe(Me pMe);
}
