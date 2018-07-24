package cc.seedland.oa.slfoundation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by idyll on 2017/11/17.
 */

public class VersionCheckBean implements Parcelable {

    public int code;
    public String version;
    public int upgrade_type;
    public String url;
    public String msg;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.version);
        dest.writeInt(this.upgrade_type);
        dest.writeString(this.url);
        dest.writeString(this.msg);
    }

    public VersionCheckBean() {
    }

    protected VersionCheckBean(Parcel in) {
        this.code = in.readInt();
        this.version = in.readString();
        this.upgrade_type = in.readInt();
        this.url = in.readString();
        this.msg = in.readString();
    }

    public static final Parcelable.Creator<VersionCheckBean> CREATOR = new Parcelable.Creator<VersionCheckBean>() {
        @Override
        public VersionCheckBean createFromParcel(Parcel source) {
            return new VersionCheckBean(source);
        }

        @Override
        public VersionCheckBean[] newArray(int size) {
            return new VersionCheckBean[size];
        }
    };
}
