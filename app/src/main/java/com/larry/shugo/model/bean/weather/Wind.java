package com.larry.shugo.model.bean.weather;

/**
 * 风力
 */
public class Wind {

    private String deg;
    private String dir;
    private String sc;
    private String spd;

    public String getDeg() {
        return deg;
    }

    public String getSc() {
        return sc;
    }

    public String getDir() {
        return dir;
    }

    public String getSpd() {
        return spd;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public void setSpd(String spd) {
        this.spd = spd;
    }
}
