package cc.seedland.foundation.net.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * author shibo
 * date 24/01/2018
 * description
 */

public class RequestConverter<T> implements Converter<T, RequestBody> {

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), JSON.toJSONBytes(value));
    }
}
