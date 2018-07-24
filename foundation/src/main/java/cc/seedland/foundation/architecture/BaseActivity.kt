package cc.seedland.foundation.architecture

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cc.seedland.foundation.view.SLTextView
import cc.seedland.oa.foundation.R
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.StackingBehavior

/**
 * author shibo
 * date 2018/6/14
 * description
 */
open abstract class BaseActivity : AppCompatActivity(), BaseContract.View, View.OnClickListener {
    //    lateinit var taskId: String
    lateinit var toolbar: Toolbar
    lateinit var tvTitle: SLTextView
    lateinit var tvTitleLeft: SLTextView
    lateinit var tvTitleRight: SLTextView
    lateinit var imgTitleRight: ImageView
    lateinit var flyContent: FrameLayout
    lateinit var middleView: View
    lateinit var loadingDialog: Dialog
    override lateinit var presenter: BaseContract.Presenter
    var TAG = "BaseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_base)
        toolbar = findViewById(R.id.toolbar)
        tvTitle = findViewById(R.id.tv_title)
        tvTitleLeft = findViewById(R.id.tv_title_left)
        tvTitleRight = findViewById(R.id.tv_title_right)
        imgTitleRight = findViewById(R.id.img_title_right)
        flyContent = findViewById(R.id.fly_content)
        tvTitleLeft.setOnClickListener(this)
        imgTitleRight.setOnClickListener(this)
        tvTitleRight.setOnClickListener(this)
        loadingDialog = Dialog(this, R.style.dialog_loading)
        initView(savedInstanceState)

//        taskId = intent.getStringExtra(EXTRA_TASK_ID)
        getCurrentClassName()
        // Create the presenter
        //        new Presenter(taskId, this).start();
    }


    abstract override fun initView(savedInstanceState: Bundle?)

    protected fun addMiddleView(resId: Int) {
        middleView = LayoutInflater.from(this).inflate(resId, null, true)
        flyContent.removeAllViews()
        flyContent.addView(middleView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
    }

    protected fun addMiddleView(view: View) {
        this.middleView = view
        flyContent.removeAllViews()
        flyContent.addView(middleView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun showTitle(title: String) {
        tvTitle.text = title
        toolbar.visibility = View.VISIBLE
    }

    override fun hideToolbar() {
        toolbar.visibility = View.GONE
    }

    override fun setTvTitleRightVisibility(visibility: Int) {
        tvTitleRight.visibility = visibility
    }

    override fun showTitleRight(txtRight: String) {
        tvTitleRight.text = txtRight
        tvTitleRight.visibility = View.VISIBLE
    }

    override fun setLoadingIndicator(active: Boolean) {

    }


    override fun isActive(): Boolean {
        return false
    }

    override fun showStacked(title: String, content: String, negative: String, callBack: MaterialDialog.SingleButtonCallback?) {
        runOnUiThread {
            if (!isFinishing) {
                MaterialDialog.Builder(this)
                        .title(title)
                        .content(content)
                        .negativeText(negative)
                        .onNegative(callBack!!)
                        .btnStackedGravity(GravityEnum.END)
                        .stackingBehavior(StackingBehavior.ALWAYS)
                        .show()
            }
        }
    }

    override fun showBasicNoTitle(content: String, positive: String, negative: String, positiveCallBack: MaterialDialog.SingleButtonCallback?, negativeCallBack: MaterialDialog.SingleButtonCallback?) {
        runOnUiThread {
            if (!isFinishing) {
                MaterialDialog.Builder(this)
                        .content(content)
                        .positiveText(positive)
                        .negativeText(negative)
                        .onPositive(positiveCallBack!!)
                        .onNegative(negativeCallBack!!)
                        .show()
            }
        }
    }

    override fun showToast(toastTxt: String?) {
        Toast.makeText(this, toastTxt, Toast.LENGTH_LONG).show()
    }

    override fun showLoading(msg: String) {
        runOnUiThread {
            dismissLoading()
            if (!isFinishing) {
                createLoadingDialog(msg).show()
            }
        }
    }

    override fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    private fun createLoadingDialog(msg: String): Dialog {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.dialog_loading, null)// 得到加载view
        val layout = v.findViewById(R.id.dialog_view) as LinearLayout// 加载布局
        val tipTextView = v.findViewById(R.id.tipTextView) as TextView// 提示文字
        if (msg == "" && msg.isEmpty()) {
            tipTextView.visibility = View.GONE
        } else {
            tipTextView.text = msg// 设置加载信息
        }
//            loadingDialog = Dialog(this, R.style.dialog_loading)// 创建自定义样式dialog
        loadingDialog.setCancelable(true)// 可以用“返回键”取消
        loadingDialog.setContentView(layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT))// 设置布局

        return loadingDialog
    }

    private fun getCurrentClassName() {
        val className = this.javaClass.name
        val i = className.lastIndexOf(".")
        val length = className.length
        TAG = className.substring(i + 1, length)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_title_left -> {
                onBackPressed()
            }
            R.id.tv_title_right -> {

            }
            R.id.img_title_right -> {

            }
            else -> {
            }
        }
    }

    companion object {

        val EXTRA_TASK_ID = "TASK_ID"
    }
}
