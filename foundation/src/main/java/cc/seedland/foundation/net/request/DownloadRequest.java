//package cc.seedland.foundation.net.request;
//
//
//import cc.seedland.foundation.net.HandleErrTransformer;
//import cc.seedland.foundation.net.callback.CallBack;
//import cc.seedland.foundation.net.func.RetryExceptionFunc;
//import cc.seedland.foundation.net.subscriber.DownloadSubscriber;
//import io.reactivex.Observable;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.ResponseBody;
//
///**
// * <p>描述：下载请求</p>
// */
//public class DownloadRequest extends BaseRequest<DownloadRequest> {
//    private String savePath;
//    private String saveName;
//    public DownloadRequest(String url) {
//        super(url);
//    }
//
//    /**
//     * 下载文件路径<br>
//     * 默认在：/storage/emulated/0/Android/data/包名/files/<br>
//     */
//    public DownloadRequest savePath(String savePath) {
//        this.savePath = savePath;
//        return this;
//    }
//
//    /**
//     * 下载文件名称<br>
//     * 默认名字是时间戳生成的<br>
//     */
//    public DownloadRequest saveName(String saveName) {
//        this.saveName = saveName;
//        return this;
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> Disposable execute(CallBack<T> callBack) {
//        return (Disposable) build().apiManager.downloadFile(url).compose(
//                (@NonNull Observable<ResponseBody> upstream) -> {
//                    if (isSyncRequest) {
//                        return upstream;//.observeOn(AndroidSchedulers.mainThread());
//                    } else {
//                        return upstream.subscribeOn(Schedulers.io())
//                                .unsubscribeOn(Schedulers.io())
//                                .observeOn(Schedulers.io());
//                    }
//                }
//        ).compose(new HandleErrTransformer()).retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay))
//                .subscribeWith(new DownloadSubscriber(context, savePath, saveName, callBack));
//    }
//
//    @Override
//    protected Observable<ResponseBody> generateRequest() {
//        return apiManager.downloadFile(url);
//    }
//}
