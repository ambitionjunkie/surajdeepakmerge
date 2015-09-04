package com.attendanceapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Attendee extends User implements Serializable {
    private List<AttendeeEvent> eventList = new ArrayList<>();

    public Attendee() {
    }

    public Attendee(User user) {
        super(user.getUserId(),user.getUsername(),user.getSchool(),user.getUserView(),user.getUniqueCode(),user.getCreateDate(),user.getEmail(),user.getDeviceToken(),user.getStatus(),user.getUserRoles());
    }

    public List<AttendeeEvent> getEventList() {
        return eventList;
    }
    public void setEventList(List<AttendeeEvent> evlist) {
        this.eventList = evlist;
    }
}
