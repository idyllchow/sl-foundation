package cc.seedland.foundation.architecture;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author shibo
 * date 2018/7/18
 * description
 */
public abstract class BaseFragment extends Fragment implements BaseContract.View {

    private BaseContract.Presenter presenter;

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showTitle(@NotNull String title) {

    }

    @Override
    public void hideToolbar() {

    }

    @Override
    public void setTvTitleRightVisibility(int visibility) {

    }

    @Override
    public void showTitleRight(@NotNull String txtRight) {

    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void showStacked(@NotNull String title, @NotNull String content, @NotNull String negative, @Nullable MaterialDialog.SingleButtonCallback callBack) {

    }

    @Override
    public void showBasicNoTitle(@NotNull String content, @NotNull String positive, @NotNull String negative, @Nullable MaterialDialog.SingleButtonCallback positiveCallBack, @Nullable MaterialDialog.SingleButtonCallback negativeCallBack) {

    }

    @Override
    public void showToast(@Nullable String toastTxt) {

    }

    @Override
    public void showLoading(@NotNull String msg) {

    }

    @Override
    public void dismissLoading() {

    }

}
