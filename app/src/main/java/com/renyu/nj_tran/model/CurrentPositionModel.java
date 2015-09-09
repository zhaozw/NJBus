package com.renyu.nj_tran.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by renyu on 15/8/28.
 */
public class CurrentPositionModel {

    /**
     * status : ok
     * buses : [{"id":"331901","speed":"0.0000","st_real_lat":32.067008973955,"st_real_lon":118.77109507474,"line_code":780,"line_interval":0,"updown_type":0,"st_id":2841,"st_level":15,"st_dis":305.5,"offline":1,"updated":7},{"id":"331886","speed":"11.482","st_real_lat":32.048851689328,"st_real_lon":118.76980517261,"line_code":780,"line_interval":0,"updown_type":0,"st_id":857,"st_level":19,"st_dis":59.4,"offline":1,"updated":12},{"id":"331884","speed":"0.0000","st_real_lat":32.02820646409,"st_real_lon":118.74376262179,"line_code":780,"line_interval":0,"updown_type":0,"st_id":3746,"st_level":28,"st_dis":132.2,"offline":1,"updated":6},{"id":"331898","speed":"0.0000","st_real_lat":32.028412330298,"st_real_lon":118.74356953345,"line_code":780,"line_interval":0,"updown_type":0,"st_id":3746,"st_level":28,"st_dis":155.3,"offline":1,"updated":6}]
     */

    private String status;
    private List<BusesEntity> buses;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBuses(List<BusesEntity> buses) {
        this.buses = buses;
    }

    public String getStatus() {
        return status;
    }

    public List<BusesEntity> getBuses() {
        return buses;
    }

    public static class BusesEntity implements Parcelable {
        /**
         * id : 331901
         * speed : 0.0000
         * st_real_lat : 32.067008973955
         * st_real_lon : 118.77109507474
         * line_code : 780
         * line_interval : 0
         * updown_type : 0
         * st_id : 2841
         * st_level : 15
         * st_dis : 305.5
         * offline : 1
         * updated : 7
         */

        private String id;
        private String speed;
        private double st_real_lat;
        private double st_real_lon;
        private int line_code;
        private int line_interval;
        private int updown_type;
        private int st_id;
        private int st_level;
        private double st_dis;
        private int offline;
        private int updated;

        public void setId(String id) {
            this.id = id;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public void setSt_real_lat(double st_real_lat) {
            this.st_real_lat = st_real_lat;
        }

        public void setSt_real_lon(double st_real_lon) {
            this.st_real_lon = st_real_lon;
        }

        public void setLine_code(int line_code) {
            this.line_code = line_code;
        }

        public void setLine_interval(int line_interval) {
            this.line_interval = line_interval;
        }

        public void setUpdown_type(int updown_type) {
            this.updown_type = updown_type;
        }

        public void setSt_id(int st_id) {
            this.st_id = st_id;
        }

        public void setSt_level(int st_level) {
            this.st_level = st_level;
        }

        public void setSt_dis(double st_dis) {
            this.st_dis = st_dis;
        }

        public void setOffline(int offline) {
            this.offline = offline;
        }

        public void setUpdated(int updated) {
            this.updated = updated;
        }

        public String getId() {
            return id;
        }

        public String getSpeed() {
            return speed;
        }

        public double getSt_real_lat() {
            return st_real_lat;
        }

        public double getSt_real_lon() {
            return st_real_lon;
        }

        public int getLine_code() {
            return line_code;
        }

        public int getLine_interval() {
            return line_interval;
        }

        public int getUpdown_type() {
            return updown_type;
        }

        public int getSt_id() {
            return st_id;
        }

        public int getSt_level() {
            return st_level;
        }

        public double getSt_dis() {
            return st_dis;
        }

        public int getOffline() {
            return offline;
        }

        public int getUpdated() {
            return updated;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.speed);
            dest.writeDouble(this.st_real_lat);
            dest.writeDouble(this.st_real_lon);
            dest.writeInt(this.line_code);
            dest.writeInt(this.line_interval);
            dest.writeInt(this.updown_type);
            dest.writeInt(this.st_id);
            dest.writeInt(this.st_level);
            dest.writeDouble(this.st_dis);
            dest.writeInt(this.offline);
            dest.writeInt(this.updated);
        }

        public BusesEntity() {
        }

        protected BusesEntity(Parcel in) {
            this.id = in.readString();
            this.speed = in.readString();
            this.st_real_lat = in.readDouble();
            this.st_real_lon = in.readDouble();
            this.line_code = in.readInt();
            this.line_interval = in.readInt();
            this.updown_type = in.readInt();
            this.st_id = in.readInt();
            this.st_level = in.readInt();
            this.st_dis = in.readDouble();
            this.offline = in.readInt();
            this.updated = in.readInt();
        }

        public static final Parcelable.Creator<BusesEntity> CREATOR = new Parcelable.Creator<BusesEntity>() {
            public BusesEntity createFromParcel(Parcel source) {
                return new BusesEntity(source);
            }

            public BusesEntity[] newArray(int size) {
                return new BusesEntity[size];
            }
        };
    }
}
