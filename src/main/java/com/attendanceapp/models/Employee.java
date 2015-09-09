package com.attendanceapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee extends User implements Serializable {

    private String parentEmail, childIdForLocation, studentClassId, modifiedDate, classCode;

    private List<EmployeeClass> employeeMeetingList = new ArrayList<>();

    public Employee() {
    }
    public Employee(User user) {
        super(user.getUserId(),user.getUsername(),user.getSchool(),user.getUserView(),user.getUniqueCode(),user.getCreateDate(),user.getEmail(),user.getDeviceToken(),user.getStatus(),user.getUserRoles());
    }

    public List<EmployeeClass> getStudentClassList() {
        return employeeMeetingList;
    }

    public void setStudentClassList(List<EmployeeClass> employeeMeetingList) {
        this.employeeMeetingList = employeeMeetingList;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getChildIdForLocation() {
        return childIdForLocation;
    }

    public void setChildIdForLocation(String childIdForLocation) {
        this.childIdForLocation = childIdForLocation;
    }

    public String getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(String studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}


