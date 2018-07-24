package cc.seedland.foundation.net.stategy;


import java.lang.reflect.Type;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.RxCache;
import io.reactivex.Observable;


/**
 *  先显示缓存，缓存不存在，再请求网络，反射加载此类
 */
final public class FirstCacheStategy extends BaseStrategy {
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = loadCache(rxCache, type, key, time, true);
        Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, false);
        return cache.switchIfEmpty(remote);
    }
}
