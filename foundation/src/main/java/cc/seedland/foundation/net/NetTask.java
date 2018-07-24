package cc.seedland.foundation.net;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.net.Proxy;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import cc.seedland.foundation.net.converter.FastJsonConvertFactory;
import cc.seedland.foundation.net.converter.IDiskConverter;
import cc.seedland.foundation.net.converter.SerializableDiskConverter;
import cc.seedland.foundation.net.cookie.CookieManger;
import cc.seedland.foundation.net.interceptor.HttpLoggingInterceptor;
import cc.seedland.foundation.net.request.CustomRequest;
import cc.seedland.foundation.net.request.GetRequest;
import cc.seedland.foundation.net.request.PostRequest;
import cc.seedland.foundation.util.CommonUtils;
import cc.seedland.foundation.util.LogUtils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author shibo
 * date 24/01/2018
 * description
 */

public class NetTask {

    public static final int DEFAULT_MILLISECONDS = 6000;             //默认的超时时间
    public static final int DEFAULT_CACHE_NEVER_EXPIRE = -1;          //缓存过期时间，默认永久缓存
    private static final int TIMEOUT_READ = 30;
    private static final int TIMEOUT_WRITE = 30;
    private static final int RETRY_MAX = 3;
    private static final int DEFAULT_RETRY_COUNT = 3;                 //默认重试次数
    private static final int DEFAULT_RETRY_INCREASEDELAY = 0;         //默认重试叠加时间
    private static final int DEFAULT_RETRY_DELAY = 500;               //默认重试延时
    private static Application context;
    private volatile static NetTask singleton = null;
    private static String UPGRADE_HOST;
    private static String BASE_URL = "";
    private Cache mCache = null;                                      //Okhttp缓存对象
    private CacheMode mCacheMode = CacheMode.NO_CACHE;                //缓存类型
    private long mCacheTime = -1;                                     //缓存时间
    private File mCacheDirectory;                                     //缓存目录
    private int mRetryCount = DEFAULT_RETRY_COUNT;                    //重试次数默认3次
    private int mRetryDelay = DEFAULT_RETRY_DELAY;                    //延迟xxms重试
    private int mRetryIncreaseDelay = DEFAULT_RETRY_INCREASEDELAY;    //叠加延迟
    private long mCacheMaxSize;                                       //缓存大小
    private HttpHeaders mCommonHeaders;                               //全局公共请求头
    private HttpParams mCommonParams;                                 //全局公共请求参数
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder rfBuilder;
    private RxCache.Builder rcBuilder;
    private CookieManger cookieJar;                                   //Cookie管理

    private NetTask() {
        if (context == null) {
            throw new ExceptionInInitializerError("NetTask must be init");
        }
        okBuilder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder.addInterceptor(new cc.seedland.foundation.net.RetryInterceptor(RETRY_MAX))
//                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS);
        rfBuilder = new Retrofit.Builder();
        rfBuilder.addConverterFactory(FastJsonConvertFactory.create());
        rfBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        rcBuilder = new RxCache.Builder().init(context)
                .diskConverter(new SerializableDiskConverter());
    }

    public static NetTask getInstance() {
        if (singleton == null) {
            synchronized (NetTask.class) {
                if (singleton == null) {
                    singleton = new NetTask();
                }
            }
        }
        return singleton;
    }

    public static void init(Application application) {
        context = application;
    }

    public static String getBaseUrl() {
        return getInstance().BASE_URL;
    }

    public NetTask setBaseUrl(String host) {
        BASE_URL = CommonUtils.checkNotNull(host, "baseUrl can't be null!");
        return this;
    }

    public static Context getContext() {
        if (context == null) {
            throw new ExceptionInInitializerError("NetTask must be init");
        }
        return context;
    }

    /**
     * get请求
     */
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    /**
     * post请求
     */
    public static PostRequest post(String url) {
        return new PostRequest(url);
    }


//    public static ApiService getDefault() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okClient = new OkHttpClient
//                .Builder()
//                .addInterceptor(new cc.seedland.foundation.net.RetryInterceptor(RETRY_MAX))
//                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
//                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
//                .readTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//
//                        Request.Builder builder = chain
//                                .request()
//                                .newBuilder()
//                                .url((chain.request().url().toString()));
//                        Request request;
//                        request = builder.build();
//                        return chain.proceed(request);
//                    }
//                }).build();
//
//        return new Retrofit.Builder()
//                .baseUrl(UPGRADE_HOST)
//                .addConverterFactory(cc.seedland.foundation.net.FastJsonConvertFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(okClient)
//                .build()
//                .create(ApiService.class);
//    }

    /**
     * 自定义请求
     */
    public static CustomRequest custom() {
        return new CustomRequest();
    }

    public static OkHttpClient getOkHttpClient() {
        return getInstance().okBuilder.build();
    }

    public static Retrofit getRetrofit() {
        return getInstance().rfBuilder.build();
    }

    public static RxCache getRxCache() {
        return getInstance().rcBuilder.build();
    }

    /**
     * 对外暴露 OkHttpClient,方便自定义
     */
    public static OkHttpClient.Builder getOkBuilder() {
        return getInstance().okBuilder;
    }

    /**
     * 对外暴露 Retrofit,方便自定义
     */
    public static Retrofit.Builder getRfBuilder() {
        return getInstance().rfBuilder;
    }

    /**
     * 对外暴露 RxCache,方便自定义
     */
    public static RxCache.Builder getRcBuilder() {
        return getInstance().rcBuilder;
    }

    /**
     * 获取全局的cookie实例
     */
    public static CookieManger getCookieJar() {
        return getInstance().cookieJar;
    }

    /**
     * 超时重试次数
     */
    public static int getRetryCount() {
        return getInstance().mRetryCount;
    }

    /**
     * 超时重试次数
     */
    public NetTask setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /**
     * 超时重试延迟时间
     */
    public static int getRetryDelay() {
        return getInstance().mRetryDelay;
    }

    /**
     * 超时重试延迟时间
     */
    public NetTask setRetryDelay(int retryDelay) {
        if (retryDelay < 0) throw new IllegalArgumentException("retryDelay must > 0");
        mRetryDelay = retryDelay;
        return this;
    }

    /**
     * 超时重试延迟叠加时间
     */
    public static int getRetryIncreaseDelay() {
        return getInstance().mRetryIncreaseDelay;
    }

    /**
     * 超时重试延迟叠加时间
     */
    public NetTask setRetryIncreaseDelay(int retryIncreaseDelay) {
        if (retryIncreaseDelay < 0)
            throw new IllegalArgumentException("retryIncreaseDelay must > 0");
        mRetryIncreaseDelay = retryIncreaseDelay;
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public static CacheMode getCacheMode() {
        return getInstance().mCacheMode;
    }

    /**
     * 全局的缓存模式
     */
    public NetTask setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /**
     * 获取全局的缓存过期时间
     */
    public static long getCacheTime() {
        return getInstance().mCacheTime;
    }

    /**
     * 全局的缓存过期时间
     */
    public NetTask setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = DEFAULT_CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /**
     * 获取全局的缓存大小
     */
    public static long getCacheMaxSize() {
        return getInstance().mCacheMaxSize;
    }

    /**
     * 全局的缓存大小,默认50M
     */
    public NetTask setCacheMaxSize(long maxSize) {
        mCacheMaxSize = maxSize;
        return this;
    }

    /**
     * 获取缓存的路劲
     */
    public static File getCacheDirectory() {
        return getInstance().mCacheDirectory;
    }

    /**
     * 全局设置缓存的路径，默认是应用包下面的缓存
     */
    public NetTask setCacheDirectory(File directory) {
        mCacheDirectory = CommonUtils.checkNotNull(directory, "directory can't be null");
        rcBuilder.diskDir(directory);
        return this;
    }

    /**
     * 获取OkHttp的缓存<br>
     */
    public static Cache getHttpCache() {
        return getInstance().mCache;
    }

    /**
     * 全局设置OkHttp的缓存,默认是3天
     */
    public NetTask setHttpCache(Cache cache) {
        this.mCache = cache;
        return this;
    }

    public NetTask debug(String tag) {
        debug(tag, true);
        return this;
    }

    public NetTask debug(String tag, boolean isPrintException) {
        String tempTag = TextUtils.isEmpty(tag) ? "NetTask" : tag;
        if (isPrintException) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(tempTag, isPrintException);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(loggingInterceptor);
        }
        return this;
    }

    /**
     * 全局cookie存取规则
     */
    public NetTask setCookieStore(CookieManger cookieManager) {
        cookieJar = cookieManager;
        okBuilder.cookieJar(cookieJar);
        return this;
    }

    /**
     * 全局读取超时时间
     */
    public NetTask setReadTimeOut(long readTimeOut) {
        okBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局写入超时时间
     */
    public NetTask setWriteTimeOut(long writeTimeout) {
        okBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局连接超时时间
     */
    public NetTask setConnectTimeout(long connectTimeout) {
        okBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局设置缓存的版本，默认为1，缓存的版本号
     */
    public NetTask setCacheVersion(int cacheVersion) {
        if (cacheVersion < 0)
            throw new IllegalArgumentException("cacheVersion must > 0");
        rcBuilder.appVersion(cacheVersion);
        return this;
    }

    /**
     * 全局设置缓存的转换器
     */
    public NetTask setCacheDiskConverter(IDiskConverter converter) {
        rcBuilder.diskConverter(CommonUtils.checkNotNull(converter, "converter can't be null"));
        return this;
    }

    /**
     * 添加全局公共请求参数
     */
    public NetTask addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * 获取全局公共请求参数
     */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * 获取全局公共请求头
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     */
    public NetTask addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * 添加全局拦截器
     */
    public NetTask addInterceptor(Interceptor interceptor) {
        okBuilder.addInterceptor(CommonUtils.checkNotNull(interceptor, "interceptor can't be null"));
        return this;
    }

    /**
     * 添加全局网络拦截器
     */
    public NetTask addNetworkInterceptor(Interceptor interceptor) {
        okBuilder.addNetworkInterceptor(CommonUtils.checkNotNull(interceptor, "interceptor can't be null"));
        return this;
    }

    /**
     * 全局设置代理
     */
    public NetTask setOkproxy(Proxy proxy) {
        okBuilder.proxy(CommonUtils.checkNotNull(proxy, "proxy can't be null"));
        return this;
    }

    /**
     * 全局设置请求的连接池
     */
    public NetTask setOkconnectionPool(ConnectionPool connectionPool) {
        okBuilder.connectionPool(CommonUtils.checkNotNull(connectionPool, "connectionPool can't be null"));
        return this;
    }

    /**
     * 全局为Retrofit设置自定义的OkHttpClient
     */
    public NetTask setOkclient(OkHttpClient client) {
        rfBuilder.client(CommonUtils.checkNotNull(client, "client can't be null"));
        return this;
    }

    /**
     * 全局设置Converter.Factory,默认GsonConverterFactory.create()
     */
    public NetTask addConverterFactory(Converter.Factory factory) {
        rfBuilder.addConverterFactory(CommonUtils.checkNotNull(factory, "factory can't be null"));
        return this;
    }

    /**
     * 全局设置CallAdapter.Factory,默认RxJavaCallAdapterFactory.create()
     */
    public NetTask addCallAdapterFactory(CallAdapter.Factory factory) {
        rfBuilder.addCallAdapterFactory(CommonUtils.checkNotNull(factory, "factory can't be null"));
        return this;
    }

    /**
     * 全局设置Retrofit callbackExecutor
     */
    public NetTask setCallbackExecutor(Executor executor) {
        rfBuilder.callbackExecutor(CommonUtils.checkNotNull(executor, "executor can't be null"));
        return this;
    }

    /**
     * 全局设置Retrofit对象Factory
     */
    public NetTask setCallFactory(okhttp3.Call.Factory factory) {
        rfBuilder.callFactory(CommonUtils.checkNotNull(factory, "factory can't be null"));
        return this;
    }

}
