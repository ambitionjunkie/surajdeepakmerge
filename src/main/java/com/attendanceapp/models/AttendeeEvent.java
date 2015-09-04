package com.attendanceapp.models;

public class AttendeeEvent {
    private String evName, evCode;

    public AttendeeEvent(String ev1, String ev2)
    {
        evName = ev1;
        evCode = ev2;
    }

    public String getEventName(){
        return evName;
    }

    public String getEventCode(){
        return evCode;
    }
}
