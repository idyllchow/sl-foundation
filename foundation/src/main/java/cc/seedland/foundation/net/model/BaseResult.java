package cc.seedland.foundation.net.model;

import cc.seedland.foundation.util.LogUtils;

/**
 * <p>描述：提供的默认的标注返回api</p>
 */
public class BaseResult<T> {
    private int code;
    private String msg;
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        LogUtils.INSTANCE.d("base result isSuccess: " + (code == 0));
        return code == 0;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
