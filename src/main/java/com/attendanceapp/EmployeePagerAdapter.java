package com.attendanceapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.attendanceapp.models.EmployeeClass;
import com.attendanceapp.models.StudentClass;

import java.util.List;

/**
 * Created by aakash on 09/09/15.
 */
public class EmployeePagerAdapter extends FragmentPagerAdapter {

    List<EmployeeClass> studentClassList;

    public EmployeePagerAdapter(FragmentManager supportFragmentManager, List<EmployeeClass> studentClassList) {
        super(supportFragmentManager);
        this.studentClassList = studentClassList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new EmployeeClassViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EmployeeClassViewFragment.EXTRA_STUDENT_CLASS, studentClassList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return studentClassList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String className = " ";
        className = studentClassList.get(position).getMeetingName();
        if (className.length() == 1) {
            className = className.toUpperCase();
        } else {
            className = (className.substring(0, 1).toUpperCase()) + className.substring(1);
        }
        return className;
    }
}
