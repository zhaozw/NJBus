package com.renyu.nj_tran.model;

import java.io.Serializable;

/**
 * Created by renyu on 15/8/28.
 */
public class CurrentPositionModel implements Serializable {

    /**
     * Spd : 6.0930
     * Id : 337039
     * Gps : {"lat":32.093033,"lon":118.7702}
     * Dis : 358.5
     * Level : 4
     * T : 3
     * Off : 1
     */

    private String Spd;
    private String Id;
    /**
     * lat : 32.093033
     * lon : 118.7702
     */

    private GpsEntity Gps;
    private double Dis;
    private int Level;
    private int T;
    private int Off;

    public void setSpd(String Spd) {
        this.Spd = Spd;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setGps(GpsEntity Gps) {
        this.Gps = Gps;
    }

    public void setDis(double Dis) {
        this.Dis = Dis;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public void setT(int T) {
        this.T = T;
    }

    public void setOff(int Off) {
        this.Off = Off;
    }

    public String getSpd() {
        return Spd;
    }

    public String getId() {
        return Id;
    }

    public GpsEntity getGps() {
        return Gps;
    }

    public double getDis() {
        return Dis;
    }

    public int getLevel() {
        return Level;
    }

    public int getT() {
        return T;
    }

    public int getOff() {
        return Off;
    }

    public static class GpsEntity implements Serializable {
        private double lat;
        private double lon;

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
