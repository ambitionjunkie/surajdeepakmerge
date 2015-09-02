package com.attendanceapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.attendanceapp.AppConstants;
import com.attendanceapp.LoginActivity;
import com.attendanceapp.R;
import com.attendanceapp.models.User;
import com.attendanceapp.utils.AndroidUtils;
import com.attendanceapp.utils.WebUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Attendee_AddEventActivity extends Activity {
    private static final String TAG = Attendee_AddEventActivity.class.getSimpleName();
    public static final String EXTRA_IS_FIRST_TIME = AppConstants.EXTRA_IS_FIRST_TIME;

    EditText eventCodeEditText, eventNameEditText;
    Button done, skip;

    ImageView saveButton;
    TextView addAnotherEvent;
    LayoutInflater layoutInflater;
    SharedPreferences sharedPreferences;
    protected Gson gson = new Gson();

    private User user;
    protected boolean isFirstTime;
    private boolean isEventAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_add_event);

        sharedPreferences = AndroidUtils.getCommonSharedPrefs(getApplicationContext());
        String userJson = sharedPreferences.getString(AppConstants.KEY_LOGGED_IN_USER, null);

        if (userJson != null) {
            user = gson.fromJson(userJson, User.class);
        } else {
            startActivity(new Intent(Attendee_AddEventActivity.this, LoginActivity.class));
            finish();
        }

        isFirstTime = getIntent().getBooleanExtra(EXTRA_IS_FIRST_TIME, false);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        eventNameEditText = (EditText) findViewById(R.id.eventName);
        eventCodeEditText = (EditText) findViewById(R.id.eventCode);
        saveButton = (ImageView) findViewById(R.id.saveButton);
        addAnotherEvent = (TextView) findViewById(R.id.addAnotherEvent);
        skip = (Button) findViewById(R.id.skip);
        done = (Button) findViewById(R.id.done);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> keysAndValues = new HashMap<>();
                keysAndValues.put("event_code", eventCodeEditText.getText().toString().trim());
                keysAndValues.put("event_name", eventNameEditText.getText().toString().trim());
                keysAndValues.put("event_userid", user.getUserId());
                keysAndValues.put("status", "1");

                // finally upload data to server using async task
                uploadDataAsync(AppConstants.KR_ADD_EVENT_BY_ATTENDEE, keysAndValues);
            }
        });

        addAnotherEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventNameEditText.setText("");
                eventCodeEditText.setText("");
            }
        });

        if (isFirstTime) {
            skip.setVisibility(View.VISIBLE);
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
                            makeToast("Event is saved!");
                            isEventAdded = true;
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

    private void makeToast(String title) {
        Toast.makeText(Attendee_AddEventActivity.this, title, Toast.LENGTH_LONG).show();
    }

    public void gotoBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isFirstTime || isEventAdded) {
            updateDataAsync();
            return;
        }
        startActivity(new Intent(Attendee_AddEventActivity.this, Attendee_DashboardActivity.class));
        finish();
    }


    private void updateDataAsync() {

        new AsyncTask<Void, Void, String>() {
            private ProgressDialog progressDialog = new ProgressDialog(Attendee_AddEventActivity.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hm = new HashMap<>();
//                hm.put("id", user.getUserId());
//                hm.put("role", user.getRole());
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
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        if (!jsonObject.has("Error")) {
//                            sharedPreferences.edit().putString(AppConstants.KEY_LOGGED_IN_USER_COMPLETE_JSON, result).apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                    }
                }
                progressDialog.dismiss();

                startActivity(new Intent(Attendee_AddEventActivity.this, Attendee_DashboardActivity.class));
                finish();


            }
        }.execute();
    }

}
