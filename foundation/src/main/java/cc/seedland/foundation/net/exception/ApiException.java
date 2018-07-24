package cc.seedland.foundation.net.exception;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.UnknownHostException;

import cc.seedland.foundation.net.model.BaseResult;
import retrofit2.HttpException;

/**
 * author shibo
 * date 25/01/2018
 * description
 */

public class ApiException extends Exception {
    //对应HTTP的状态码
    private static final int BADREQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private final int code;
    private String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean isOk(BaseResult baseResult) {
        if (baseResult != null && baseResult.isOk()) {
            return true;
        }
        return false;
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            ex = new ApiException(e, ((HttpException) e).code());
            ex.message = ((HttpException) e).message();

        } else if (e instanceof ServerException) {
            ex = new ApiException(e, IErrorCode.ERROR_SERVER_EXCEPTION);
            ex.message = TextUtils.isEmpty(ex.message) ?  "服务端异常，请稍后再试" : ex.message;
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, IErrorCode.ERROR_NONETWORKACTIVITY_EXCEPTION);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ApiException(e, IErrorCode.ERROR_CONNECTIONTIMEOUT_EXCEPTION);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ApiException(e, IErrorCode.ERROR_SOCKETTIMEOUT_EXCEPTION);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, IErrorCode.ERROR_UNKNOWNHOST_EXCEPTION);
            ex.message = "无法解析该域名";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, IErrorCode.ERROR_SSLVERIFY_EXCEPTION);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof JSONException) {
            ex = new ApiException(e, IErrorCode.ERROR_PARSE_EXCEPTION);
            ex.message = "解析异常";
        } else if (e instanceof ClassCastException) {
            ex = new ApiException(e, IErrorCode.ERROR_CAST_EXCEPTION);
            ex.message = "转型异常";
            return ex;
        } else if (e instanceof NullPointerException) {
            ex = new ApiException(e, IErrorCode.ERROR_NULLPOINTER_EXCEPTION);
            ex.message = "NullPointerException";
            return ex;
        } else {
            ex = new ApiException(e, IErrorCode.ERROR_UNKNOWN_EXCEPTION);
            ex.message = (TextUtils.isEmpty(e.getMessage()) ? "未知错误" : e.getMessage());
            return ex;
        }

        return ex;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public interface IErrorCode {
        /**
         * 无网络活动状态
         */
        int ERROR_NONETWORKACTIVITY_EXCEPTION = 1001;
        /**
         * 网络链接超时
         */
        int ERROR_CONNECTIONTIMEOUT_EXCEPTION = 1002;
        /**
         * SOCKET链接超时
         */
        int ERROR_SOCKETTIMEOUT_EXCEPTION = 1003;

        /**
         * 主机名异常
         */
        int ERROR_UNKNOWNHOST_EXCEPTION = 1004;
        /**
         * SSL证书信任失败
         */
        int ERROR_SSLVERIFY_EXCEPTION = 1005;
        /**
         * 解析异常
         */
        int ERROR_PARSE_EXCEPTION = 1006;
        /**
         * 转型异常
         */
        int ERROR_CAST_EXCEPTION = 1007;
        /**
         * 空指针异常
         */
        int ERROR_NULLPOINTER_EXCEPTION = 1008;
        /**
         * 空指针异常
         */
        int ERROR_UNKNOWN_EXCEPTION = 1009;

        /**
         * 请求参数错误
         */
        int ERROR_PARAMS_EXCEPTION = 400;

        /**
         * 没权限
         */
        int ERROR_NO_PERMISSION_EXCEPTION = 403;

        /**
         * 资源不存在
         */
        int ERROR_RESOURCE_NOT_FOUND_EXCEPTION = 404;
        /**
         * 服务端异常
         */
        int ERROR_SERVER_EXCEPTION = 500;
        /**
         * 服务端异常
         */
        int ERROR_SERVER_ONE_EXCEPTION = 501;
        /**
         * 服务端异常
         */
        int ERROR_SERVER_TWO_EXCEPTION = 502;
        /**
         * 服务端异常
         */
        int ERROR_SERVER_THREE_EXCEPTION = 503;

    }
}
