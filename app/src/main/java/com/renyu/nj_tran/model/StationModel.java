package com.renyu.nj_tran.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by renyu on 15/8/28.
 */
public class StationModel implements Parcelable {
    int st_id=0;
    String st_name="";
    double st_real_lat=0;
    double st_real_lon=0;
    int st_pos=0;
    ArrayList<LineModel> lineModels=null;

    public int getSt_id() {
        return st_id;
    }

    public void setSt_id(int st_id) {
        this.st_id = st_id;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public double getSt_real_lat() {
        return st_real_lat;
    }

    public void setSt_real_lat(double st_real_lat) {
        this.st_real_lat = st_real_lat;
    }

    public double getSt_real_lon() {
        return st_real_lon;
    }

    public void setSt_real_lon(double st_real_lon) {
        this.st_real_lon = st_real_lon;
    }

    public int getSt_pos() {
        return st_pos;
    }

    public void setSt_pos(int st_pos) {
        this.st_pos = st_pos;
    }

    public ArrayList<LineModel> getLineModels() {
        return lineModels;
    }

    public void setLineModels(ArrayList<LineModel> lineModels) {
        this.lineModels = lineModels;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.st_id);
        dest.writeString(this.st_name);
        dest.writeDouble(this.st_real_lat);
        dest.writeDouble(this.st_real_lon);
        dest.writeInt(this.st_pos);
        dest.writeTypedList(lineModels);
    }

    public StationModel() {
    }

    protected StationModel(Parcel in) {
        this.st_id = in.readInt();
        this.st_name = in.readString();
        this.st_real_lat = in.readDouble();
        this.st_real_lon = in.readDouble();
        this.st_pos = in.readInt();
        this.lineModels = in.createTypedArrayList(LineModel.CREATOR);
    }

    public static final Creator<StationModel> CREATOR = new Creator<StationModel>() {
        public StationModel createFromParcel(Parcel source) {
            return new StationModel(source);
        }

        public StationModel[] newArray(int size) {
            return new StationModel[size];
        }
    };
}
