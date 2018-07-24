package cc.seedland.foundation.net.stategy;


import java.lang.reflect.Type;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.RxCache;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * <p>描述：网络加载，不缓存</p>
 */
public class NoStrategy implements IStrategy {

    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String cacheKey, long cacheTime, Observable<T> source, Type type) {
        return source.map((@NonNull T t) -> new CacheResult<T>(false, t));
    }

}
