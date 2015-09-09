package com.renyu.nj_tran.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by renyu on 15/8/28.
 */
public class LineModel implements Parcelable {
    int line_id=0;
    int line_code=0;
    String line_name="";
    int updown_type=0;
    int st_start_id=0;
    int st_end_id=0;
    String bus_start="";
    String bus_end="";

    public int getLine_id() {
        return line_id;
    }

    public void setLine_id(int line_id) {
        this.line_id = line_id;
    }

    public int getLine_code() {
        return line_code;
    }

    public void setLine_code(int line_code) {
        this.line_code = line_code;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public LineModel() {
    }

    public int getUpdown_type() {
        return updown_type;
    }

    public void setUpdown_type(int updown_type) {
        this.updown_type = updown_type;
    }

    public int getSt_start_id() {
        return st_start_id;
    }

    public void setSt_start_id(int st_start_id) {
        this.st_start_id = st_start_id;
    }

    public int getSt_end_id() {
        return st_end_id;
    }

    public void setSt_end_id(int st_end_id) {
        this.st_end_id = st_end_id;
    }

    public String getBus_start() {
        return bus_start;
    }

    public void setBus_start(String bus_start) {
        this.bus_start = bus_start;
    }

    public String getBus_end() {
        return bus_end;
    }

    public void setBus_end(String bus_end) {
        this.bus_end = bus_end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.line_id);
        dest.writeInt(this.line_code);
        dest.writeString(this.line_name);
        dest.writeInt(this.updown_type);
        dest.writeInt(this.st_start_id);
        dest.writeInt(this.st_end_id);
        dest.writeString(this.bus_start);
        dest.writeString(this.bus_end);
    }

    protected LineModel(Parcel in) {
        this.line_id = in.readInt();
        this.line_code = in.readInt();
        this.line_name = in.readString();
        this.updown_type = in.readInt();
        this.st_start_id = in.readInt();
        this.st_end_id = in.readInt();
        this.bus_start = in.readString();
        this.bus_end = in.readString();
    }

    public static final Creator<LineModel> CREATOR = new Creator<LineModel>() {
        public LineModel createFromParcel(Parcel source) {
            return new LineModel(source);
        }

        public LineModel[] newArray(int size) {
            return new LineModel[size];
        }
    };
}
