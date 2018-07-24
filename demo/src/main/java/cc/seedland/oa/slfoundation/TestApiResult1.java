
package cc.seedland.oa.slfoundation;


import cc.seedland.foundation.net.model.BaseResult;

public class TestApiResult1<T> extends BaseResult<T> {
    String reason;
    int error_code;
    //int resultcode;
    T result;

    @Override
    public int getCode() {
        return error_code;
    }


    @Override
    public String getMsg() {
        return reason;
    }


    @Override
    public T getData() {
        return result;
    }

    @Override
    public boolean isOk() {
        return getCode()==0;//如果不是0表示成功，请重写isOk()方法。
    }

    @Override
    public String toString() {
        return "PhoneIpApiBean{" +
                "reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", result="  + result +
                '}';
    }


}
