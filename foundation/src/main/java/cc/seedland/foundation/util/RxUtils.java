package cc.seedland.foundation.util;

import cc.seedland.foundation.net.func.HandleFuc;
import cc.seedland.foundation.net.func.HttpResponseFunc;
import cc.seedland.foundation.net.model.BaseResult;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <p>描述：线程调度工具</p>
 */
public class RxUtils {

    public static <T> ObservableTransformer<T, T> io_main() {
        return (@NonNull Observable<T> upstream) -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(
                        (@NonNull Disposable disposable) -> LogUtils.INSTANCE.d("doOnSubscribe io_main " + disposable.isDisposed())
                )
                .doFinally(() -> LogUtils.INSTANCE.d("doFinally"))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<BaseResult<T>, T> _io_main() {
        return (@NonNull Observable<BaseResult<T>> upstream) -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HandleFuc<T>())
                .doOnSubscribe(
                        (@NonNull Disposable disposable) -> LogUtils.INSTANCE.d("doOnSubscribe _io_main" + disposable.isDisposed())
                )
                .doFinally(() -> LogUtils.INSTANCE.d("doFinally"))
                .onErrorResumeNext(new HttpResponseFunc<T>());
    }


    public static <T> ObservableTransformer<BaseResult<T>, T> _main() {
        return (@NonNull Observable<BaseResult<T>> upstream) -> upstream
                //.observeOn(AndroidSchedulers.mainThread())
                .map(new HandleFuc<T>())
                .doOnSubscribe(
                        (@NonNull Disposable disposable) -> LogUtils.INSTANCE.d("doOnSubscribe _main" + disposable.isDisposed())
                )
                .doFinally(() -> LogUtils.INSTANCE.d("doFinally"))
                .onErrorResumeNext(new HttpResponseFunc<T>());
    }
}
