package com.dreamdigitizers.mysound.views.interfaces;

import com.dreamdigitizers.androidsoundcloudapi.models.Me;

public interface IViewHome extends IViewRx, IViewTracks {
    void onRxNext(Me pMe);
}
