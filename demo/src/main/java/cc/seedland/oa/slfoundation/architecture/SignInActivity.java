package cc.seedland.oa.slfoundation.architecture;

import android.os.Bundle;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cc.seedland.foundation.architecture.BaseActivity;
import cc.seedland.oa.slfoundation.R;

/**
 * author shibo
 * date 2018/6/14
 * description
 */
public class SignInActivity extends BaseActivity {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        addMiddleView(R.layout.act_sign_in);
        showLoading("loading");
        new SignInPresenter(this).start();

        showTitleRight("分享");
    }

    @Override
    public void onClick(@NotNull View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_title_right) {
            showToast("click=======right");
        }
    }
}
