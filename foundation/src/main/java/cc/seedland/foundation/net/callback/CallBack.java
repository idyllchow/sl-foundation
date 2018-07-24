
package cc.seedland.foundation.net.callback;


import cc.seedland.foundation.net.exception.ApiException;
import cc.seedland.foundation.util.NetUtils;
import java.lang.reflect.Type;

/**
 * 网络请求回调
 */
public abstract class CallBack<T> implements IType<T> {

    public abstract void onStart();

    public abstract void onCompleted();

    public abstract void onError(ApiException e);

    public abstract void onSuccess(T t);

    @Override
    public Type getType() {//获取需要解析的泛型T类型
        return NetUtils.findNeedClass(getClass());
    }

    public Type getRawType() {//获取需要解析的泛型T raw类型
        return NetUtils.findRawType(getClass());
    }
}
