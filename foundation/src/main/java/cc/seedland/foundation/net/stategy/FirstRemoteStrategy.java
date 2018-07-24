package cc.seedland.foundation.net.stategy;


import java.lang.reflect.Type;
import java.util.Arrays;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.RxCache;
import io.reactivex.Observable;


/**
 * 描述：先请求网络，网络请求失败，再加载缓存 反射加载此类
 */
public final class FirstRemoteStrategy extends BaseStrategy {
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = loadCache(rxCache, type, key, time, true);
        Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, false);
        //return remote.switchIfEmpty(cache);
        return Observable
                .concatDelayError(Arrays.asList(remote, cache))
                .take(1);
    }
}
