package cc.seedland.foundation.net.callback;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cc.seedland.foundation.net.model.BaseResult;
import cc.seedland.foundation.util.NetUtils;

/**
 * <p>描述：提供Clazz回调代理</p>
 * 主要用于可以自定义ApiResult<br>
 */
public abstract class CallClazzProxy<T extends BaseResult<R>, R> implements IType<T> {
    private Type type;


    public CallClazzProxy(Type type) {
        this.type = type;
    }

    public Type getCallType() {
        return type;
    }

    @Override
    public Type getType() {//CallClazz代理方式，获取需要解析的Type
        Type rawType = NetUtils.findNeedType(getClass());
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }

        Type dataType =  ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        return new ParameterizedTypeImpl(new Type[]{dataType}, null, rawType);
    }
}
