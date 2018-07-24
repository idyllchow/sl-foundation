package cc.seedland.oa.slfoundation.architecture;

import cc.seedland.foundation.architecture.BaseContract;
import cc.seedland.foundation.architecture.Presenter;

/**
 * author shibo
 * date 2018/6/14
 * description
 */
public class SignInPresenter extends Presenter implements SignInContract.Presenter{

    private final SignInContract.View signInView;
    private String mTaskId;


    public SignInPresenter(/*String taskId, */SignInContract.View signInView) {
        super(/*taskId, */signInView);
//        this.mTaskId = taskId;
        this.signInView = signInView;
        this.signInView.setPresenter(this);
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
        signInView.showTitle("Demo");
    }
}
