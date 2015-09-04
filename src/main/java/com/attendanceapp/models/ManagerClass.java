package com.attendanceapp.models;

import com.estimote.sdk.Beacon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by aakash on 05/09/15.
 */
public class ManagerClass implements Serializable{
    private User user;
    private String id, managerId, className, district, code, classCode, startTime, endTime, startDate, endDate, createDate, modifiedDate;
    private List<Student> employeeList = new ArrayList<>();
    private transient ArrayList<Beacon> beaconList = new ArrayList<>();
    private Double latitude, longitude;
    private Integer status, interval;
    private RepeatType repeatType;
    private TreeSet<Integer> repeatDates = new TreeSet<>();
    private TreeSet<SelectedDays> repeatDays = new TreeSet<>();
}
