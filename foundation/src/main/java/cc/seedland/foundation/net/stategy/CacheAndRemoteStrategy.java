package cc.seedland.foundation.net.stategy;

import java.lang.reflect.Type;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.RxCache;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;


/**
 * <p>描述：先显示缓存，再请求网络 反射调用</p>
 */
public final class CacheAndRemoteStrategy extends BaseStrategy {
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = loadCache(rxCache, type, key, time, true);
        Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, false);
        return Observable.concat(cache, remote)
                .filter((@NonNull CacheResult<T> tCacheResult) -> tCacheResult != null && tCacheResult.data != null);
    }

}
