package com.attendanceapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.attendanceapp.activities.Attendee_DashboardActivity;
import com.attendanceapp.activities.Employee_DashboardActivity;
import com.attendanceapp.activities.EventHost_DashboardActivity;
import com.attendanceapp.activities.Manager_DashboardActivity;
import com.attendanceapp.models.User;
import com.attendanceapp.utils.AndroidUtils;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;

import static com.attendanceapp.AppConstants.SENDER_ID;
import static com.attendanceapp.utils.AndroidUtils.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.e(TAG, "Device registered: regId = " + registrationId);

        String userString = AndroidUtils.getCommonSharedPrefs(getApplicationContext()).getString(AppConstants.KEY_LOGGED_IN_USER, null);
        User user = new Gson().fromJson(userString, User.class);
        ServerUtilities.register(context, registrationId, user.getUserId(), user.getDeviceToken());
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String message;
        if (bundle != null && bundle.containsKey("classCode")) {
            String classCode = bundle.getString("classCode");
            SharedPreferences sp = AndroidUtils.getCommonSharedPrefs(context);
            String userString = sp.getString(AppConstants.KEY_LOGGED_IN_USER, null);
            if (userString == null) {
                return;
            }
            User user = new Gson().fromJson(userString, User.class);

            int count = sp.getInt(user.getUserId() + classCode, 0);
            sp.edit().putInt(user.getUserId() + classCode, ((count + 1))).apply();
        }

        if (AndroidUtils.getCommonSharedPrefs(context).getBoolean(AppConstants.IS_NOTIFICATIONS_ON, true)) {

            if (bundle != null && bundle.containsKey("m")) {
                message = bundle.getString("m");
                displayMessage(context, message);
                generateNotification(context, message, bundle);
            }
        }

//        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
//         String message = intent.getExtras().getString("price");
//         notifies user
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        generateNotification(context, message, null);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, Bundle bundle) {
        String title = context.getString(R.string.app_name);
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        Intent notificationIntent = new Intent(context, StudentNotificationActivity.class);

        // check for logged in user
        SharedPreferences sharedPreferences = AndroidUtils.getCommonSharedPrefs(context);
        String userString = sharedPreferences.getString(AppConstants.KEY_LOGGED_IN_USER, null);


        if (userString != null) {

            User user = new Gson().fromJson(userString, User.class);
            String userRole = String.valueOf(user.getUserRoles().get(0).getRole());

            if ("1".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, TeacherDashboardActivity.class);
            } else if ("2".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, StudentDashboardActivity.class);
            } else if ("3".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, ParentDashboardActivity.class);
            } else if ("4".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, Manager_DashboardActivity.class);
            } else if ("5".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, Employee_DashboardActivity.class);
            } else if ("6".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, EventHost_DashboardActivity.class);
            } else if ("7".equalsIgnoreCase(userRole)) {
                notificationIntent = new Intent(context, Attendee_DashboardActivity.class);
            }
        }

        if (bundle != null) {
            notificationIntent.putExtras(bundle);
        }

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);

    }

}
