package com.attendanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.attendanceapp.activities.Attendee_AddEventActivity;
import com.attendanceapp.activities.Attendee_DashboardActivity;
import com.attendanceapp.activities.Employee_DashboardActivity;
import com.attendanceapp.models.ClassEventCompany;
import com.attendanceapp.models.Company;
import com.attendanceapp.models.Employee;
import com.attendanceapp.models.EmployeeClass;
import com.attendanceapp.models.UserRole;
import com.attendanceapp.utils.AndroidUtils;
import com.attendanceapp.utils.DataUtils;
import com.attendanceapp.utils.UserUtils;
import com.attendanceapp.utils.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aakash on 10/09/15.
 */
public class EmployeeAddMeetingActivity extends Activity {

    private static final String TAG = "StudentAddClassActivity";
    public static final String EXTRA_IS_FIRST_TIME = AppConstants.EXTRA_IS_FIRST_TIME;
    public static final String EXTRA_STUDENT_CLASS_INDEX = "EXTRA_STUDENT_CLASS_INDEX";

    EditText classCodeEditText, classNameEditText;
    Button done, skip;
    ImageView saveButton, imgHelp;
    TextView addAnotherClass;
    LayoutInflater layoutInflater;
    SharedPreferences sharedPreferences;

    private UserUtils userUtils;
    private Employee student;
    protected boolean isFirstTime;
    private boolean isClassAdded;
    private boolean isEditClass, isClassDeleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add_meeting);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPreferences = AndroidUtils.getCommonSharedPrefs(getApplicationContext());
        userUtils = new UserUtils(this);
        student = userUtils.getUserWithDataFromSharedPrefs(Employee.class);
        isFirstTime = getIntent().getBooleanExtra(EXTRA_IS_FIRST_TIME, false);

        classNameEditText = (EditText) findViewById(R.id.className);
        classCodeEditText = (EditText) findViewById(R.id.classCode);
        imgHelp = (ImageView) findViewById(R.id.imgHelp);
        saveButton = (ImageView) findViewById(R.id.saveButtons);
        addAnotherClass = (TextView) findViewById(R.id.addAnotherClass);
        skip = (Button) findViewById(R.id.skip);
        done = (Button) findViewById(R.id.done);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> keysAndValues = new HashMap<>();
                keysAndValues.put("company_code", classCodeEditText.getText().toString().trim());
                keysAndValues.put("company_name", classNameEditText.getText().toString().trim());
                keysAndValues.put("employee_email", student.getEmail());
                keysAndValues.put("user_id", student.getUserId());
                keysAndValues.put("status", "1");

                // finally upload data to server using async task
                uploadDataAsync(AppConstants.URL_ADD_CLASS_BY_STUDENT, keysAndValues);
            }
        });

        addAnotherClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classCodeEditText.setText("");
                classNameEditText.setText("");
            }
        });

        if (isFirstTime) {
            skip.setVisibility(View.VISIBLE);
        }

        int index = getIntent().getIntExtra(EXTRA_STUDENT_CLASS_INDEX, -1);

        if (index != -1) {
            if(userUtils.getUserWithDataFromSharedPrefs(Employee.class)==null) {
                System.out.println("Null vshdgs");
            }
           EmployeeClass teacherClass = student.getStudentClassList().get(index);
            isEditClass = true;

            classNameEditText.setText(teacherClass.getMeetingName());
            classCodeEditText.setText(teacherClass.getMeetingUniqueCode());
            classCodeEditText.setVisibility(View.GONE);
            addAnotherClass.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);

            done.setText("Save");

            /* delete button */
            imgHelp.setImageResource(R.drawable.delete);
            imgHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(EmployeeAddMeetingActivity.this)
                            .setTitle("Want to delete").setMessage("Press ok if you want to delete class")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Map<String, String> keysAndValues = new HashMap<>();
                                    keysAndValues.put("company_code", classCodeEditText.getText().toString().trim());
                                    keysAndValues.put("company_name", classNameEditText.getText().toString().trim());
                                    keysAndValues.put("employee_email", student.getEmail());
                                    keysAndValues.put("user_id", student.getUserId());
                                    keysAndValues.put("status", "1");

                                    // finally upload data to server using async task
                                    uploadDataAsync(AppConstants.URL_ADD_CLASS_BY_STUDENT, keysAndValues);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dialog.cancel();
                                }
                            }).create().show();
                }
            });
        }
    }


    private void uploadDataAsync(final String url, final Map<String, String> keysAndValues) {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    return new WebUtils().post(url, keysAndValues);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if (s == null) {
                    makeToast("Error in uploading data");
                } else {
                    try {
                        JSONObject jObject = new JSONObject(s);

                        // check if result contains Error
                        if (jObject.has("Error")) {
                            makeToast(jObject.getString("Error"));

                        } else {
                            if (isEditClass) {
                                makeToast("Class is deleted!");
                                isClassDeleted = true;
                            } else {
                                makeToast("Class is saved!");
                                isClassAdded = true;
                            }
                            onBackPressed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error in parsing data: " + s);
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }.execute();
    }
//
    private void makeToast(String title) {
        Toast.makeText(EmployeeAddMeetingActivity.this, title, Toast.LENGTH_LONG).show();
    }

    public void gotoBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isFirstTime || isClassAdded || isClassDeleted) {
            updateDataAsync();
            return;
        }
        if (!isEditClass) {
            startActivity(new Intent(EmployeeAddMeetingActivity.this, Employee_DashboardActivity.class));
        }

        finish();
    }


    private void updateDataAsync() {
        new AsyncTask<Void, Void, String>() {
            private ProgressDialog progressDialog = new ProgressDialog(EmployeeAddMeetingActivity.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("id", student.getUserId());
                hm.put("role", String.valueOf(UserRole.Student.getRole()));
                try {
                    return new WebUtils().post(AppConstants.URL_GET_DATA_BY_ID, hm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                progressDialog.dismiss();
                if (result != null) {

                    Employee student1 = new Employee(student);
                    List<EmployeeClass> teacherClasses = DataUtils.getMeetingListFromJsonString(result);

                    if (student1.getStudentClassList().size() != teacherClasses.size()) {

                        student1.getStudentClassList().clear();
                        student1.getStudentClassList().addAll(teacherClasses);

                        userUtils.saveUserWithDataToSharedPrefs(student, Employee.class);

                    }
                }
                startActivity(new Intent(EmployeeAddMeetingActivity.this, Employee_DashboardActivity.class));
                finish();
            }
        }.execute();

    }
}
