package com.attendanceapp.models;

import com.estimote.sdk.Beacon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

public class ClassSetup {

    private String id, className, district, code, interval, beacons = "";
    private Calendar startTime, endTime, startDate, endDate;
    private double latitude, longitude;
    private TreeSet<Integer> repeatDates = new TreeSet<>();
    private RepeatType repeatType;
    private TreeSet<SelectedDays> repeatDays = new TreeSet<>();
    private int status = 1;
    private User user;
    private ArrayList<Beacon> beaconList = new ArrayList<>();



    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public TreeSet<SelectedDays> getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(TreeSet<SelectedDays> repeatDays) {
        this.repeatDays = repeatDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TreeSet<Integer> getRepeatDates() {
        return repeatDates;
    }

    public void setRepeatDates(TreeSet<Integer> repeatDates) {
        this.repeatDates = repeatDates;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBeacons() {
        return beacons;
    }

    public void setBeacons(String beacons) {
        this.beacons = beacons;
    }


    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<Beacon> getBeaconList() {
        return beaconList;
    }

    public void setBeaconList(ArrayList<Beacon> beaconList) {
        this.beaconList = beaconList;
    }
}
