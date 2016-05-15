package com.test.Entity;

/**
 * Created by Administrator on 2016/4/7.
 */
public class Nearby_sites {
    private int id;
    private String name;
    private String busid;
    private String distance;

    public Nearby_sites() {

    }

    public Nearby_sites(int id, String name, String busid, String distance) {
        this.id = id;
        this.name = name;
        this.busid = busid;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBusid() {
        return busid;
    }

    public String getDistance() {
        return distance;
    }


}
