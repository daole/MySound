package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.Presenter;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PresenterPlayback extends Presenter<IViewPlayback> implements IPresenterPlayback {
    private Subscription mSubscription;

    public PresenterPlayback(IViewPlayback pView) {
        super(pView);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unsubscribe();
    }

    @Override
    public void tracks() {
        this.unsubscribe();
        this.mSubscription = ApiFactory.getApiInstance()
                .tracksRx()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Track>>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable pError) {
                        pError.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Track> pTracks) {
                    }
                });
    }

    @Override
    public void userFavorites(int pId) {
        this.unsubscribe();
        this.mSubscription = ApiFactory.getApiInstance()
                .userFavoritesRx(pId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Track>>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable pError) {
                        pError.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Track> pTracks) {
                    }
                });
    }

    @Override
    public void playlists(int pId) {

    }

    @Override
    public void playlist(int pId) {

    }

    private void unsubscribe() {
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }
}
