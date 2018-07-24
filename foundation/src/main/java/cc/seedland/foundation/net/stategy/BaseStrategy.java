package cc.seedland.foundation.net.stategy;

import java.lang.reflect.Type;
import java.util.ConcurrentModificationException;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.RxCache;
import cc.seedland.foundation.util.LogUtils;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


/**
 * <p>描述：实现缓存策略的基类</p>
 */
public abstract class BaseStrategy implements IStrategy {
    <T> Observable<CacheResult<T>> loadCache(final RxCache rxCache, Type type, final String key, final long time, final boolean needEmpty) {
        Observable<CacheResult<T>> observable = rxCache.<T>load(type, key, time).flatMap(
                (@NonNull T t) -> {
                    if (t == null) {
                        return Observable.error(new NullPointerException("Not find the cache!"));
                    }
                    return Observable.just(new CacheResult<T>(true, t));
                }
        );
        if (needEmpty) {
            observable = observable
                    .onErrorResumeNext(
                            (@NonNull Throwable throwable) -> Observable.empty()
                    );
        }
        return observable;
    }

    //请求成功后：异步保存
    <T> Observable<CacheResult<T>> loadRemote2(final RxCache rxCache, final String key, Observable<T> source, final boolean needEmpty) {
        Observable<CacheResult<T>> observable = source
                .map((@NonNull T t) -> {
                    LogUtils.INSTANCE.d("loadRemote result=" + t);
                    rxCache.save(key, t).subscribeOn(Schedulers.io())
                            .subscribe(
                                    (@NonNull Boolean status) -> LogUtils.INSTANCE.d("save status => " + status)
                                    , (@NonNull Throwable throwable) -> {
                                        if (throwable instanceof ConcurrentModificationException) {
                                            LogUtils.INSTANCE.d("Save failed, please use a synchronized cache strategy :" + throwable);
                                        } else {
                                            LogUtils.INSTANCE.d(throwable.getMessage());
                                        }
                                    });
                    return new CacheResult<T>(false, t);
                });

        if (needEmpty) {
            observable = observable
                    .onErrorResumeNext((@NonNull Throwable throwable) -> Observable.empty());

        }
        return observable;
    }

    //请求成功后：同步保存
    <T> Observable<CacheResult<T>> loadRemote(final RxCache rxCache, final String key, Observable<T> source, final boolean needEmpty) {
        Observable<CacheResult<T>> observable = source
                .flatMap(
                        (final @NonNull T t) ->
                                rxCache.save(key, t).map(
                                        (@NonNull Boolean aBoolean) -> {
                                            LogUtils.INSTANCE.d("save status => " + aBoolean);
                                            return new CacheResult<T>(false, t);
                                        })
                                        .onErrorReturn(
                                                (@NonNull Throwable throwable) -> {
                                                    LogUtils.INSTANCE.d("save status => " + throwable);
                                                    return new CacheResult<T>(false, t);
                                                }));

        if (needEmpty) {
            observable = observable
                    .onErrorResumeNext(
                            (@NonNull Throwable throwable) -> Observable.empty());
        }
        return observable;
    }
}
