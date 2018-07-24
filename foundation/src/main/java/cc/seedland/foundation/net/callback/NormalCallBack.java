
package cc.seedland.foundation.net.callback;

/**
 * <p>描述：一般的回调,默认可以使用该回调，不用关注其他回调方法</p>
 * 使用该回调默认只需要处理onError，onSuccess两个方法<br>
 */
public abstract class NormalCallBack<T> extends CallBack<T> {

    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {

    }
}
