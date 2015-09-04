package com.attendanceapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attendanceapp.models.AttendeeEvent;
import com.attendanceapp.models.Student;

import java.util.List;

public class AttendeePagerAdapter  extends FragmentPagerAdapter {
    List<AttendeeEvent> students;

    public AttendeePagerAdapter(FragmentManager supportFragmentManager, List<AttendeeEvent> students) {
        super(supportFragmentManager);
        this.students = students;
    }

    @Override
    public Fragment getItem(int position) {
        return new DummySectionFragment();
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String className = " ";
        className = students.get(position).getEventName();
        return className;
    }

    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            return inflater.inflate(android.R.layout.test_list_item,container, false);
        }
    }
}
