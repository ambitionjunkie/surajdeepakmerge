package com.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendanceapp.models.EmployeeClass;
import com.attendanceapp.models.StudentClass;

/**
 * Created by aakash on 09/09/15.
 */
public class EmployeeClassViewFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_STUDENT_CLASS = "student_class";
    EmployeeClass studentClass;
    protected TextView notificationBtn;

    public EmployeeClassViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentClass = (EmployeeClass) getArguments().getSerializable(EXTRA_STUDENT_CLASS);

        View view = inflater.inflate(R.layout.fragment_main_student, container, false);

        notificationBtn = (TextView) view.findViewById(R.id.notificationBtn);

        notificationBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notificationBtn:
                notificationBtn();
                break;
        }
    }

    private void notificationBtn(){
        Intent intent =new Intent(getActivity(), StudentNotificationActivity.class);
        intent.putExtra(StudentNotificationActivity.EXTRA_STUDENT_CLASS, studentClass);
        startActivity(intent);
    }

}
