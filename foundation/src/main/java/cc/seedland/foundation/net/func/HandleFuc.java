package cc.seedland.foundation.net.func;


import cc.seedland.foundation.net.exception.ApiException;
import cc.seedland.foundation.net.exception.ServerException;
import cc.seedland.foundation.net.model.BaseResult;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * <p>描述：BaseResult<T>转换T</p>
 */
public class HandleFuc<T> implements Function<BaseResult<T>, T> {

    @Override
    public T apply(@NonNull BaseResult<T> baseResult) throws Exception {
        if (ApiException.isOk(baseResult)) {
            return baseResult.getData();
        } else {
            throw new ServerException(baseResult.getCode(), baseResult.getMsg());
        }
    }

}
