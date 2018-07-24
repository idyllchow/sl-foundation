package cc.seedland.foundation.net.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * author shibo
 * date 24/01/2018
 * description
 */

public class ResponseConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public ResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource buffer = Okio.buffer(value.source());
        String s = buffer.readUtf8();
        buffer.close();
//        try {
        return JSON.parseObject(s, type);
//        } catch (com.alibaba.fastjson.JSONException e) {
//            return (T) JSON.parseArray(s);
//        }
    }
}
