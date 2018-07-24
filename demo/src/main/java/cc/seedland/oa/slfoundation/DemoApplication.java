package cc.seedland.oa.slfoundation;

import android.app.Application;

import cc.seedland.foundation.app.BaseApplication;
import cc.seedland.foundation.net.NetTask;

/**
 * author shibo
 * date 25/01/2018
 * description
 */

public class DemoApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NetTask.init(this);
        NetTask.getInstance().setBaseUrl("http://oa-test.seedland.cc");
    }
}
