package cc.seedland.oa.slfoundation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author shibo
 * date 08/02/2018
 * description
 */

public class MailCountBean implements Parcelable {


    /**
     * deleteCount : 0
     * todoCount : 0
     * receiveInCount : 0
     * sendCount : 0
     * receiveCount : 0
     * sendInCount : 0
     * ceceiveCompleteCount : 0
     * waitSendCount : 0
     * completeCount : 0
     */
    private int type;
    private int deleteCount;
    private int todoCount;
    private int receiveInCount;
    private int sendCount;
    private int receiveCount;
    private int sendInCount;
    private int ceceiveCompleteCount;
    private int waitSendCount;
    private int completeCount;

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public int getTodoCount() {
        return todoCount;
    }

    public void setTodoCount(int todoCount) {
        this.todoCount = todoCount;
    }

    public int getReceiveInCount() {
        return receiveInCount;
    }

    public void setReceiveInCount(int receiveInCount) {
        this.receiveInCount = receiveInCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getSendInCount() {
        return sendInCount;
    }

    public void setSendInCount(int sendInCount) {
        this.sendInCount = sendInCount;
    }

    public int getCeceiveCompleteCount() {
        return ceceiveCompleteCount;
    }

    public void setCeceiveCompleteCount(int ceceiveCompleteCount) {
        this.ceceiveCompleteCount = ceceiveCompleteCount;
    }

    public int getWaitSendCount() {
        return waitSendCount;
    }

    public void setWaitSendCount(int waitSendCount) {
        this.waitSendCount = waitSendCount;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public MailCountBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.deleteCount);
        dest.writeInt(this.todoCount);
        dest.writeInt(this.receiveInCount);
        dest.writeInt(this.sendCount);
        dest.writeInt(this.receiveCount);
        dest.writeInt(this.sendInCount);
        dest.writeInt(this.ceceiveCompleteCount);
        dest.writeInt(this.waitSendCount);
        dest.writeInt(this.completeCount);
    }

    protected MailCountBean(Parcel in) {
        this.type = in.readInt();
        this.deleteCount = in.readInt();
        this.todoCount = in.readInt();
        this.receiveInCount = in.readInt();
        this.sendCount = in.readInt();
        this.receiveCount = in.readInt();
        this.sendInCount = in.readInt();
        this.ceceiveCompleteCount = in.readInt();
        this.waitSendCount = in.readInt();
        this.completeCount = in.readInt();
    }

    public static final Creator<MailCountBean> CREATOR = new Creator<MailCountBean>() {
        @Override
        public MailCountBean createFromParcel(Parcel source) {
            return new MailCountBean(source);
        }

        @Override
        public MailCountBean[] newArray(int size) {
            return new MailCountBean[size];
        }
    };
}
