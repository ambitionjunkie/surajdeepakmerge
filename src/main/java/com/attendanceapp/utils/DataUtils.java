package com.attendanceapp.utils;

import com.attendanceapp.models.Attendance;
import com.attendanceapp.models.Attendee;
import com.attendanceapp.models.ClassEventCompany;
import com.attendanceapp.models.ClassMessage;
import com.attendanceapp.models.Event;
import com.attendanceapp.models.RepeatType;
import com.attendanceapp.models.Student;
import com.attendanceapp.models.StudentClass;
import com.attendanceapp.models.TeacherClass;
import com.attendanceapp.models.User;
import com.attendanceapp.models.UserRole;
import com.estimote.sdk.Beacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DataUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private static Calendar calendar = Calendar.getInstance();

    public static User getUserFromJsonString(String jsonString) {

        User user = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject dataObject = jsonObject.getJSONObject("Data");
            JSONObject userObject = dataObject.getJSONObject("User");

            user = getUserFromJsonObject(userObject);
            if (dataObject.has("Role")) {
                user.setUserRoles(getUserRoleList(dataObject.getJSONArray("Role")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    private static User getUserFromJsonObject(JSONObject userObject) {

        User user = null;

        try {
            user = new User();

            user.setUserId(userObject.getString("id"));
            user.setUsername(userObject.getString("username"));
            user.setEmail(userObject.getString("email"));
            user.setSchool(userObject.getString("school"));
            user.setUserView(userObject.getString("userview"));
            user.setStatus(userObject.getInt("status"));
            user.setUniqueCode(userObject.getString("code"));
            user.setDeviceToken(userObject.getString("device_token"));
            user.setCreateDate(userObject.getString("created"));
            user.setSecurityQuestion(userObject.getString("question"));
            user.setSecurityQuestionAnswer(userObject.getString("answer"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    private static List<UserRole> getUserRoleList(JSONArray userRoleArray) {

        List<UserRole> list = new ArrayList<>();

        for (int i = 0; i < userRoleArray.length(); i++) {
            try {

                JSONObject object = userRoleArray.getJSONObject(i);
                list.add(UserUtils.userRoleFromString(object.getString("role")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public static List<StudentClass> getStudentClassListFromJsonString(String jsonString) {
        List<StudentClass> list = new LinkedList<>();

        try {
            JSONObject userDataJsonObject = new JSONObject(jsonString);
            JSONArray dataArray = userDataJsonObject.getJSONArray("Data");


            JSONObject zeroObject = dataArray.getJSONObject(0);
            JSONArray classArray = zeroObject.getJSONArray("Teacher");

            if (classArray != null) {
                for (int i = 0; i < classArray.length(); i++) {
                    JSONObject jsonObject = classArray.getJSONObject(i);
                    StudentClass studentClass = new StudentClass();

                    studentClass.setStudentClassId(jsonObject.getString("id"));
                    studentClass.setTeacherId(jsonObject.getString("user_id"));
                    studentClass.setClassName(jsonObject.getString("className"));
                    studentClass.setClassUniqueCode(jsonObject.getString("classCode"));

                    list.add(studentClass);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Student> getParentChildrenListFromJsonString(String jsonString) {
        ArrayList<Student> students = new ArrayList<>();

        try {
            JSONObject userDataJsonObject = new JSONObject(jsonString);
            JSONArray dataArray = userDataJsonObject.getJSONArray("Data");
            JSONObject zeroObject = dataArray.getJSONObject(0);
            JSONArray children = zeroObject.getJSONArray("Children");

            if (children != null) {
                for (int i = 0; i < children.length(); i++) {
                    try {

                        JSONObject object = children.getJSONObject(i);
                        Student student = new Student();

                        if (object.has("id")) {
                            student.setUserId(object.getString("id"));
                        }
                        if (object.has("child_name")) {
                            student.setUsername(object.getString("child_name"));
                        }
                        if (object.has("student_id")) {
                            student.setAllClassesId(object.getString("student_id"));
                        }
                        if (object.has("mainstu_id")) {
                            student.setChildIdForLocation(object.getString("mainstu_id"));
                        }
                        if (object.has("User")) {
                            JSONObject studentObject = object.getJSONObject("User");
                            if (studentObject.has("email")) {
                                student.setEmail(studentObject.getString("email"));
                            }
                        }

                        students.add(student);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static List<TeacherClass> getTeacherClassListFromJsonString(String jsonString) {
        List<TeacherClass> teacherClassList = new LinkedList<>();

        try {
            JSONObject userDataJsonObject = new JSONObject(jsonString);
            JSONArray dataArray = userDataJsonObject.getJSONArray("Data");
            JSONObject zeroObject = dataArray.getJSONObject(0);
            JSONArray classArray = zeroObject.getJSONArray("Teacher");

            if (classArray != null) {
                for (int i = 0; i < classArray.length(); i++) {
                    JSONObject jsonObject = classArray.getJSONObject(i);
                    TeacherClass teacherClass = new TeacherClass();

                    teacherClass.setId(jsonObject.getString("id"));
                    teacherClass.setTeacherId(jsonObject.getString("user_id"));
                    teacherClass.setClassName(jsonObject.getString("className"));
                    teacherClass.setStartTime(jsonObject.getString("startTime"));
                    teacherClass.setEndTime(jsonObject.getString("endTime"));
                    teacherClass.setStartDate(jsonObject.getString("startDate"));
                    teacherClass.setEndDate(jsonObject.getString("endDate"));
                    teacherClass.setRepeatType(getRepeatTypeFromString(jsonObject.getString("repeatType")));
                    teacherClass.setInterval(jsonObject.getInt("interval"));
                    teacherClass.setDistrict(jsonObject.getString("district"));
                    teacherClass.setCode(jsonObject.getString("code"));
                    teacherClass.setClassCode(jsonObject.getString("classCode"));
                    teacherClass.setLatitude(jsonObject.getDouble("latitude"));
                    teacherClass.setLongitude(jsonObject.getDouble("longitude"));
                    teacherClass.setStatus(jsonObject.getInt("status"));
                    teacherClass.setCreateDate(jsonObject.getString("created"));
                    teacherClass.setModifiedDate(jsonObject.getString("modified"));
                    teacherClass.setStudentList(getTeacherClassStudentsList(jsonObject.getJSONArray("Student")));
                    teacherClass.setBeaconList(getBeaconsList(jsonObject.getJSONArray("Beacon")));

                    teacherClassList.add(teacherClass);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teacherClassList;
    }

    private static List<Student> getTeacherClassStudentsList(final JSONArray student) {
        List<Student> students = new LinkedList<>();

        for (int i = 0; i < student.length(); i++) {
            try {
                JSONObject object = student.getJSONObject(i);
                Student s1 = new Student();

                s1.setUserId(object.getString("id"));
                s1.setStudentClassId(object.getString("teacher_id"));
                s1.setClassCode(object.getString("classCode"));
                s1.setStatus(object.getInt("status"));
                s1.setEmail(object.getString("student_email"));
                s1.setCreateDate(object.getString("created"));
                s1.setModifiedDate(object.getString("modified"));

                String parentEmails = object.getString("parent_email");
                String s[] = parentEmails.split(",");

                for (String s2 : s) {
                    s1.getParentEmailList().add(s2);
                }

                students.add(s1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return students;
    }

    private static ArrayList<Beacon> getBeaconsList(final JSONArray student) {
        ArrayList<Beacon> beacons = new ArrayList<>();

        for (int i = 0; i < student.length(); i++) {
            try {
                JSONObject o = student.getJSONObject(i);

                //public Beacon(String proximityUUID, String name, String macAddress, int major, int minor, int measuredPower, int rssi) {

                Beacon s1 = new Beacon(o.getString("proximityUUID"), o.getString("name"),
                        o.getString("macAddress"), o.getInt("major"), o.getInt("minor"), o.getInt("measuredPower"), o.getInt("rssi"));
                beacons.add(s1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return beacons;
    }

    private static RepeatType getRepeatTypeFromString(String repeatString) {
        for (RepeatType repeat_type : RepeatType.values()) {
            if (repeat_type.name().equalsIgnoreCase(repeatString)) {
                return repeat_type;
            }
        }
        return RepeatType.DAILY;
    }

    public static Student getStudentFromJsonString(String jsonString) {

        Student student = new Student();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject index = dataArray.getJSONObject(i);

                if (index.has("Teacher")) {
                    JSONObject jsonObject1 = index.getJSONObject("Teacher");
                    StudentClass studentClass = new StudentClass();

                    studentClass.setStudentClassId(jsonObject1.getString("id"));
                    studentClass.setTeacherId(jsonObject1.getString("user_id"));
                    studentClass.setClassName(jsonObject1.getString("className"));
                    studentClass.setClassUniqueCode(jsonObject1.getString("classCode"));

                    student.getStudentClassList().add(studentClass);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return student;
    }

    public static ArrayList<ClassMessage> getMessagesArrayList(String jsonString) {

        ArrayList<ClassMessage> classMessageArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
                ClassMessage classMessage = new ClassMessage();
                JSONObject index = dataArray.getJSONObject(i);
                JSONObject jsonObject1 = index.getJSONObject("Message");

                if (jsonObject1.has("user_id")) {
                    classMessage.setFrom(jsonObject1.getString("user_id"));
                }
                if (jsonObject1.has("student_id")) {
                    classMessage.setToId(jsonObject1.getString("student_id"));
                }
                if (jsonObject1.has("student_email")) {
                    classMessage.setToEmail(jsonObject1.getString("student_email"));
                }
                if (jsonObject1.has("message")) {
                    classMessage.setMessage(jsonObject1.getString("message"));
                }
                if (jsonObject1.has("class_code")) {
                    classMessage.setClassCode(jsonObject1.getString("class_code"));
                }
                if (jsonObject1.has("created")) {
                    classMessage.setTime(jsonObject1.getString("created"));
                }

                classMessageArrayList.add(classMessage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return classMessageArrayList;
    }

    public static ArrayList<ClassMessage> getNotificationsArrayList(String jsonString) {

        ArrayList<ClassMessage> classMessageArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            /* {
      "Notification": {
        "id": "1",
        "user_id": "67",
        "tech_id": "6",
        "teacher_id": "2",
        "message": "s3@m.com is using phone at this time",
        "classCode": "2c2xwa",
        "created": "2015-08-26 08:31:34"
      }
    },*/

            for (int i = 0; i < dataArray.length(); i++) {
                ClassMessage classMessage = new ClassMessage();
                JSONObject index = dataArray.getJSONObject(i);
                JSONObject jsonObject1 = index.getJSONObject("Notification");

                // for id
                if (jsonObject1.has("id")) {
                    classMessage.setId(jsonObject1.getString("id"));
                }

                // for student id
                if (jsonObject1.has("user_id")) {
                    classMessage.setFrom(jsonObject1.getString("user_id"));
                }

                // for teacher id
                if (jsonObject1.has("tech_id")) {
                    classMessage.setToId(jsonObject1.getString("tech_id"));
                }

                // for class id
                if (jsonObject1.has("teacher_id")) {
                    classMessage.setClassId(jsonObject1.getString("teacher_id"));
                }
//                if (jsonObject1.has("student_email")) {
//                    classMessage.setToEmail(jsonObject1.getString("student_email"));
//                }
                if (jsonObject1.has("message")) {
                    classMessage.setMessage(jsonObject1.getString("message"));
                }
                if (jsonObject1.has("classCode")) {
                    classMessage.setClassCode(jsonObject1.getString("classCode"));
                }
                if (jsonObject1.has("created")) {
                    classMessage.setTime(jsonObject1.getString("created"));
                }

                classMessageArrayList.add(classMessage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return classMessageArrayList;
    }

    public static String getImageNameFromPathPlusName(String pathWithName) {
        return pathWithName.substring(pathWithName.lastIndexOf('/') + 1);
    }

    public static List<Attendance> getAttendanceListFromJsonString(String jsonString) {
        List<Attendance> list = new ArrayList<>();

        /*
           "user_id": "36",                    // Teacher
            "student_id": "39",                 // Student
            "teacher_id": "28",                 // class id
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
        },*/

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
                Attendance attendance = new Attendance();
                JSONObject index = dataArray.getJSONObject(i);
                if (index.has("StudentAttendance")){
                    JSONObject jsonObject1 = index.getJSONObject("StudentAttendance");

                attendance.setId(jsonObject1.getString("id"));
                attendance.setTeacherId(jsonObject1.getString("user_id"));
                attendance.setStudentId(jsonObject1.getString("student_id"));
                attendance.setClassId(jsonObject1.getString("teacher_id"));
                attendance.setClassCode(jsonObject1.getString("classCode"));
                attendance.setIsPresent("p".equalsIgnoreCase(jsonObject1.getString("attend")));
                attendance.setCreateDate(jsonObject1.getString("created"));

                if (index.has("Student")) {
                    JSONObject jsonObject2 = index.getJSONObject("Student");
                    attendance.setParentEmail(jsonObject2.getString("parent_email"));
                    attendance.setStudentEmail(jsonObject2.getString("student_email"));
//                attendance.setUsername(jsonObject2.getString("student_name"));
                    attendance.setStudentName(jsonObject2.getString("student_email").substring(0, jsonObject2.getString("student_email").indexOf("@")));
                    attendance.setStudentImage(jsonObject2.getString("image"));
                }
            }
                else if(index.has("EmployeeAttendance")){
                    JSONObject jsonObject1 = index.getJSONObject("EmployeeAttendance");

                    attendance.setId(jsonObject1.getString("id"));
                    attendance.setTeacherId(jsonObject1.getString("user_id"));
                    attendance.setStudentId(jsonObject1.getString("employee_id"));
                    attendance.setClassId(jsonObject1.getString("company_id"));
                    attendance.setClassCode(jsonObject1.getString("companyCode"));
                    attendance.setIsPresent("p".equalsIgnoreCase(jsonObject1.getString("attend")));
                    attendance.setCreateDate(jsonObject1.getString("created"));

                   /* if (index.has("Student")) {
                        JSONObject jsonObject2 = index.getJSONObject("Student");
                        attendance.setParentEmail(jsonObject2.getString("parent_email"));
                        attendance.setStudentEmail(jsonObject2.getString("student_email"));
//                attendance.setUsername(jsonObject2.getString("student_name"));
                        attendance.setStudentName(jsonObject2.getString("student_email").substring(0, jsonObject2.getString("student_email").indexOf("@")));
                        attendance.setStudentImage(jsonObject2.getString("image"));
                    }*/
                }else if(index.has("EventeeAttendance")){
                    JSONObject jsonObject1 = index.getJSONObject("EventeeAttendance");

                    attendance.setId(jsonObject1.getString("id"));
                    attendance.setTeacherId(jsonObject1.getString("user_id"));
                    attendance.setStudentId(jsonObject1.getString("eventee_id"));
                    attendance.setClassId(jsonObject1.getString("event_id"));
                    attendance.setClassCode(jsonObject1.getString("eventCode"));
                    attendance.setIsPresent("p".equalsIgnoreCase(jsonObject1.getString("attend")));
                    attendance.setCreateDate(jsonObject1.getString("created"));

                   if (index.has("Eventee")) {
                        JSONObject jsonObject2 = index.getJSONObject("Eventee");
                        attendance.setParentEmail(jsonObject2.getString("parent_email"));
                        attendance.setStudentEmail(jsonObject2.getString("eventee_email"));
//                attendance.setUsername(jsonObject2.getString("student_name"));
                        attendance.setStudentName(jsonObject2.getString("eventee_email").substring(0, jsonObject2.getString("eventee_email").indexOf("@")));
                        attendance.setStudentImage(jsonObject2.getString("image"));
                    }
                }
                list.add(attendance);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Beacon> getBeaconListFromJson(String s) {
        List<Beacon> beacons = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(s);

/*{
    "id": "B9407F30-F5F8-466E-AFF9-25556B57FE6D:55409:46360",
    "uuid": "B9407F30-F5F8-466E-AFF9-25556B57FE6D",
    "major": 55409,
    "minor": 46360,
    "mac": "da2cb518d871",
    "settings": {
      "battery": 100,
      "interval": 950,
      "hardware": "D3.4",
      "firmware": "A3.0.1",
      "basic_power_mode": false,
      "smart_power_mode": false,
      "timezone": "America/Detroit",
      "scheduling": false,
      "scheduling_config": "0",
      "scheduling_period": "0",
      "security": false,
      "motion_detection": null,
      "conditional_broadcasting": "off",
      "latitude": null,
      "longitude": null,
      "location_id": null,
      "broadcasting_scheme": "estimote",
      "range": -12,
      "power": -12,
      "firmware_deprecated": false,
      "firmware_newest": true
    },
    "color": "mint",
    "context_id": 75318,
    "name": "mint",
    "icon": "BEACON-MINT-COCKTAIL.png",
    "battery_life_expectancy_in_days": 1021,
    "tags": []
  },

  */
            for (int i = 0; i < array.length(); i++) {
                JSONObject index = array.getJSONObject(i);

//                String id = index.getString("id");
                String proximityUUID = index.getString("uuid");
                String name = index.getString("name");
                String macAddress = index.getString("mac");
                int major = index.getInt("major");
                int minor = index.getInt("minor");
                int measuredPower = index.getInt("battery_life_expectancy_in_days");
                int rssi = 0;

                Beacon beacon = new Beacon(proximityUUID, name, macAddress, major, minor, measuredPower, rssi);
                beacons.add(beacon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return beacons;
    }

    public static TreeMap<String, List<Attendance>> getClassAttendanceMap(String jsonString) {
        TreeMap<String, List<Attendance>> map = new TreeMap<>();

        try {

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {

                Attendance attendance = new Attendance();
                JSONObject index = dataArray.getJSONObject(i);
                /*
                "user_id": "52",
                "student_id": "74",
                "teacher_id": "72",
                "classCode": "dulb28",
                "attend": "P",
                "created": "2015-07-24",
                "modified": "2015-07-24 05:45:19"
                */
                JSONObject jsonObject1 = index.getJSONObject("StudentAttendance");
                attendance.setTeacherId(jsonObject1.getString("user_id"));
                attendance.setStudentId(jsonObject1.getString("student_id"));
                attendance.setClassId(jsonObject1.getString("teacher_id"));
                attendance.setClassCode(jsonObject1.getString("classCode"));
                attendance.setIsPresent("p".equalsIgnoreCase(jsonObject1.getString("attend")));
                attendance.setCreateDate(jsonObject1.getString("created"));

                /*"Teacher": {
                    "id": "72",
                    "user_id": "52",
                    "className": "g",
                    "classCode": "dulb28",
                    "latitude": "0.0",
                    "longitude": "0.0"
                  }*/
                JSONObject jsonObject2 = index.getJSONObject("Teacher");
                String className = jsonObject2.getString("className");
                attendance.setClassName(className);

                List<Attendance> list = map.containsKey(className) ? map.get(className) : new ArrayList<Attendance>();
                list.add(attendance);

                map.put(className, list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static TreeMap<String, List<Attendance>> getReportsMap(String jsonString) {
        TreeMap<String, List<Attendance>> map = new TreeMap<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
/*
* {
      "StudentAttendance": {
        "id": "21",
        "user_id": "6",
        "student_id": "1",
        "teacher_id": "1",
        "classCode": "z0gri8",
        "attend": "P",
        "created": "2015-08-11",
        "modified": "2015-08-11 07:40:09"
      },
      "Student": {
        "id": "1",
        "teacher_id": "1",
        "user_id": "0",
        "classCode": "z0gri8",
        "status": "1",
        "parent_email": "",
        "student_email": "s1@m.com",
        "student_name": "",
        "image": "",
        "created": "2015-08-06 05:26:47",
        "modified": "2015-08-06 05:26:47"
      }
    },*/
                Attendance attendance = new Attendance();
                JSONObject index = dataArray.getJSONObject(i);

                if (index.has("StudentAttendance")) {
                    JSONObject attendanceObject = index.getJSONObject("StudentAttendance");

                    attendance.setTeacherId(attendanceObject.getString("user_id"));
                    attendance.setStudentId(attendanceObject.getString("student_id"));
                    attendance.setClassId(attendanceObject.getString("teacher_id"));
                    attendance.setClassCode(attendanceObject.getString("classCode"));
                    attendance.setIsPresent("p".equalsIgnoreCase(attendanceObject.getString("attend")));
                    attendance.setCreateDate(attendanceObject.getString("created"));

                    JSONObject studentObject = index.getJSONObject("Student");
                    String studentEmail = studentObject.getString("student_email");
                    attendance.setStudentEmail(studentEmail);

                    List<Attendance> list = map.containsKey(studentEmail) ? map.get(studentEmail) : new ArrayList<Attendance>();
                    list.add(attendance);

                    map.put(studentEmail, list);
                }else if (index.has("EventeeAttendance"))
                {
                    JSONObject attendanceObject = index.getJSONObject("EventeeAttendance");

                    attendance.setTeacherId(attendanceObject.getString("user_id"));
                    attendance.setStudentId(attendanceObject.getString("eventee_id"));
                    attendance.setClassId(attendanceObject.getString("event_id"));
                    attendance.setClassCode(attendanceObject.getString("eventCode"));
                    attendance.setIsPresent("p".equalsIgnoreCase(attendanceObject.getString("attend")));
                    attendance.setCreateDate(attendanceObject.getString("created"));

                    JSONObject studentObject = index.getJSONObject("Eventee");
                    String studentEmail = studentObject.getString("eventee_email");
                    attendance.setStudentEmail(studentEmail);

                    List<Attendance> list = map.containsKey(studentEmail) ? map.get(studentEmail) : new ArrayList<Attendance>();
                    list.add(attendance);

                    map.put(studentEmail, list);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TreeMap<String, List<Attendance>> finalTreeMap = new TreeMap<>();

        for (Map.Entry<String, List<Attendance>> attendances : map.entrySet()) {
            finalTreeMap.put(attendances.getKey() + " (" + attendances.getValue().size() + ")", attendances.getValue());
        }

        return finalTreeMap;
    }


    public static ArrayList<ClassEventCompany> getClassEventCompanyArrayListFromJsonString(String jsonString) {

        // {"Data":[{"Event":[]}]}

        ArrayList<ClassEventCompany> teacherClassList = new ArrayList<>();

        try {
            JSONObject userDataJsonObject = new JSONObject(jsonString);
            JSONArray dataArray = userDataJsonObject.getJSONArray("Data");
            JSONObject zeroObject = dataArray.getJSONObject(0);

            JSONArray classArray;

            if (zeroObject.has("Event")) {
                classArray = zeroObject.getJSONArray("Event");

            } else if (zeroObject.has("Company")) {
                classArray = zeroObject.getJSONArray("Company");

            } else {
                classArray = zeroObject.getJSONArray("Class");
            }

            if (classArray != null) {
                for (int i = 0; i < classArray.length(); i++) {
                    JSONObject jsonObject = classArray.getJSONObject(i);
                    ClassEventCompany teacherClass = new ClassEventCompany();

                    teacherClass.setId(jsonObject.getString("id"));
                    teacherClass.getMaker().setUserId(jsonObject.getString("user_id"));

                    if (jsonObject.has("eventName")) {
                        teacherClass.setName(jsonObject.getString("eventName"));
                    } else if (jsonObject.has("companyName")) {
                        teacherClass.setName(jsonObject.getString("companyName"));
                    } else if (jsonObject.has("className")) {
                        teacherClass.setName(jsonObject.getString("className"));
                    }

//                    if (!jsonObject.has("companyName")) {
                    simpleDateFormat.applyPattern("HH:mm:ss");
                    /*"startTime": "04:30:00",
                      "endTime": "04:30:00",
                      "startDate": "2015-09-01",
                      "endDate": "2015-09-01",
                    */
                    if (jsonObject.has("startTime")) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("startTime")));
                        teacherClass.setStartTime(calendar);
                    }
                    if (jsonObject.has("endTime")) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("endTime")));
                        teacherClass.setEndTime(calendar);
                    }

                    simpleDateFormat.applyPattern("yyyy-MM-dd");

                    if (jsonObject.has("startDate") && !"null".equalsIgnoreCase(jsonObject.getString("startDate"))) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("startDate")));
                        teacherClass.setStartDate(calendar);
                    }

                    if (jsonObject.has("endDate") && !"null".equalsIgnoreCase(jsonObject.getString("endDate"))) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("endDate")));
                        teacherClass.setEndDate(calendar);
                    }
//                    }
                    teacherClass.setRepeatType(getRepeatTypeFromString(jsonObject.getString("repeatType")));
                    teacherClass.setInterval(jsonObject.getInt("interval"));
                    teacherClass.setDistrict(jsonObject.getString("district"));
                    teacherClass.setCode(jsonObject.getString("code"));

                    if (jsonObject.has("classCode")) {
                        teacherClass.setUniqueCode(jsonObject.getString("classCode"));
                    } else if (jsonObject.has("companyCode")) {
                        teacherClass.setUniqueCode(jsonObject.getString("meetingCode"));
                        teacherClass.setCode(jsonObject.getString("companyCode"));
                    } else if (jsonObject.has("eventCode")) {
                        teacherClass.setUniqueCode(jsonObject.getString("eventCode"));
                    }


                    teacherClass.setLatitude(jsonObject.getDouble("latitude"));
                    teacherClass.setLongitude(jsonObject.getDouble("longitude"));
                    teacherClass.setStatus(jsonObject.getInt("status"));
                    teacherClass.setCreateDate(jsonObject.getString("created"));
                    teacherClass.setModifiedDate(jsonObject.getString("modified"));

                    if (jsonObject.has("Beacon")) {
                        teacherClass.setBeaconList(getBeaconsList(jsonObject.getJSONArray("Beacon")));
                    }


                    if (jsonObject.has("Student")) {
                        //classEventCompany.setUsers(getTeacherClassStudentsList(jsonObject.getJSONArray("Student")));
                    } else if (jsonObject.has("Eventee")) {
                        //classEventCompany.setUsers(getTeacherClassStudentsList(jsonObject.getJSONArray("Eventee")));
                    } else if (jsonObject.has("Employee")) {
                        teacherClass.setUsers(getUserListInClassEventCompany(jsonObject.getJSONArray("Employee")));
                    }


                    teacherClassList.add(teacherClass);
                }
            }

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return teacherClassList;
    }


    private static ArrayList<User> getUserListInClassEventCompany(final JSONArray student) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < student.length(); i++) {
            try {
                JSONObject object = student.getJSONObject(i);
                User user = new User();

                user.setUserId(object.getString("id"));
                user.getParentEmailList().add(object.getString("parent_email"));
                user.setStatus(object.getInt("status"));
                user.setCreateDate(object.getString("created"));
                user.setImageName(object.getString("image"));

                String email = "";
                if (object.has("student_email")) {
                    email = object.getString("student_email");
                } else if (object.has("employee_email")) {
                    email = object.getString("employee_email");
                } else if (object.has("employee_email")) {
                    email = object.getString("employee_email");
                }
                user.setEmail(email);

                users.add(user);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    private static ArrayList<User> getUserListInEvent(final JSONArray student) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < student.length(); i++) {
            try {
                JSONObject object = student.getJSONObject(i);
                User user = new User();

                user.setUserId(object.getString("id"));
                user.getParentEmailList().add(object.getString("parent_email"));
                user.setStatus(object.getInt("status"));
                user.setCreateDate(object.getString("created"));
                user.setImageName(object.getString("image"));

                String email = "";
                if (object.has("student_email")) {
                    email = object.getString("student_email");
                } else if (object.has("employee_email")) {
                    email = object.getString("employee_email");
                } else if (object.has("eventee_email")) {
                    email = object.getString("eventee_email");
                }
                user.setEmail(email);

                users.add(user);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public static Attendee getAttendeeFromJsonString(String jsonString) {

        Attendee student = new Attendee();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject index = dataArray.getJSONObject(i);

                if (index.has("Event")) {
                    JSONObject jsonObject1 = index.getJSONObject("Event");
                    Event studentClass = new Event();

                    studentClass.setId(jsonObject1.getString("id"));
                    //studentClass.set(jsonObject1.getString("user_id"));
                    studentClass.setName(jsonObject1.getString("eventName"));
                    studentClass.setUniqueCode(jsonObject1.getString("eventCode"));

                    student.getEventArrayList().add(studentClass);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return student;
    }

    public static ArrayList<Event> getEventArrayListFromJsonString(String jsonString) {

        // {"Data":[{"Event":[]}]}

        ArrayList<Event> teacherClassList = new ArrayList<>();

        try {
            JSONObject userDataJsonObject = new JSONObject(jsonString);
            JSONArray dataArray = userDataJsonObject.getJSONArray("Data");
            JSONObject zeroObject = dataArray.getJSONObject(0);

            JSONArray classArray;

            if (zeroObject.has("Event")) {
                classArray = zeroObject.getJSONArray("Event");

            } else if (zeroObject.has("Company")) {
                classArray = zeroObject.getJSONArray("Company");

            } else {
                classArray = zeroObject.getJSONArray("Class");
            }

            if (classArray != null) {
                for (int i = 0; i < classArray.length(); i++) {
                    JSONObject jsonObject = classArray.getJSONObject(i);
                    Event teacherClass = new Event();

                    teacherClass.setId(jsonObject.getString("id"));
                    teacherClass.getMaker().setUserId(jsonObject.getString("user_id"));

                    if (jsonObject.has("eventName")) {
                        teacherClass.setName(jsonObject.getString("eventName"));
                    } else if (jsonObject.has("companyName")) {
                        teacherClass.setName(jsonObject.getString("companyName"));
                    } else if (jsonObject.has("className")) {
                        teacherClass.setName(jsonObject.getString("className"));
                    }

//                    if (!jsonObject.has("companyName")) {
                    simpleDateFormat.applyPattern("HH:mm:ss");
                    /*"startTime": "04:30:00",
                      "endTime": "04:30:00",
                      "startDate": "2015-09-01",
                      "endDate": "2015-09-01",
                    */
                    if (jsonObject.has("startTime")) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("startTime")));
                        teacherClass.setStartTime(calendar);
                    }
                    if (jsonObject.has("endTime")) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("endTime")));
                        teacherClass.setEndTime(calendar);
                    }

                    simpleDateFormat.applyPattern("yyyy-MM-dd");

                    if (jsonObject.has("startDate") && !"null".equalsIgnoreCase(jsonObject.getString("startDate"))) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("startDate")));
                        teacherClass.setStartDate(calendar);
                    }

                    if (jsonObject.has("endDate") && !"null".equalsIgnoreCase(jsonObject.getString("endDate"))) {
                        calendar.setTime(simpleDateFormat.parse(jsonObject.getString("endDate")));
                        teacherClass.setEndDate(calendar);
                    }
//                    }
                    teacherClass.setRepeatType(getRepeatTypeFromString(jsonObject.getString("repeatType")));
                    teacherClass.setInterval(jsonObject.getInt("interval"));
                    teacherClass.setDistrict(jsonObject.getString("district"));
                    teacherClass.setCode(jsonObject.getString("code"));

                    if (jsonObject.has("classCode")) {
                        teacherClass.setUniqueCode(jsonObject.getString("classCode"));
                    } else if (jsonObject.has("companyCode")) {
                        teacherClass.setUniqueCode(jsonObject.getString("meetingCode"));
                        teacherClass.setCode(jsonObject.getString("companyCode"));
                    } else if (jsonObject.has("eventCode")) {
                        teacherClass.setUniqueCode(jsonObject.getString("eventCode"));
                    }


                    teacherClass.setLatitude(jsonObject.getDouble("latitude"));
                    teacherClass.setLongitude(jsonObject.getDouble("longitude"));
                    teacherClass.setStatus(jsonObject.getInt("status"));
                    teacherClass.setCreateDate(jsonObject.getString("created"));
                    teacherClass.setModifiedDate(jsonObject.getString("modified"));

                    if (jsonObject.has("Beacon")) {
                        teacherClass.setBeaconList(getBeaconsList(jsonObject.getJSONArray("Beacon")));
                    }


                    if (jsonObject.has("Student")) {
                        //classEventCompany.setUsers(getTeacherClassStudentsList(jsonObject.getJSONArray("Student")));
                    } else if (jsonObject.has("Eventee")) {
                        teacherClass.setUsers(getUserListInEvent(jsonObject.getJSONArray("Eventee")));
                    } else if (jsonObject.has("Employee")) {
                        teacherClass.setUsers(getUserListInClassEventCompany(jsonObject.getJSONArray("Employee")));
                    }


                    teacherClassList.add(teacherClass);
                }
            }

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return teacherClassList;
    }
}
