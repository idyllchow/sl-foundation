package cc.seedland.foundation.net.request;

import com.alibaba.fastjson.TypeReference;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.HandleErrTransformer;
import cc.seedland.foundation.net.callback.CallBack;
import cc.seedland.foundation.net.callback.CallBackProxy;
import cc.seedland.foundation.net.func.BaseResultFunc;
import cc.seedland.foundation.net.func.CacheResultFunc;
import cc.seedland.foundation.net.func.HandleFuc;
import cc.seedland.foundation.net.func.RetryExceptionFunc;
import cc.seedland.foundation.net.model.BaseResult;
import cc.seedland.foundation.net.subscriber.CallBackSubscriber;
import cc.seedland.foundation.util.CommonUtils;
import cc.seedland.foundation.util.RxUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * author shibo
 * date 2018/7/24
 * description
 */
public class CustomRequest extends BaseRequest<CustomRequest>{

    public CustomRequest() {
        super("");
    }

    @Override
    public CustomRequest build() {
        return super.build();
    }

    /**
     * 创建api服务  可以支持自定义的api，默认使用BaseApiService,上层不用关心
     *
     * @param service 自定义的apiservice class
     */
    public <T> T create(final Class<T> service) {
        checkvalidate();
        return retrofit.create(service);
    }

    private void checkvalidate() {
        CommonUtils.checkNotNull(retrofit, "请先在调用build()才能使用");
    }

    /**
     * 调用call返回一个Observable<T>
     * 举例：如果你给的是一个Observable<BaseResult<AuthModel>> 那么返回的<T>是一个BaseResult<AuthModel>
     */
    public <T> Observable<T> call(Observable<T> observable) {
        checkvalidate();
        return observable.compose(RxUtils.io_main())
                .compose(new HandleErrTransformer())
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    public <T> void call(Observable<T> observable, CallBack<T> callBack) {
        call(observable, new CallBackSubscriber(context, callBack));
    }

    public <R> void call(Observable observable, Observer<R> subscriber) {
        observable.compose(RxUtils.io_main())
                .subscribe(subscriber);
    }


    /**
     * 调用call返回一个Observable,针对BaseResult的业务<T>
     * 举例：如果你给的是一个Observable<BaseResult<AuthModel>> 那么返回的<T>是AuthModel
     */
    public <T> Observable<T> apiCall(Observable<BaseResult<T>> observable) {
        checkvalidate();
        return observable
                .map(new HandleFuc<T>())
                .compose(RxUtils.<T>io_main())
                .compose(new HandleErrTransformer<T>())
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    public <T> Disposable apiCall(Observable<T> observable, CallBack<T> callBack) {
        return call(observable, new CallBackProxy<BaseResult<T>, T>(callBack) {
        });
    }

    public <T> Disposable call(Observable<T> observable, CallBackProxy<? extends BaseResult<T>, T> proxy) {
        Observable<CacheResult<T>> cacheobservable = build().toObservable(observable, proxy);
        if (CacheResult.class != proxy.getCallBack().getRawType()) {
            return cacheobservable.compose(new ObservableTransformer<CacheResult<T>, T>() {
                @Override
                public ObservableSource<T> apply(@NonNull Observable<CacheResult<T>> upstream) {
                    return upstream.map(new CacheResultFunc<T>());
                }
            }).subscribeWith(new CallBackSubscriber<T>(context, proxy.getCallBack()));
        } else {
            return cacheobservable.subscribeWith(new CallBackSubscriber<CacheResult<T>>(context, proxy.getCallBack()));
        }
    }

    private <T> Observable<CacheResult<T>> toObservable(Observable observable, CallBackProxy<? extends BaseResult<T>, T> proxy) {
        return observable.map(new BaseResultFunc(proxy != null ? proxy.getType() : new TypeReference<ResponseBody>() {
        }.getType()))
                .compose(isSyncRequest ? RxUtils._main() : RxUtils._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallBack().getType()))
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        return null;
    }

}
