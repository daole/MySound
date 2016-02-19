package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.core.IApi;
import com.dreamdigitizers.androidsoundcloudapi.models.Collection;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

class PresenterPlayback extends PresenterRx<IViewPlayback> implements IPresenterPlayback {
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
    public void tracks(final UtilsDialog.IRetryAction pRetryAction) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = Observable.just(Share.getMe())
                .flatMap(new Func1<Me, Observable<Me>>() {
                    @Override
                    public Observable<Me> call(Me pMe) {
                        if (pMe != null) {
                            return Observable.just(pMe);
                        } else {
                            return api.meRx(Share.getAccessToken());
                        }
                    }
                })
                .flatMap(new Func1<Me, Observable<List<Track>>>() {
                    @Override
                    public Observable<List<Track>> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.tracksRx();
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Track>>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(List<Track> pTracks) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxSoundsNext(pTracks);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void tracks(final UtilsDialog.IRetryAction pRetryAction, final int pLinkedPartitioning, final int pLimit, final int pOffset) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = Observable.just(Share.getMe())
                .flatMap(new Func1<Me, Observable<Me>>() {
                    @Override
                    public Observable<Me> call(Me pMe) {
                        if (pMe != null) {
                            return Observable.just(pMe);
                        } else {
                            return api.meRx(Share.getAccessToken());
                        }
                    }
                })
                .flatMap(new Func1<Me, Observable<Collection>>() {
                    @Override
                    public Observable<Collection> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.tracksRx(pLinkedPartitioning, pLimit, pOffset);
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Collection>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Collection pCollection) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxSoundsNext(pCollection);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void tracks(final UtilsDialog.IRetryAction pRetryAction, final int pLinkedPartitioning, final int pLimit, final int pOffset, final String pQ) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = Observable.just(Share.getMe())
                .flatMap(new Func1<Me, Observable<Me>>() {
                    @Override
                    public Observable<Me> call(Me pMe) {
                        if (pMe != null) {
                            return Observable.just(pMe);
                        } else {
                            return api.meRx(Share.getAccessToken());
                        }
                    }
                })
                .flatMap(new Func1<Me, Observable<Collection>>() {
                    @Override
                    public Observable<Collection> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.tracksRx(pLinkedPartitioning, pLimit, pOffset, pQ);
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Collection>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Collection pCollection) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxSoundsSearchNext(pCollection);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void userFavorites(final UtilsDialog.IRetryAction pRetryAction) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = Observable.just(Share.getMe())
                .flatMap(new Func1<Me, Observable<Me>>() {
                    @Override
                    public Observable<Me> call(Me pMe) {
                        if (pMe != null) {
                            return Observable.just(pMe);
                        } else {
                            return api.meRx(Share.getAccessToken());
                        }
                    }
                })
                .flatMap(new Func1<Me, Observable<List<Track>>>() {
                    @Override
                    public Observable<List<Track>> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.userFavoritesRx(pMe.getId());
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Track>>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(List<Track> pTracks) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxFavoritesNext(pTracks);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void userFavorites(final UtilsDialog.IRetryAction pRetryAction, final int pLinkedPartitioning, final int pLimit, final String pOffset) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = Observable.just(Share.getMe())
                .flatMap(new Func1<Me, Observable<Me>>() {
                    @Override
                    public Observable<Me> call(Me pMe) {
                        if (pMe != null) {
                            return Observable.just(pMe);
                        } else {
                            return api.meRx(Share.getAccessToken());
                        }
                    }
                })
                .flatMap(new Func1<Me, Observable<Collection>>() {
                    @Override
                    public Observable<Collection> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.userFavoritesRx(pMe.getId(), pLinkedPartitioning, pLimit, pOffset);
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Collection>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Collection pCollection) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxFavoritesNext(pCollection);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void playlists(final UtilsDialog.IRetryAction pRetryAction) {

    }

    @Override
    public void playlist(final UtilsDialog.IRetryAction pRetryAction) {

    }

    @Override
    public void favorite(final UtilsDialog.IRetryAction pRetryAction, final Track pTrack) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = api.favoriteRx(pTrack.getId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Void pVoid) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxFavoriteNext(pTrack);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void unfavorite(final UtilsDialog.IRetryAction pRetryAction, final Track pTrack) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = api.unfavoriteRx(pTrack.getId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Void pVoid) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxUnfavoriteNext(pTrack);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    private void unsubscribe() {
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = null;
        }
    }
}
