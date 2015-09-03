package com.attendanceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.attendanceapp.AppConstants;
import com.attendanceapp.OnSwipeTouchListener;
import com.attendanceapp.R;
import com.attendanceapp.activities.Attendee_AddEventActivity;
import com.attendanceapp.activities.MapActivity;
import com.attendanceapp.adapters.ParentPagerAdapter;
import com.attendanceapp.adapters.Parent_ChildClassExpendableListAdapter;
import com.attendanceapp.models.Attendance;
import com.attendanceapp.models.Attendee;
import com.attendanceapp.models.Event;
import com.attendanceapp.models.Parent;
import com.attendanceapp.models.Student;
import com.attendanceapp.models.User;
import com.attendanceapp.models.UserRole;
import com.attendanceapp.utils.DataUtils;
import com.attendanceapp.utils.NavigationPage;
import com.attendanceapp.utils.UserUtils;
import com.attendanceapp.utils.WebUtils;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Attendee_DashboardActivity extends FragmentActivity implements View.OnClickListener, NavigationPage.NavigationFunctions {

    private static final String TAG = Attendee_DashboardActivity.class.getSimpleName();
    private static final int REQUEST_EDIT_CHILD = 100;

    protected ImageView addEventLayout;
    protected static TextView listEmptyTextView, oneWordTextView;
    ProgressBar absentListProgress;
    protected LinearLayout  mainPage, absentLinearLayout;
    ExpandableListView absentListView;
    ViewPager mViewPager;
    private FrameLayout navigationLayout;

    Animation textAnimation;
    ParentPagerAdapter parentPagerAdapter;

    private Attendee attendee;
    private TreeMap<String, List<Attendance>> classAttendanceMap = new TreeMap<>();
    private Parent_ChildClassExpendableListAdapter listAdapter;

    UserUtils userUtils;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_dashboard);

        navigationLayout = (FrameLayout) findViewById(R.id.navigation);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        oneWordTextView = (TextView) findViewById(R.id.oneWordTitle);
        listEmptyTextView = (TextView) findViewById(R.id.listEmptyTextView);
        addEventLayout = (ImageView) findViewById(R.id.addEventLayout);
        mainPage = (LinearLayout) findViewById(R.id.mainPage);
        absentLinearLayout = (LinearLayout) findViewById(R.id.absentLinearLayout);
        absentListProgress = (ProgressBar) findViewById(R.id.absentListProgress);
        absentListView = (ExpandableListView) findViewById(R.id.absentListView);

        userUtils = new UserUtils(Attendee_DashboardActivity.this);
        User user = userUtils.getUserFromSharedPrefs();
        attendee = new Attendee(user);

        addEventLayout.setOnClickListener(this);

        absentListView.setOnTouchListener(swipeTouchListener);
        absentLinearLayout.setOnTouchListener(swipeTouchListener);
        mainPage.setOnTouchListener(swipeTouchListener);
/*
        setParentPagerAdapter();
        setOneWordTextView(0);

        listAdapter = new Parent_ChildClassExpendableListAdapter(Attendee_DashboardActivity.this, classAttendanceMap);
        absentListView.setAdapter(listAdapter);

        if (parent.getStudentList().size() >= 1) {
            showAbsentList(parent.getStudentList().get(0));
        }


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setOneWordTextView(position);
            }
        });
*/
    }

    private void setOneWordTextView(int current) {
        oneWordTextView.setText("");
        if (attendee.getEventList().size() > current) {
//            oneWordTextView.setText(String.valueOf(attendee.getEventList().get(current).getUsername().charAt(0)).toUpperCase());
            showAbsentList(attendee.getEventList().get(current));
        }
        showMessageIfNoClass();
    }

    private void setParentPagerAdapter() {
//        parentPagerAdapter = new ParentPagerAdapter(getSupportFragmentManager(), attendee.getEventList());
        mViewPager.setAdapter(parentPagerAdapter);
    }

    private void showMessageIfNoClass() {
        absentLinearLayout.setVisibility(attendee.getEventList().size() == 0 ? View.GONE : View.VISIBLE);
        listEmptyTextView.setVisibility(attendee.getEventList().size() == 0 ? View.VISIBLE : View.GONE);

    }

    OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener() {
        public boolean onSwipeRight() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            changeTitle();
            return true;
        }

        public boolean onSwipeLeft() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            changeTitle();
            return true;
        }

        void changeTitle() {
            int current = mViewPager.getCurrentItem();
            //oneWordTextView.setText(String.valueOf(attendee.getEventList().get(current).getUsername().charAt(0)).toUpperCase());
            showAbsentList(attendee.getEventList().get(current));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Parent parent1 = userUtils.getUserWithDataFromSharedPrefs(Parent.class);

        if (parent1 != null) {
            List<Event> teacherClasses = attendee.getEventList();
            List<Student> teacher1Classes = parent1.getStudentList();

            if (teacherClasses != null && teacher1Classes != null && teacherClasses.size() != teacher1Classes.size()) {
                attendee.getEventList().clear();
                //attendee.getStudentList().addAll(parent1.getStudentList());
                parentPagerAdapter.notifyDataSetChanged();
                setOneWordTextView(0);
            }

        }

        new NavigationPage(this, userUtils.getUserFromSharedPrefs());
        updateDataAsync();
    }

    private void showAbsentList(final Event ev) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                absentListProgress.setVisibility(View.VISIBLE);
                absentListView.setVisibility(View.GONE);
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> map = new HashMap<>();
//                map.put("student_id", ev.getChildIdForLocation());
                try {
                    return new WebUtils().post(AppConstants.URL_PARENT_CHECK_CHILD_ATTENDANCE, map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                absentListProgress.setVisibility(View.GONE);
                absentListView.setVisibility(View.VISIBLE);

                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("Error")) {
                            makeToast(jsonObject.getString("Error"));
                            return;
                        }
                        classAttendanceMap = DataUtils.getClassAttendanceMap(result);
                        updateList();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                    }
                }
            }
        }.execute();
    }

    private void updateList() {
        listAdapter.update(classAttendanceMap);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addEventLayout:
                addEventLayout();
                break;
        }
    }

    private void editChildLayout() {
        Intent intent = new Intent(Attendee_DashboardActivity.this, Attendee_AddEventActivity.class);
/*        intent.putExtra(Attendee_AddEventActivity.EXTRA_PARENT, parent);
        intent.putExtra(Attendee_AddEventActivity.EXTRA_IS_EDIT_STUDENT, true);
        intent.putExtra(Attendee_AddEventActivity.EXTRA_STUDENT_NAME, parent.getStudentList().get(mViewPager.getCurrentItem()).getUsername());
        intent.putExtra(Attendee_AddEventActivity.EXTRA_STUDENT_ID, parent.getStudentList().get(mViewPager.getCurrentItem()).getUserId());*/

        startActivityForResult(intent, REQUEST_EDIT_CHILD);
    }

    private void addEventLayout() {
        Intent intent = new Intent(Attendee_DashboardActivity.this, Attendee_AddEventActivity.class);
        //intent.putExtra(Attendee_AddEventActivity.EXTRA_PARENT, parent);
        startActivity(intent);
    }

    private void makeToast(String title) {
        Toast.makeText(Attendee_DashboardActivity.this, title, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (navigationLayout.getVisibility() == View.VISIBLE) {
            textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_out);
            navigationLayout.setAnimation(textAnimation);
            navigationLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void gotoBack(View view) {
        onBackPressed();
    }

    private void updateDataAsync() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("id", attendee.getUserId());
                hm.put("role", String.valueOf(UserRole.Parent.getRole()));
                try {
                    return new WebUtils().post(AppConstants.URL_GET_DATA_BY_ID, hm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {

                    /*List<Event> teacherClasses = DataUtils.getParentChildrenListFromJsonString(result);

                    if (attendee.getEventList().size() != teacherClasses.size()) {

                        attendee.getEventList().clear();
                        attendee.getEventList().addAll(teacherClasses);

                        userUtils.saveUserWithDataToSharedPrefs(attendee, Parent.class);

                        parentPagerAdapter.notifyDataSetChanged();
                        setOneWordTextView(0);
                    }*/
                }
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_CHILD) {
            if (resultCode == RESULT_OK) {
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("id", attendee.getUserId());
                        hm.put("role", String.valueOf(UserRole.Parent.getRole()));
                        try {
                            return new WebUtils().post(AppConstants.URL_GET_DATA_BY_ID, hm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (result != null) {

                            /*List<Student> teacherClasses = DataUtils.getParentChildrenListFromJsonString(result);

                            attendee.getEventList().clear();
                            attendee.getEventList().addAll(teacherClasses);

                            userUtils.saveUserWithDataToSharedPrefs(parent, Parent.class);
*/
                            parentPagerAdapter.notifyDataSetChanged();
                            setOneWordTextView(0);
                        }
                    }
                }.execute();
            }
        }
    }
}