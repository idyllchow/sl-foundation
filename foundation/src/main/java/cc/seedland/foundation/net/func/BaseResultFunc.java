package cc.seedland.foundation.net.func;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cc.seedland.foundation.net.model.BaseResult;
import cc.seedland.foundation.util.NetUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;


public class BaseResultFunc<T> implements Function<ResponseBody, BaseResult<T>> {
    protected Type type;

    public BaseResultFunc(Type type) {
        this.type = type;
    }

    @Override
    public BaseResult<T> apply(@NonNull ResponseBody responseBody) throws Exception {
        BaseResult<T> baseResult = new BaseResult<>();
        baseResult.setCode(-1);
        if (type instanceof ParameterizedType) {//自定义ApiResult
            final Class<T> cls = (Class) ((ParameterizedType) type).getRawType();
            if (BaseResult.class.isAssignableFrom(cls)) {
                final Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                final Class clazz = NetUtils.getClass(params[0], 0);
                final Class rawType = NetUtils.getClass(type, 0);
                try {
                    String json = responseBody.string();
                    json = json.replace("}{", ",");
                    //增加是List<String>判断错误的问题
                    if (!List.class.isAssignableFrom(rawType) && clazz.equals(String.class)) {
                        baseResult.setData((T) json);
                        baseResult.setCode(0);
                    } else {
                        BaseResult result = JSON.parseObject(json, type);
                        if (result != null) {
                            baseResult = result;
                            if (result.getData() == null) {
                                final Class<T> clazzz = NetUtils.getClass(type, 0);
                                T dataa = JSON.parseObject(json, clazzz);
                                baseResult.setData(dataa);
                            }
                        } else {
                            baseResult.setMsg("json is null");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    baseResult.setMsg(e.getMessage());
                } finally {
                    responseBody.close();
                }
            } else {
                baseResult.setMsg("ApiResult.class.isAssignableFrom(cls) err!!");
            }
        } else {//默认Apiresult
            try {
                final String json = responseBody.string();
                final Class<T> clazz = NetUtils.getClass(type, 0);
                if (clazz.equals(String.class)) {
                    //apiResult.setData((T) json);
                    //apiResult.setCode(0);
                    final BaseResult result = parseApiResult(json, baseResult);
                    if (result != null) {
                        baseResult = result;
                        baseResult.setData((T) json);
                    } else {
                        baseResult.setMsg("json is null");
                    }
                } else {
                    final BaseResult result = parseApiResult(json, baseResult);
                    if (result != null) {
                        baseResult = result;
                        if (baseResult.getData() != null) {
                            T data = JSON.parseObject(baseResult.getData().toString(), clazz);
                            baseResult.setData(data);
                        } else {
                            baseResult.setMsg("ApiResult's data is null");
                        }
                    } else {
                        baseResult.setMsg("json is null");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                baseResult.setMsg(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                baseResult.setMsg(e.getMessage());
            } finally {
                responseBody.close();
            }
        }
        return baseResult;
    }

    private BaseResult parseApiResult(String json, BaseResult apiResult) throws JSONException {
        if (TextUtils.isEmpty(json))
            return null;
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("code")) {
            apiResult.setCode(jsonObject.getInt("code"));
        }
        if (jsonObject.has("data")) {
            apiResult.setData(jsonObject.getString("data"));
        }
        if (jsonObject.has("msg")) {
            apiResult.setMsg(jsonObject.getString("msg"));
        }
        return apiResult;
    }
}
