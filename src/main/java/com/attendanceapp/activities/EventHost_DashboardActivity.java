package com.attendanceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.attendanceapp.Absent;
import com.attendanceapp.AppConstants;
import com.attendanceapp.activities.EventHost_Absent;
import com.attendanceapp.OnSwipeTouchListener;
import com.attendanceapp.R;
import com.attendanceapp.TeacherSendMessageToOneClass;
import com.attendanceapp.adapters.EventHostPagerAdapter;
import com.attendanceapp.models.Event;
import com.attendanceapp.models.User;
import com.attendanceapp.models.UserRole;
import com.attendanceapp.utils.AndroidUtils;
import com.attendanceapp.utils.DataUtils;
import com.attendanceapp.utils.GPSTracker;
import com.attendanceapp.utils.NavigationPage;
import com.attendanceapp.utils.StringUtils;
import com.attendanceapp.utils.UserUtils;
import com.attendanceapp.utils.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EventHost_DashboardActivity extends FragmentActivity implements View.OnClickListener, NavigationPage.NavigationFunctions {

    private static final String TAG = EventHost_DashboardActivity.class.getSimpleName();
    private static final int REQUEST_EDIT_EVENT = 251;


    protected ImageView addClassButton;
    private TextView oneWordTextView;
    GPSTracker gpsTracker;
    /* main page functionality */
    protected LinearLayout takeAttendanceBtn, takeAttendanceCurrentLocationBtn, studentsBtn, sendClassNotificationBtn, mainPage;

    /* for settings page functionality */
    protected ImageView settingButton;
    protected LinearLayout classInformationLayout, absenceLayout, reportsLayout, onOffNotifications, settingPage;
    protected RelativeLayout classNotificationLayout, oneWordTitleLayout;
    protected ImageView onOffNotificationImageView;


    private ScrollView swipePage;
    private EventHostPagerAdapter baseViewPagerAdapter;
    private ViewPager mViewPager;
    private Animation textAnimation;
    private FrameLayout navigationLayout;

    private UserUtils userUtils;
    protected User user;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_EVENT) {
            if (resultCode == RESULT_OK) {
                updateDataAsync(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_host_dashboard);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        swipePage = (ScrollView) findViewById(R.id.swipePage);
        addClassButton = (ImageView) findViewById(R.id.addClassButton);
        oneWordTextView = (TextView) findViewById(R.id.oneWordTitle);

        navigationLayout = (FrameLayout) findViewById(R.id.navigation);
        takeAttendanceBtn = (LinearLayout) findViewById(R.id.takeAttendanceBtn);
        takeAttendanceCurrentLocationBtn = (LinearLayout) findViewById(R.id.takeAttendanceCurrentLocationBtn);
        studentsBtn = (LinearLayout) findViewById(R.id.studentsBtn);
        sendClassNotificationBtn = (LinearLayout) findViewById(R.id.sendClassNotificationBtn);
        mainPage = (LinearLayout) findViewById(R.id.mainPage);
        oneWordTitleLayout = (RelativeLayout) findViewById(R.id.oneWordTitleLayout);

        /* for setting tab */
        settingPage = (LinearLayout) findViewById(R.id.settingPage);
        settingButton = (ImageView) findViewById(R.id.settingButton);
        classInformationLayout = (LinearLayout) findViewById(R.id.classInformationLayout);
        absenceLayout = (LinearLayout) findViewById(R.id.absenceLayout);
        reportsLayout = (LinearLayout) findViewById(R.id.reportsLayout);
        onOffNotifications = (LinearLayout) findViewById(R.id.onOffNotifications);
        classNotificationLayout = (RelativeLayout) findViewById(R.id.classNotificationLayout);
        onOffNotificationImageView = (ImageView) findViewById(R.id.onOffNotificationImageView);

        addClassButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        takeAttendanceBtn.setOnClickListener(this);
        takeAttendanceCurrentLocationBtn.setOnClickListener(this);
        studentsBtn.setOnClickListener(this);
        sendClassNotificationBtn.setOnClickListener(this);

        classInformationLayout.setOnClickListener(this);
        absenceLayout.setOnClickListener(this);
        reportsLayout.setOnClickListener(this);
        onOffNotifications.setOnClickListener(this);
        classNotificationLayout.setOnClickListener(this);

        oneWordTitleLayout.setOnTouchListener(swipeTouchListener);
        classInformationLayout.setOnTouchListener(swipeTouchListener);
        absenceLayout.setOnTouchListener(swipeTouchListener);
        reportsLayout.setOnTouchListener(swipeTouchListener);
        onOffNotifications.setOnTouchListener(swipeTouchListener);
        classNotificationLayout.setOnTouchListener(swipeTouchListener);

        settingButton.setOnTouchListener(swipeTouchListener);
        mainPage.setOnTouchListener(swipeTouchListener);
        settingPage.setOnTouchListener(swipeTouchListener);
        swipePage.setOnTouchListener(swipeTouchListener);
        takeAttendanceBtn.setOnTouchListener(swipeTouchListener);
        takeAttendanceCurrentLocationBtn.setOnTouchListener(swipeTouchListener);
        studentsBtn.setOnTouchListener(swipeTouchListener);
        sendClassNotificationBtn.setOnTouchListener(swipeTouchListener);


        userUtils = new UserUtils(EventHost_DashboardActivity.this);
        user = userUtils.getUserFromSharedPrefs();

        baseViewPagerAdapter = new EventHostPagerAdapter(getSupportFragmentManager(), user.getEventArrayList());
        mViewPager.setAdapter(baseViewPagerAdapter);

        setOneWordTextView(0);

        //noinspection deprecation
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setOneWordTextView(position);
            }
        });
        gpsTracker = new GPSTracker(getApplicationContext());
    }

    private void setOneWordTextView(int current) {
        oneWordTextView.setText("");
        if (user.getEventArrayList().size() > current) {
            oneWordTextView.setText(String.valueOf(user.getEventArrayList().get(current).getName().charAt(0)).toUpperCase());
        }
        showMessageIfNoClass();
        onOffNotificationsSetImage();
    }

    private void showMessageIfNoClass() {
        swipePage.setVisibility(user.getEventArrayList().size() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.takeAttendanceBtn:
                takeAttendance("beacons");
                break;

            case R.id.takeAttendanceCurrentLocationBtn:
                takeAttendance("gps");
                break;

            case R.id.addClassButton:
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.EXTRA_USER_ROLE, UserRole.EventHost.getRole());
                AndroidUtils.openActivity(this, EventHost_AddEventActivity.class, bundle, false);
                break;

            case R.id.settingButton:
                settingButton();
                break;

            case R.id.studentsBtn:
                studentsBtn();
                break;

            case R.id.sendClassNotificationBtn:
                sendClassNotificationBtn();
                break;

            case R.id.classInformationLayout:
                classInformationLayout();
                break;

            case R.id.absenceLayout:
                absenceLayout();
                break;

            case R.id.reportsLayout:
                reportsLayout();
                break;

            case R.id.onOffNotifications:
                onOffNotifications();
                break;

            case R.id.classNotificationLayout:
                classNotificationLayout();
                break;

        }
    }

    private void absenceLayout() {

        if (!haveStudentsInClass()) {
            makeToast("Please add attendees!");
            return;
        }

        new AsyncTask<Void, Void, String>() {
            Event teacherClass = user.getEventArrayList().get(mViewPager.getCurrentItem());
            String classId = teacherClass.getId();
            ProgressDialog dialog = new ProgressDialog(EventHost_DashboardActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("user_id", user.getUserId());
                hm.put("event_id", classId);
                hm.put("role", String.valueOf(UserRole.EventHost.getRole()));

                try {
                    return new WebUtils().post(AppConstants.URL_EVENTHOST_ATTENDANCE_CURRENT_LOCATION, hm);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);

                        if (object.has("Error")) {
                            makeToast(object.getString("Error"));
                            return;
                        }
                        Intent intent = new Intent(EventHost_DashboardActivity.this, EventHost_Absent.class);
                        intent.putExtra(EventHost_Absent.EXTRA_ATTENDANCE_DATA, result);
                        intent.putExtra(EventHost_Absent.EXTRA_TITLE, teacherClass.getName());
                        intent.putExtra(EventHost_Absent.SHOW_EDIT_BUTTON, true);
                        intent.putExtra(EventHost_Absent.EXTRA_CLASS_ID, classId);
                        intent.putExtra(EventHost_Absent.EXTRA_LIST_OPTION, EventHost_Absent.SHOW_NAME_DATE);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                        makeToast("Error in getting data");
                    }
                } else {
                    makeToast("Please check internet connection");
                }
            }
        }.execute();

    }

    private void reportsLayout() {

        if (!haveStudentsInClass()) {
            makeToast("Please add attendees!");
            return;
        }

        Intent intent = new Intent(EventHost_DashboardActivity.this, EventHost_ReportsActivity.class);
        intent.putExtra(EventHost_ReportsActivity.EXTRA_CLASS_INDEX, mViewPager.getCurrentItem());
        intent.putExtra(EventHost_ReportsActivity.EXTRA_TYPE,"eventhost");
        System.out.println("This is current item>>" + mViewPager.getCurrentItem());
        startActivity(intent);

    }

    private void onOffNotifications() {
        Event teacherClass = getTeacherClassOnThisPage();
        if (teacherClass == null) {
            return;
        }
        userUtils.toggleClassNotifications(teacherClass.getUniqueCode());
        onOffNotificationsSetImage();
    }

    private Event getTeacherClassOnThisPage() {
        return user.getEventArrayList().size() > mViewPager.getCurrentItem() ? user.getEventArrayList().get(mViewPager.getCurrentItem()) : null;
    }

    private void onOffNotificationsSetImage() {
        Event teacherClass = getTeacherClassOnThisPage();
        if (teacherClass == null) {
            return;
        }
        boolean isOn = userUtils.isClassNotificationOn(teacherClass.getUniqueCode());
        onOffNotificationImageView.setImageResource(isOn ? R.drawable.on : R.drawable.off);
    }

    private void classNotificationLayout() {
        if (!haveStudentsInClass()) {
            makeToast("Please add attendees!");
            return;
        }
        Intent intent = new Intent(EventHost_DashboardActivity.this, EventHostSendMessage.class);
        intent.putExtra(TeacherSendMessageToOneClass.EXTRA_STUDENT_CLASS_INDEX, mViewPager.getCurrentItem());
        intent.putExtra(TeacherSendMessageToOneClass.EXTRA_HIDE_MESSAGE_BOX, true);
        startActivity(intent);
    }

    private void classInformationLayout() {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.EXTRA_USER_ROLE, UserRole.EventHost.getRole());
        bundle.putInt(AppConstants.EXTRA_SELECTED_INDEX, mViewPager.getCurrentItem());
        Intent intent = new Intent(this, EventHost_AddEventActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_EDIT_EVENT);
    }

    private void sendClassNotificationBtn() {
        if (!haveStudentsInClass()) {
            makeToast("Please add attendees!");
            return;
        }
        Intent intent = new Intent(EventHost_DashboardActivity.this, EventHostSendMessage.class);
        intent.putExtra(UserSendMessageToOneClass.EXTRA_SELECTED_CLASS_INDEX, mViewPager.getCurrentItem());
        intent.putExtra(AppConstants.EXTRA_USER_ROLE, UserRole.EventHost.getRole());
        startActivity(intent);
    }

    private boolean haveStudentsInClass() {
        int viewPagerIndex = mViewPager.getCurrentItem();
        List<Event> list = user.getEventArrayList();

        Event teacherClass = list.size() > viewPagerIndex ? list.get(viewPagerIndex) : null;

        return teacherClass != null && teacherClass.getUsers().size() > 0;
    }

    private void studentsBtn() {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.EXTRA_USER_ROLE, UserRole.EventHost.getRole());
        bundle.putInt(AppConstants.EXTRA_SELECTED_INDEX, mViewPager.getCurrentItem());

        AndroidUtils.openActivity(this, ShowEventUsersActivity.class, bundle, false);
    }

    private void makeToast(String title) {
        Toast.makeText(EventHost_DashboardActivity.this, title, Toast.LENGTH_LONG).show();
    }

    private void settingButton() {
        if (settingPage.getVisibility() == View.VISIBLE) {
            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
            mainPage.setAnimation(textAnimation);
            mainPage.setVisibility(View.VISIBLE);

            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
            settingPage.setAnimation(textAnimation);
            settingPage.setVisibility(View.GONE);

            settingButton.setImageResource(R.drawable.setting);

        } else {
            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
            mainPage.setAnimation(textAnimation);
            mainPage.setVisibility(View.GONE);

            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
            settingPage.setAnimation(textAnimation);
            settingPage.setVisibility(View.VISIBLE);

            settingButton.setImageResource(R.drawable.home_blue);

        }
    }

    @Override
    public void onBackPressed() {
        if (navigationLayout.getVisibility() == View.VISIBLE) {
            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_out);
            navigationLayout.setAnimation(textAnimation);
            navigationLayout.setVisibility(View.GONE);
        } else if (settingPage.getVisibility() == View.VISIBLE) {
            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
            mainPage.setAnimation(textAnimation);
            mainPage.setVisibility(View.VISIBLE);

            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
            settingPage.setAnimation(textAnimation);
            settingPage.setVisibility(View.GONE);

            settingButton.setImageResource(R.drawable.setting);
        } else {
            super.onBackPressed();
        }
    }

    public void gotoBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user1 = userUtils.getUserFromSharedPrefs();

        if (user1 != null) {
            List<Event> mainList = user.getEventArrayList();
            List<Event> newList = user1.getEventArrayList();

            if (mainList != null && newList != null) {
                user.getEventArrayList().clear();
                user.getEventArrayList().addAll(user1.getEventArrayList());
                baseViewPagerAdapter.notifyDataSetChanged();
                setOneWordTextView(0);
            }
        }

        new NavigationPage(this, userUtils.getUserFromSharedPrefs());
        updateDataAsync(false);
    }

    private void updateDataAsync(final boolean deleteLastData) {
        new AsyncTask<Void, Void, String>() {
            private ProgressDialog progressDialog = new ProgressDialog(EventHost_DashboardActivity.this);

            @Override
            protected void onPreExecute() {
                if (deleteLastData) {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("id", user.getUserId());
                hm.put("role", String.valueOf(UserRole.EventHost.getRole()));
                try {
                    return new WebUtils().post(AppConstants.URL_GET_DATA_BY_ID, hm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (deleteLastData) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }
                if (result != null) {

                    List<Event> newList = DataUtils.getEventArrayListFromJsonString(result);

                    if (deleteLastData || (user.getEventArrayList().size() != newList.size())) {

                        user.getEventArrayList().clear();
                        user.getEventArrayList().addAll(newList);

                        userUtils.saveUserToSharedPrefs(user);

                        baseViewPagerAdapter.notifyDataSetChanged();
                        setOneWordTextView(0);
                    }
                }
            }
        }.execute();

    }

    public void takeAttendance(final String attendanceUsing) {
        final User user = userUtils.getUserFromSharedPrefs();
        final Event hostevent = user.getEventArrayList().get(mViewPager.getCurrentItem());
        final List<User> studentList = hostevent.getUsers();

        if (studentList.size() < 1) {
            makeToast("Please add attendees to take attendance");
            return;
        }

        new AsyncTask<Void, Void, String>() {
            private ProgressDialog dialog = new ProgressDialog(EventHost_DashboardActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String result = null;
                // user_id, employee_id ( more than one comma separated ), company_code, company_id, type(Manual, Automatic, ByBeacon)"
                HashMap<String, String> hm = new HashMap<>();
                hm.put("user_id", user.getUserId());
                hm.put("event_code", hostevent.getUniqueCode());
                hm.put("event_id", hostevent.getId());
                hm.put("eventee_id", StringUtils.getAllIdsFromStudentList(studentList, ','));

                if ("beacons".equalsIgnoreCase(attendanceUsing)) {
                    hm.put("type", "ByBeacon");
                } else if ("gps".equalsIgnoreCase(attendanceUsing)) {
                    hm.put("type", "Automatic");
                    if (gpsTracker.canGetLocation()) {
                        Location location = gpsTracker.getLocation();

                        if (location != null) {
                            final double latitude = location.getLatitude();
                            final double longitude = location.getLongitude();

                            hm.put("teach_lat",String.valueOf(latitude));
                            hm.put("teach_long", String.valueOf(longitude));

                        }
                    }
                    else{
                        gpsTracker.showSettingsAlert();
                    }
                }

                try {
                    result = new WebUtils().post(AppConstants.URL_TAKE_ATTENDANCE_BY_EVENT_HOST, hm);
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                dialog.cancel();

                if (result != null) {

                    if (result.contains("Error") || result.contains("error")) {
                        makeToast("Error in getting attendance!");

                    } else {
                        Intent intent;
                        intent = new Intent(EventHost_DashboardActivity.this, EventHost_AttendanceTakenActivity.class);
                        intent.putExtra(EventHost_AttendanceTakenActivity.EXTRA_SELECTED_CLASS_INDEX, mViewPager.getCurrentItem());
                        intent.putExtra(AppConstants.EXTRA_USER_ROLE, UserRole.EventHost.getRole());
                        intent.putExtra(EventHost_AttendanceTakenActivity.EXTRA_ATTENDANCE_DATA, result);

                        startActivity(intent);
                    }
                } else {
                    makeToast("Please check internet connection");
                }
            }
        }.execute();

    }

    OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener() {
        public boolean onSwipeRight() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            setOneWordTextView(mViewPager.getCurrentItem());
            return true;
        }

        public boolean onSwipeLeft() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            setOneWordTextView(mViewPager.getCurrentItem());
            return true;
        }
    };
}