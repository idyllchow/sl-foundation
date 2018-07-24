package cc.seedland.foundation.net.converter;


import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * author shibo
 * date 24/01/2018
 * description
 */

public class FastJsonConvertFactory extends Converter.Factory{

    private final SerializeConfig serializeConfig;

    private FastJsonConvertFactory(SerializeConfig serializeConfig) {
        if (serializeConfig == null) {
            throw new NullPointerException("serializeConfig == null");
        }
        this.serializeConfig = serializeConfig;
    }

    public static FastJsonConvertFactory create() {
        return create(SerializeConfig.getGlobalInstance());
    }

    public static FastJsonConvertFactory create(SerializeConfig serializeConfig) {
        return new FastJsonConvertFactory(serializeConfig);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestConverter<>();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ResponseConverter<>(type);
    }
}
