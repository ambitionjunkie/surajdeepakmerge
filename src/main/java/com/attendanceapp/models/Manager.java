package com.attendanceapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Manager extends User implements Serializable {

    private List<ManagerClass> companies = new ArrayList<>();


    public Manager(User user) {
        super(user.getUserId(),user.getUsername(),user.getSchool(),user.getUserView(),user.getUniqueCode(),user.getCreateDate(),user.getEmail(),user.getDeviceToken(),user.getStatus(),user.getUserRoles());
    }

    public List<ManagerClass> getCompanies() {
        return companies;
    }

    public void setCompanies(List<ManagerClass> companies) {
        this.companies = companies;
    }
}
