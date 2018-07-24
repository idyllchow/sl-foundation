package cc.seedland.foundation.net.request;


import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.callback.CallBack;
import cc.seedland.foundation.net.callback.CallBackProxy;
import cc.seedland.foundation.net.callback.CallClazzProxy;
import cc.seedland.foundation.net.func.BaseResultFunc;
import cc.seedland.foundation.net.func.CacheResultFunc;
import cc.seedland.foundation.net.func.RetryExceptionFunc;
import cc.seedland.foundation.net.model.BaseResult;
import cc.seedland.foundation.net.subscriber.CallBackSubscriber;
import cc.seedland.foundation.util.RxUtils;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;


public class GetRequest extends BaseRequest<GetRequest> {
    public GetRequest(String url) {
        super(url);
    }

    public <T> Observable<T> execute(Class<T> clazz) {
        return execute(new CallClazzProxy<BaseResult<T>, T>(clazz) {
        });
    }

    public <T> Observable<T> execute(Type type) {
        return execute(new CallClazzProxy<BaseResult<T>, T>(type) {
        });
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> execute(CallClazzProxy<? extends BaseResult<T>, T> proxy) {
        return build().generateRequest()
                .map(new BaseResultFunc(proxy.getType()))
                .compose(isSyncRequest ? RxUtils._main() : RxUtils._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallType()))
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay))
                .compose((@NonNull Observable upstream) -> upstream.map(new CacheResultFunc<T>()));
    }

    public <T> Disposable execute(CallBack<T> callBack) {
        return execute(new CallBackProxy<BaseResult<T>, T>(callBack) {
        });
    }

    public <T> Disposable execute(CallBackProxy<? extends BaseResult<T>, T> proxy) {
        Observable<CacheResult<T>> observable = build().toObservable(apiManager.get(url, params.urlParamsMap), proxy);
        if (CacheResult.class != proxy.getCallBack().getRawType()) {
            return observable.compose((@NonNull Observable<CacheResult<T>> upstream) -> upstream.map(new CacheResultFunc<T>())).subscribeWith(new CallBackSubscriber<T>(context, proxy.getCallBack()));
        } else {
            return observable.subscribeWith(new CallBackSubscriber<CacheResult<T>>(context, proxy.getCallBack()));
        }
    }


    @SuppressWarnings("unchecked")
    private <T> Observable<CacheResult<T>> toObservable(Observable observable, CallBackProxy<? extends BaseResult<T>, T> proxy) {
        return observable.map(new BaseResultFunc(proxy != null ? proxy.getType() : new TypeReference<ResponseBody>() {
        }.getType()))
                .compose(isSyncRequest ? RxUtils._main() : RxUtils._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallBack().getType()))
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        return apiManager.get(url, params.urlParamsMap);
    }
}
