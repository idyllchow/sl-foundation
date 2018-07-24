package cc.seedland.foundation.net.subscriber;

import android.content.Context;

import java.lang.ref.WeakReference;

import cc.seedland.foundation.net.exception.ApiException;
import cc.seedland.foundation.util.NetUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * author shibo
 * date 25/01/2018
 * description
 */

public abstract class BaseSubscriber<T> extends DisposableObserver<T> {

    public WeakReference<Context> weakReference;

    public BaseSubscriber() {
    }

    public BaseSubscriber(Context context) {
        if (context != null) {
            weakReference = new WeakReference<>(context);
        }
    }

    @Override
    protected void onStart() {
        if (weakReference != null && weakReference.get() != null && !NetUtils.isNetworkAvailable(weakReference.get())) {
            onComplete();
        }
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(ApiException.handleException(e));
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onError(ApiException e);
}
