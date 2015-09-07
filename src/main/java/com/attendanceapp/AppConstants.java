package com.attendanceapp;

public final class AppConstants {

    private AppConstants() {
    }

    public static final String URL_BEACON_LIST = "https://cloud.estimote.com/v1/beacons";

    /*
    * common
    */
    public static final String URL_LOGIN = "http://www.abdevs.com/attendance/Mobiles/login";
    public static final String URL_REGISTER = "http://www.abdevs.com/attendance/Mobiles/userRegistered";
    public static final String URL_ADD_ROLL = "http://www.abdevs.com/attendance/Mobiles/addRole";
    public static final String URL_GET_DATA_BY_ID = "http://www.abdevs.com/attendance/Mobiles/teacherdata";
    public static final String URL_REGISTER_FOR_NOTIFIC = "http://www.abdevs.com/attendance/Mobiles/saveRegId";
    public static final String URL_UN_REGISTER_FOR_NOTIFIC = "http://www.abdevs.com/attendance/Mobiles/saveRegId";
    public static final String URL_SEND_LOCATION_UPDATE = "http://www.abdevs.com/attendance/Mobiles/attendenceByStudent";
    public static final String URL_SEND_PRESENCE_USING_BEACONS = "http://www.abdevs.com/attendance/Mobiles/addBeacons";
    public static final String URL_FORGOT_PASSWORD_VERIFY_EMAIL = "http://www.abdevs.com/attendance/Mobiles/getquestion";
    public static final String URL_FORGOT_PASSWORD_VERIFY_QUESTION = "http://www.abdevs.com/attendance/Mobiles/forgatpassword";
    public static final String URL_FORGOT_PASSWORD_VERIFY_CODE = "http://www.abdevs.com/attendance/Mobiles/matchCode";
    public static final String URL_UPDATE_PASSWORD = "http://www.abdevs.com/attendance/Mobiles/updatePassword";
    public static final String URL_GET_LAT_LNG = "http://www.abdevs.com/attendance/Mobiles/getlatlong";


    public static final String EXTRA_IS_FIRST_TIME = "EXTRA_IS_FIRST_TIME";
    public static final String EXTRA_USER_ROLE = "EXTRA_USER_ROLE";
    public static final String EXTRA_SELECTED_INDEX = "EXTRA_SELECTED_INDEX";


    /*
    * class_id
    */
    public static final String URL_EXPORT_REPORT = "http://www.abdevs.com/attendance/Mobiles/export";

    /*
    * class_id, date
    */
    public static final String URL_CUSTOM_REPORT = "http://www.abdevs.com/attendance/Mobiles/customReport";

    /*
    * user_id,email
    */
    public static final String URL_SEND_NOTIFICATION_IF_USING_MOBILE = "http://www.abdevs.com/attendance/Mobiles/sendNotificationToTeacher";

    /*
    * Student related
    */
    public static final String URL_ADD_CLASS_BY_STUDENT = "http://www.abdevs.com/attendance/Mobiles/addClassByStudent";
    public static final String URL_ADD_PARENT_BY_STUDENT = "http://www.abdevs.com/attendance/Mobiles/addCodeForParent";
    public static final String URL_STUDENT_GET_NOTIFICATIONS_ONE_CLASS = "http://www.abdevs.com/attendance/Mobiles/studentmsglist";
    public static final String URL_STUDENT_CHECK_ATTENDANCE_SINGLE_CLASS = "http://www.abdevs.com/attendance/Mobiles/attandenceListByStudent";

    /*
    * Teacher related
    */
    public static final String URL_CREATE_CLASS = "http://www.abdevs.com/attendance/Mobiles/addClassByTeacher";
    public static final String URL_ADD_STUDENT = "http://www.abdevs.com/attendance/Mobiles/addStudent";
    public static final String URL_SEND_NOTIFICATION = "http://www.abdevs.com/attendance/Mobiles/sendPushNotificationToGCM";
    public static final String URL_TAKE_ATTENDANCE_CURRENT_LOCATION = "http://www.abdevs.com/attendance/Mobiles/studentAttendens";
    public static final String URL_SHOW_ATTENDANCE_CURRENT_LOCATION = "http://www.abdevs.com/attendance/Mobiles/attandenceList";
    public static final String URL_DELETE_STUDENT_FROM_CLASS = "http://www.abdevs.com/attendance/Mobiles/deleteStudentByTeacher";

    /*
    * class_id
    */
    public static final String URL_DELETE_CLASS_BY_TEACHER = "http://www.abdevs.com/attendance/Mobiles/deleteStudentByTeacher";

    /*
    * studentAttendance_id
    */
    public static final String URL_DELETE_STUDENT_ATTENDANCE_BY_TEACHER = "http://www.abdevs.com/attendance/Mobiles/deleteAttendanceByTeacher";

    /*
    * studentAttendance_id,attend
    * */
    public static final String URL_EDIT_STUDENT_ATTENDANCE_BY_TEACHER = "http://www.abdevs.com/attendance/Mobiles/editAttandence";

    /*
    * user_id, class_id
    */
    public static final String URL_CLASS_NOTIFICATION_LIST = "http://www.abdevs.com/attendance/Mobiles/notificationlist";

    /*
    * notification_id
    */
    public static final String URL_DELETE_NOTIFICATION = "http://www.abdevs.com/attendance/Mobiles/deleteNotification";


    /*
    * for Parents
    */
    public static final String URL_PARENT_ADD_CHILD = "http://www.abdevs.com/attendance/Mobiles/addchild";
    public static final String URL_PARENT_EDIT_CHILD = "http://www.abdevs.com/attendance/Mobiles/editChild";
    public static final String URL_PARENT_DELETE_CHILD = "http://www.abdevs.com/attendance/Mobiles/deleteChildParent";
    public static final String URL_PARENT_CHECK_CHILD_ATTENDANCE = "http://www.abdevs.com/attendance/Mobiles/attandenceListByParent";


    /*
    * shared preferences keys
    */
    public static final String SP_COMMON = "com.attendanceapp";
    public static final String KEY_LOGGED_IN_USER = SP_COMMON + "KEY_LOGGED_IN_USER";
    public static final String KEY_LOGGED_IN_USER_NOTIFICATIONS = SP_COMMON + "KEY_LOGGED_IN_USER_NOTIFICATIONS";
    public static final String KEY_LOGGED_IN_USER_DATA = SP_COMMON + "KEY_LOGGED_IN_USER_DATA";


    /*
    * notifications related
    */
    public static final String IS_NOTIFICATIONS_ON = SP_COMMON + ".IS_NOTIFICATIONS_ON";
    public static final String IS_CLASS_NOTIFICATIONS_ON = SP_COMMON + "_IS_CLASS_NOTIFICATIONS_ON_";
    public static final String TAG = "Attendance App ";
    public static final String DISPLAY_MESSAGE_ACTION = "com.attendanceapp.DISPLAY_MESSAGE";
    public static final String SENDER_ID = "848894344660";
    public static final String EXTRA_NOTIFICATION_MESSAGE = "EXTRA_NOTIFICATION_MESSAGE";
    public static final String EXTRA_BROADCAST_MESSAGE = "m";


    /*
    * event related
    */
    public static final String URL_CREATE_EVENT = "http://www.abdevs.com/attendance/Mobiles/addEventByEventHost";
    public static final String URL_ADD_ATTENDEE_TO_EVENT = "http://www.abdevs.com/attendance/Mobiles/addEventee";
    public static final String URL_ADD_EVENT_BY_ATTENDEE = "http://www.abdevs.com/attendance/Mobiles/addEventByEventee";
    public static final String URL_TAKE_ATTENDANCE_BY_EVENT_HOST = "http://www.abdevs.com/attendance/Mobiles/eventeeAttendens";
    public static final String URL_EVENTHOST_ATTENDANCE_CURRENT_LOCATION = "http://www.abdevs.com/attendance/Mobiles/event_attendencelist";
    public static final String URL_EVENTHOST_CUSTOM_REPORT = "http://www.abdevs.com/attendance/Mobiles/eventcustomReport";
    public static final String URL_EVENTHOST_EXPORT_REPORT = "http://www.abdevs.com/attendance/Mobiles/eventexport";
    /*
    *
    * corporation related
    *
    */
    public static final String URL_CREATE_COMPANY = "http://www.abdevs.com/attendance/Mobiles/addCompanyByManager";
    public static final String URL_ADD_EMPLOYEE_TO_COMPANY = "http://www.abdevs.com/attendance/Mobiles/addEmployee";
    public static final String URL_ADD_COMPANY_BY_EMPLOYEE = "http://www.abdevs.com/attendance/Mobiles/addCompanyByEmployee";
    public static final String URL_TAKE_ATTENDANCE_BY_MANAGER = "http://www.abdevs.com/attendance/Mobiles/employeeAttendens";

    // message,employee_email,meetingCode, employee_id, user_id , image_url
    public static final String URL_MANAGER_SEND_NOTIFICATION = "http://www.abdevs.com/attendance/Mobiles/sendNotificationToEmployee";

    /*
        event host / attendee
    */
    public static final String KR_ADD_EVENT_BY_HOST = "http://www.abdevs.com/attendance/attendees/addEventByHost";
    public static final String KR_ADD_EVENT_BY_ATTENDEE = "http://www.abdevs.com/attendance/attendees/addEventByAttendee";
}
