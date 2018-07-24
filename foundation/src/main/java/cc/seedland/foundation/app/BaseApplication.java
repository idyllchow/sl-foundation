package cc.seedland.foundation.app;

import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;

import cc.seedland.foundation.net.NetTask;
import cc.seedland.oa.foundation.BuildConfig;

/**
 * author shibo
 * date 2018/6/12
 * description
 */
public class BaseApplication extends MultiDexApplication {

    private static  BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        initRouter();
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    private void initRouter(){
        if (isDebug()) {
            //一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
        NetTask.init(this);
    }

    public boolean isDebug() {
        return true;
    }

}
