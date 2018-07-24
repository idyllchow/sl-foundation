package cc.seedland.foundation.architecture

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog


/**
 * author shibo
 * date 2018/6/14
 * description
 */
interface BaseContract {

    interface View : BaseView<Presenter> {

        fun isActive(): Boolean

        fun setLoadingIndicator(active: Boolean)

        fun showTitle(title: String)

        fun hideToolbar()

        fun setTvTitleRightVisibility(visibility: Int)

        fun showTitleRight(txtRight: String)

        fun initView(savedInstanceState: Bundle?)

        fun showStacked(title: String, content: String, negative: String, callBack: MaterialDialog.SingleButtonCallback?)

        fun showBasicNoTitle(content: String, positive: String, negative: String, positiveCallBack: MaterialDialog.SingleButtonCallback?, negativeCallBack: MaterialDialog.SingleButtonCallback?)

        fun showToast(toastTxt: String?)

        fun showLoading(msg: String)

        fun dismissLoading()
    }

    interface Presenter : BasePresenter {

        fun initData()

    }
}
