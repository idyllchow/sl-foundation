package cc.seedland.oa.slfoundation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;

import cc.seedland.foundation.app.BaseApplication;
import cc.seedland.foundation.architecture.BaseActivity;
import cc.seedland.foundation.net.NetTask;
import cc.seedland.foundation.net.callback.CallBackProxy;
import cc.seedland.foundation.net.callback.NormalCallBack;
import cc.seedland.foundation.net.callback.ProgressDialogCallBack;
import cc.seedland.foundation.net.exception.ApiException;
import cc.seedland.foundation.net.subscriber.IProgressDialog;
import cc.seedland.foundation.util.LogUtils;
import cc.seedland.foundation.view.SLTextView;

public class MainActivity extends BaseActivity {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        addMiddleView(R.layout.activity_main);
        SLTextView btnGet = findViewById(R.id.btn_get);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGet();
            }
        });
        SLTextView btnGet2 = findViewById(R.id.btn_get2);
        btnGet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGet2();
            }
        });
        SLTextView btnGet3 = findViewById(R.id.btn_get3);
        btnGet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGet3();
            }
        });

        showTitleRight("分享");
    }

    private void testGet() {
        LogUtils.INSTANCE.d("test");
        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("登录中...");
                return dialog;
            }
        };
//        NetTask.get("/update/versionCheck.jsp")
//                .params("version", "6.5.33")
//                .params("os", "android")
//                .execute(new CallBackProxy<TestApiResult1<VersionCheckBean>, VersionCheckBean>(new ProgressDialogCallBack<VersionCheckBean>(mProgressDialog) {
//                    @Override
//                    public void onError(ApiException e) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(VersionCheckBean versionCheckBean) {
//                        LogUtils.d("versionCheckBean: " + versionCheckBean);
//                    }
//                }) {
//
//                });


        NetTask.get("/update/versionCheck.jsp")
                .baseUrl("http://oa-test.seedland.cc")
                .params("version", "6.5.33")
                .params("os", "android")
                .readTimeOut(30 * 1000)//局部定义读超时 ,可以不用定义
                .writeTimeOut(30 * 1000)
                .connectTimeout(30 * 1000)
                .timeStamp(true)
                .execute(new NormalCallBack<VersionCheckBean>() {
                             @Override
                             public void onError(ApiException e) {
                                 e.printStackTrace();
                                 LogUtils.INSTANCE.d("====e==" + e.getMessage());
                             }

                             @Override
                             public void onSuccess(VersionCheckBean versionCheckBean) {
                                 LogUtils.INSTANCE.d("===data: " + versionCheckBean.url + "; msg: " + versionCheckBean.msg);
                             }
                         }
                );
    }

    public void testGet2() {
        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("登录中...");
                return dialog;
            }
        };
        NetTask.get("/update/versionCheck.jsp")
                .baseUrl("http://oa-test.seedland.cc")
                .params("version", "6.5.33")
                .params("os", "android")
                .readTimeOut(30 * 1000)//局部定义读超时 ,可以不用定义
                .writeTimeOut(30 * 1000)
                .connectTimeout(30 * 1000)
                .timeStamp(true)
                .execute(new NormalCallBack<VersionCheckBean>() {
                             @Override
                             public void onError(ApiException e) {
                                 e.printStackTrace();
                                 LogUtils.INSTANCE.d("====e==" + e.getMessage());
                             }

                             @Override
                             public void onSuccess(VersionCheckBean versionCheckBean) {
                                 LogUtils.INSTANCE.d("===data: " + versionCheckBean.url + "; msg: " + versionCheckBean.msg);
                             }
                         }
                );

    }

    public void testGet3() {
        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("登录中...");
                return dialog;
            }
        };
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "4241");
        JSONObject jsonObject = new JSONObject(params);
        NetTask.post("/app/index/count-mail-number.htm")
                .baseUrl("http://sloa.isunn.cn")
                .params("userId", "4241")
//                .params("status", "3")
//                .params("orderBy", "2")
                .readTimeOut(30 * 1000)//局部定义读超时 ,可以不用定义
                .writeTimeOut(30 * 1000)
                .connectTimeout(30 * 1000)
                .timeStamp(true)
                .execute(new CallBackProxy<CirculateBaseBean<MailCountBean>, MailCountBean>(new ProgressDialogCallBack<MailCountBean>(null){
                    @Override
                    public void onSuccess(MailCountBean mailCountBean) {
                        LogUtils.INSTANCE.d("===result===" + mailCountBean);
                    }
                }) {

                });

    }

    @Override
    public void onClick(@NotNull View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_title_right) {
            showToast("click=======right");
        }
    }
}
