package com.attendanceapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.attendanceapp.AppConstants;
import com.attendanceapp.activities.Attendee_NotificationActivity;
import com.attendanceapp.OnSwipeTouchListener;
import com.attendanceapp.R;
import com.attendanceapp.StudentAddClassActivity;
import com.attendanceapp.StudentCheckAttendanceActivity;
import com.attendanceapp.StudentNotificationActivity;
import com.attendanceapp.adapters.BaseViewPagerAdapter;
import com.attendanceapp.adapters.EventHostPagerAdapter;
import com.attendanceapp.models.Event;
import com.attendanceapp.models.Student;
import com.attendanceapp.models.User;
import com.attendanceapp.models.UserRole;
import com.attendanceapp.utils.AndroidUtils;
import com.attendanceapp.utils.DataUtils;
import com.attendanceapp.utils.NavigationPage;
import com.attendanceapp.utils.UserUtils;
import com.attendanceapp.utils.WebUtils;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendee_DashboardActivity extends FragmentActivity implements View.OnClickListener, NavigationPage.NavigationFunctions {

    private static final String TAG = Attendee_DashboardActivity.class.getSimpleName();

    protected ImageView addClassButton;
    protected TextView oneWordTextView, totalNotificationsTextView;
    RelativeLayout classNotificationLayout;
    ScrollView swipePage;
    LinearLayout attendanceLayout, mainPage;
    ViewPager mViewPager;
    private FrameLayout navigationLayout;

    /* for settings layout */
    ImageView settingButton;
    protected LinearLayout classInformationLayout, settingPage;

    EventHostPagerAdapter mStudentPagerAdapter;
    protected SharedPreferences sharedPreferences;
    Animation textAnimation;

    User user;
    protected UserUtils userUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_dashboard);

        navigationLayout = (FrameLayout) findViewById(R.id.navigation);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        addClassButton = (ImageView) findViewById(R.id.addClassButton);
        oneWordTextView = (TextView) findViewById(R.id.oneWordTitle);
        attendanceLayout = (LinearLayout) findViewById(R.id.attendanceLayout);
        classNotificationLayout = (RelativeLayout) findViewById(R.id.classNotificationLayout);
        totalNotificationsTextView = (TextView) findViewById(R.id.totalNotificationsTextView);
        swipePage = (ScrollView) findViewById(R.id.swipePage);
        mainPage = (LinearLayout) findViewById(R.id.mainPage);

        /* for setting tab */
        settingButton = (ImageView) findViewById(R.id.settingButton);
        classInformationLayout = (LinearLayout) findViewById(R.id.classInformationLayout);
        settingButton.setOnClickListener(this);
        settingPage = (LinearLayout) findViewById(R.id.settingPage);
        settingPage.setOnTouchListener(swipeTouchListener);
        classInformationLayout.setOnClickListener(this);
        classInformationLayout.setOnTouchListener(swipeTouchListener);

        addClassButton.setOnClickListener(this);
        classNotificationLayout.setOnClickListener(this);
        attendanceLayout.setOnClickListener(this);

        mainPage.setOnTouchListener(swipeTouchListener);
        settingButton.setOnTouchListener(swipeTouchListener);
        swipePage.setOnTouchListener(swipeTouchListener);
        attendanceLayout.setOnTouchListener(swipeTouchListener);
        classNotificationLayout.setOnTouchListener(swipeTouchListener);

        sharedPreferences = AndroidUtils.getCommonSharedPrefs(getApplicationContext());
        userUtils = new UserUtils(Attendee_DashboardActivity.this);
        user = userUtils.getUserFromSharedPrefs();

        mStudentPagerAdapter = new EventHostPagerAdapter(getSupportFragmentManager(), user.getEventArrayList());
        mViewPager.setAdapter(mStudentPagerAdapter);

        setOneWordTextView(0);


        //noinspection deprecation
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setOneWordTextView(position);
            }
        });

    }

    private void setOneWordTextView(int current) {
        oneWordTextView.setText("");

        if (user.getEventArrayList().size() > current) {
            oneWordTextView.setText(String.valueOf(user.getEventArrayList().get(current).getName().charAt(0)).toUpperCase());
        }

        showMessageIfNoClass();
        setNotificationBadge(current);
    }

    private void showMessageIfNoClass() {
        swipePage.setVisibility(user.getEventArrayList().size() < 1 ? View.GONE : View.VISIBLE);
    }

    private void setNotificationBadge(int classIndex) {
        if (classIndex >= user.getEventArrayList().size()) {
            return;
        }
        final Event studentClass = user.getEventArrayList().get(classIndex);

        int total = sharedPreferences.getInt(user.getUserId() + studentClass.getUniqueCode(), 0);
        if (total > 0) {
            totalNotificationsTextView.setVisibility(View.VISIBLE);
            totalNotificationsTextView.setText(String.valueOf(total));
        } else {
            totalNotificationsTextView.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.attendanceLayout:
                attendanceLayout();
                break;
            case R.id.classNotificationLayout:
                classNotificationLayout();
                break;
            case R.id.addClassButton:
                addClassButton();
                break;
            case R.id.settingButton:
                settingButton();
                break;
            case R.id.classInformationLayout:
                classInformationLayout();
                break;

        }
    }

    private void classInformationLayout() {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.EXTRA_USER_ROLE, UserRole.Attendee.getRole());
        bundle.putInt(Attendee_AddEventActivity.EXTRA_SELECTED_CLASS_INDEX, mViewPager.getCurrentItem());

        AndroidUtils.openActivity(this, Attendee_AddEventActivity.class, bundle, false);
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


    private void addClassButton() {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.EXTRA_USER_ROLE, UserRole.Attendee.getRole());
        AndroidUtils.openActivity(this, Attendee_AddEventActivity.class, bundle, false);
    }

    private void classNotificationLayout() {
        Event studentClass = user.getEventArrayList().get(mViewPager.getCurrentItem());
        sharedPreferences.edit().putInt(user.getUserId() + studentClass.getUniqueCode(), 0).apply();
        setNotificationBadge(mViewPager.getCurrentItem());

        Intent intent = new Intent(Attendee_DashboardActivity.this, Attendee_NotificationActivity.class);
        intent.putExtra(StudentNotificationActivity.EXTRA_STUDENT_CLASS, studentClass);
        startActivity(intent);
    }

    private void attendanceLayout() {
        Intent intent = new Intent(Attendee_DashboardActivity.this, EventAttendanceActivity.class);
        intent.putExtra(StudentCheckAttendanceActivity.EXTRA_STUDENT_CLASS, user.getEventArrayList().get(mViewPager.getCurrentItem()));
        intent.putExtra(StudentCheckAttendanceActivity.EXTRA_STUDENT, user);
        startActivity(intent);
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
            List<Event> teacherClasses = user.getEventArrayList();
            List<Event> teacher1Classes = user1.getEventArrayList();

            if (teacherClasses != null && teacher1Classes != null) {
                user.getEventArrayList().clear();
                user.getEventArrayList().addAll(user1.getEventArrayList());
                mStudentPagerAdapter.notifyDataSetChanged();
                setOneWordTextView(0);
                setNotificationBadge(0);
            } else {
                setNotificationBadge(mViewPager.getCurrentItem());
            }
        }

        new NavigationPage(this, userUtils.getUserFromSharedPrefs());

        updateDataAsync();
    }

    private void updateDataAsync() {
        new AsyncTask<Void, Void, Void>() {
            private String result;

            @Override
            protected Void doInBackground(Void... params) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("id", user.getUserId());
                hm.put("role", String.valueOf(UserRole.Attendee.getRole()));
                try {
                    result = new WebUtils().post(AppConstants.URL_GET_DATA_BY_ID, hm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (result != null) {

                    List<Event> teacherClasses = DataUtils.getAttendeeFromJsonString(result).getEventArrayList();

                    if (user.getEventArrayList().size() != teacherClasses.size()) {

                        user.getEventArrayList().clear();
                        user.getEventArrayList().addAll(teacherClasses);

//                        sharedPreferences.edit().putString(AppConstants.KEY_LOGGED_IN_USER_DATA, new Gson().toJson(user, Student.class)).apply();
                        userUtils.saveUserToSharedPrefs(user);

                        mStudentPagerAdapter.notifyDataSetChanged();
                        setOneWordTextView(0);
                        setNotificationBadge(0);
                    }

                }
            }
        }.execute();
    }

    OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener() {
        public boolean onSwipeRight() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            setNotificationBadge(mViewPager.getCurrentItem());
            setOneWordTextView(mViewPager.getCurrentItem());
            return true;
        }

        public boolean onSwipeLeft() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            setNotificationBadge(mViewPager.getCurrentItem());
            setOneWordTextView(mViewPager.getCurrentItem());
            return true;
        }

    };

}
