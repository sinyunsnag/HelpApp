package com.helpapplication.http.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by massivcode on 2017-02-25.
 */

public class Info {
    // 보호자 전화번호
    private String receiver;
    // 사용자 전화번호
    private String caller;
    // 발신 위치 리스트
    private List<LatLng> location;
    // 차량번호
    private String car_number;
    // 발신 시각
    private long time;

    public Info(String receiver, String caller, List<LatLng> location, String car_number, long time) {
        this.receiver = receiver;
        this.caller = caller;
        this.location = location;
        this.car_number = car_number;
        this.time = time;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public List<LatLng> getLocation() {
        return location;
    }

    public void setLocation(List<LatLng> location) {
        this.location = location;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Info{" +
                "receiver='" + receiver + '\'' +
                ", caller='" + caller + '\'' +
                ", location=" + location +
                ", car_number='" + car_number + '\'' +
                ", time=" + time +
                '}';
    }
}
