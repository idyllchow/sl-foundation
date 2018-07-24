package cc.seedland.foundation.architecture

/**
 * author shibo
 * date 2018/6/14
 * description
 */
open class Presenter(/*protected var mTaskId: String?,*/ private var baseView: BaseContract.View) : BaseContract.Presenter {


    override fun initData() {

    }

    override fun start() {
        baseView.showTitle("TitleBar")
    }


}
