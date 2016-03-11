package com.dreamdigitizers.mysound.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidsoundcloudapi.core.ApiFactory;
import com.dreamdigitizers.androidsoundcloudapi.core.IApi;
import com.dreamdigitizers.androidsoundcloudapi.core.IApiV2;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlist;
import com.dreamdigitizers.androidsoundcloudapi.models.Playlists;
import com.dreamdigitizers.androidsoundcloudapi.models.Tracks;
import com.dreamdigitizers.androidsoundcloudapi.models.Me;
import com.dreamdigitizers.androidsoundcloudapi.models.Track;
import com.dreamdigitizers.androidsoundcloudapi.models.parameters.ParameterAddTrackToPlaylist;
import com.dreamdigitizers.androidsoundcloudapi.models.v2.Charts;
import com.dreamdigitizers.mysound.Constants;
import com.dreamdigitizers.mysound.Share;
import com.dreamdigitizers.mysound.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.mysound.views.interfaces.IViewPlayback;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
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
    public void charts(final UtilsDialog.IRetryAction pRetryAction, final int pLinkedPartitioning, final int pLimit, final int pOffset) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        final IApiV2 apiV2 = ApiFactory.getApiV2Instance();
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
                .flatMap(new Func1<Me, Observable<Charts>>() {
                    @Override
                    public Observable<Charts> call(Me pMe) {
                        Share.setMe(pMe);
                        return Observable.zip(
                                api.trackLikesRx(),
                                apiV2.chartsRx(pLinkedPartitioning, pLimit, pOffset, Constants.SOUNDCLOUD_PARAMETER__KIND_TOP, Constants.SOUNDCLOUD_PARAMETER__GENRE_ALL_MUSIC),
                                new Func2<List<Integer>, Charts, Charts>() {
                                    @Override
                                    public Charts call(List<Integer> pTrackLikes, Charts pCharts) {
                                        for (Charts.Collection collection : pCharts.getCollection()) {
                                            Track track = collection.getTrack();
                                            track.setStreamUrl(track.getUri() + "/stream");
                                            if (pTrackLikes.contains(track.getId())) {
                                                track.setUserFavorite(true);
                                            }
                                        }
                                        return pCharts;
                                    }
                                }
                        );
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Charts>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Charts pCharts) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxChartsNext(pCharts);
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
                })
                .subscribeOn(Schedulers.io())
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
                .flatMap(new Func1<Me, Observable<Tracks>>() {
                    @Override
                    public Observable<Tracks> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.tracksRx(pLinkedPartitioning, pLimit, pOffset);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Tracks>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Tracks pTracks) {
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
                .flatMap(new Func1<Me, Observable<Tracks>>() {
                    @Override
                    public Observable<Tracks> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.tracksRx(pLinkedPartitioning, pLimit, pOffset, pQ);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Tracks>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Tracks pTracks) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxSoundsSearchNext(pTracks);
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

    /*
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
                })
                .subscribeOn(Schedulers.io())
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
    */

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
                .flatMap(new Func1<Me, Observable<Tracks>>() {
                    @Override
                    public Observable<Tracks> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.userFavoritesRx(pMe.getId(), pLinkedPartitioning, pLimit, pOffset);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Tracks>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Tracks pTracks) {
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
    public void userPlaylists(final UtilsDialog.IRetryAction pRetryAction) {
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
                .flatMap(new Func1<Me, Observable<List<Playlist>>>() {
                    @Override
                    public Observable<List<Playlist>> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.userPlaylistsRx(pMe.getId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Playlist>>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(List<Playlist> pPlaylists) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxPlaylistsNext(pPlaylists);
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
    public void userPlaylists(final UtilsDialog.IRetryAction pRetryAction, final int pLinkedPartitioning, final int pLimit, final int pOffset) {
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
                .flatMap(new Func1<Me, Observable<Playlists>>() {
                    @Override
                    public Observable<Playlists> call(Me pMe) {
                        Share.setMe(pMe);
                        return api.userPlaylistsRx(pMe.getId(), pLinkedPartitioning, pLimit, pOffset);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Playlists>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Playlists pPlaylists) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxPlaylistsNext(pPlaylists);
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

    /*@Override
    public void playlist(final UtilsDialog.IRetryAction pRetryAction, final int pLinkedPartitioning, final int pLimit, final int pOffset) {
    }*/

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

    @Override
    public void deletePlaylist(final UtilsDialog.IRetryAction pRetryAction, final Playlist pPlaylist) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = api.deletePlaylistRx(pPlaylist.getId())
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
                            view.onRxDeletePlaylistNext(pPlaylist);
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
    public void addToPlaylist(final UtilsDialog.IRetryAction pRetryAction, final Track pTrack, final Playlist pPlaylist) {
        this.unsubscribe();

        pPlaylist.getTracks().add(pTrack);
        List<ParameterAddTrackToPlaylist.Playlist.Track> parameterTracks = new ArrayList<>();
        for (Track track : pPlaylist.getTracks()) {
            ParameterAddTrackToPlaylist.Playlist.Track parameterTrack = new ParameterAddTrackToPlaylist.Playlist.Track();
            parameterTrack.setId(track.getId());
            parameterTracks.add(parameterTrack);
        }
        ParameterAddTrackToPlaylist.Playlist parameterPlaylist = new ParameterAddTrackToPlaylist.Playlist();
        parameterPlaylist.setTracks(parameterTracks);
        ParameterAddTrackToPlaylist parameter = new ParameterAddTrackToPlaylist();
        parameter.setPlaylist(parameterPlaylist);

        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = api.playlistRx(pPlaylist.getId(), parameter)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Playlist>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Playlist pPlaylist) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxAddToPlaylistNext(pTrack, pPlaylist);
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
    public void removeFromPlaylist(final UtilsDialog.IRetryAction pRetryAction, final Track pTrack, final Playlist pPlaylist) {
        this.unsubscribe();

        pPlaylist.getTracks().remove(pTrack);
        List<ParameterAddTrackToPlaylist.Playlist.Track> parameterTracks = new ArrayList<>();
        for (Track track : pPlaylist.getTracks()) {
            ParameterAddTrackToPlaylist.Playlist.Track parameterTrack = new ParameterAddTrackToPlaylist.Playlist.Track();
            parameterTrack.setId(track.getId());
            parameterTracks.add(parameterTrack);
        }
        ParameterAddTrackToPlaylist.Playlist parameterPlaylist = new ParameterAddTrackToPlaylist.Playlist();
        parameterPlaylist.setTracks(parameterTracks);
        ParameterAddTrackToPlaylist parameter = new ParameterAddTrackToPlaylist();
        parameter.setPlaylist(parameterPlaylist);

        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = api.playlistRx(pPlaylist.getId(), parameter)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Playlist>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Playlist pPlaylist) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxRemoveFromPlaylistNext(pTrack, pPlaylist);
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
    public void createPlaylist(final UtilsDialog.IRetryAction pRetryAction, final Track pTrack, final String pPlaylistTitle, final boolean pIsPublic) {
        this.unsubscribe();
        final IApi api = ApiFactory.getApiInstance();
        this.mSubscription = api.playlistsRx(
                pPlaylistTitle,
                pIsPublic ? Constants.SOUNDCLOUD_PARAMETER__PUBLIC : Constants.SOUNDCLOUD_PARAMETER__PRIVATE,
                Constants.SOUNDCLOUD_PARAMETER__UNDEFINED,
                Constants.SOUNDCLOUD_PARAMETER__PLAYLIST)
                .flatMap(new Func1<Playlist, Observable<Playlist>>() {
                    @Override
                    public Observable<Playlist> call(Playlist pPlaylist) {
                        pPlaylist.getTracks().add(pTrack);
                        List<ParameterAddTrackToPlaylist.Playlist.Track> parameterTracks = new ArrayList<>();
                        for (Track track : pPlaylist.getTracks()) {
                            ParameterAddTrackToPlaylist.Playlist.Track parameterTrack = new ParameterAddTrackToPlaylist.Playlist.Track();
                            parameterTrack.setId(track.getId());
                            parameterTracks.add(parameterTrack);
                        }
                        ParameterAddTrackToPlaylist.Playlist parameterPlaylist = new ParameterAddTrackToPlaylist.Playlist();
                        parameterPlaylist.setTracks(parameterTracks);
                        ParameterAddTrackToPlaylist parameter = new ParameterAddTrackToPlaylist();
                        parameter.setPlaylist(parameterPlaylist);

                        return api.playlistRx(pPlaylist.getId(), parameter);
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Playlist>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(Playlist pPlaylist) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxCreatePlaylistNext(pTrack, pPlaylist);
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
