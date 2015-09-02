package com.attendanceapp.models;

import java.io.Serializable;

public class Attendance implements Serializable {
    /*
            {
                "StudentAttendance": {
                    "id": "122",
                    "user_id": "36",
                    "student_id": "34",
                    "teacher_id": "28",
                    "classCode": "CC55783e31b03f5",
                    "attend": "A",
                    "created": "2015-06-19",
                    "modified": "2015-06-19 06:05:51"
                },
                "Student": {
                    "id": "34",
                    "teacher_id": "28",
                    "user_id": "0",
                    "classCode": "CC55783e31b03f5",
                    "status": "1",
                    "parent_email": "",
                    "student_email": "asdf@gmail.coma",
                    "student_name": "",
                    "image": "",
                    "created": "2015-06-11 01:04:22",
                    "modified": "2015-06-11 01:04:22"
                }
            },
    */
    private User user;
    private ClassEventCompany classEventCompany;
    private String id, classId, studentId, teacherId, className, classCode, createDate, studentEmail, parentEmail, studentName, studentImage;
    private boolean isPresent;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public User getUser() {
        return user;
    }

    public ClassEventCompany getClassEventCompany() {
        return classEventCompany;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setClassEventCompany(ClassEventCompany classEventCompany) {
        this.classEventCompany = classEventCompany;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
