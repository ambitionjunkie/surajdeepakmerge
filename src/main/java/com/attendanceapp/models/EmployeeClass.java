package com.attendanceapp.models;

import java.io.Serializable;

/**
 * Created by aakash on 09/09/15.
 */
public class EmployeeClass implements Serializable {
    private String employeeMeetingId, managerId, meetingName, meetingUniqueCode;

    public String getEmployeeMeetingId() {
        return employeeMeetingId;
    }

    public void setEmployeeMeetingId(String employeeMeetingId) {
        this.employeeMeetingId = employeeMeetingId;
    }

    public String getMeetingUniqueCode() {
        return meetingUniqueCode;
    }

    public void setMeetingUniqueCode(String meetingUniqueCode) {
        this.meetingUniqueCode = meetingUniqueCode;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
