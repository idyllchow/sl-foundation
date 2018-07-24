package cc.seedland.oa.slfoundation;

import android.os.Parcel;
import android.os.Parcelable;

import cc.seedland.foundation.net.model.BaseResult;

/**
 * author shibo
 * date 08/02/2018
 * description
 */

public class CirculateBaseBean<T> extends BaseResult<T> implements Parcelable {

    private String code;
    private boolean success;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isOk() {
        return success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    public CirculateBaseBean() {
    }

    protected CirculateBaseBean(Parcel in) {
        this.code = in.readString();
        this.success = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CirculateBaseBean> CREATOR = new Parcelable.Creator<CirculateBaseBean>() {
        @Override
        public CirculateBaseBean createFromParcel(Parcel source) {
            return new CirculateBaseBean(source);
        }

        @Override
        public CirculateBaseBean[] newArray(int size) {
            return new CirculateBaseBean[size];
        }
    };
}
