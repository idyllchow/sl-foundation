
package cc.seedland.foundation.net.callback;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cc.seedland.foundation.net.CacheResult;
import cc.seedland.foundation.net.model.BaseResult;
import cc.seedland.foundation.util.NetUtils;
import okhttp3.ResponseBody;

/**
 * <p>描述：提供回调代理</p>
 * 主要用于可以自定义BaseResult<br>
 */
public abstract class CallBackProxy<T extends BaseResult<R>, R> implements IType<T> {
    CallBack<R> mCallBack;

    public CallBackProxy(CallBack<R> callBack) {
        mCallBack = callBack;
    }

    public CallBack getCallBack() {
        return mCallBack;
    }

    @Override
    public Type getType() {//CallBack代理方式，获取需要解析的Type

        Type typeArguments = null;
        if (mCallBack != null) {
            Type rawType = mCallBack.getRawType();//如果用户的信息是返回List需单独处理
            if (List.class.isAssignableFrom(NetUtils.getClass(rawType, 0)) || Map.class.isAssignableFrom(NetUtils.getClass(rawType, 0))) {
                typeArguments = mCallBack.getType();
            } else if (CacheResult.class.isAssignableFrom(NetUtils.getClass(rawType, 0))) {
                 Type type = mCallBack.getType();
                typeArguments = NetUtils.getParameterizedType(type, 0);
            } else {
                Type type = mCallBack.getType();
                typeArguments = NetUtils.getClass(type, 0);
            }

        }
        if (typeArguments == null) {
            typeArguments = ResponseBody.class;
        }
        Type rawType = NetUtils.findNeedType(getClass());
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        Type type = new ParameterizedTypeImpl(new Type[]{typeArguments}, null, rawType);
        return type;
    }
}
