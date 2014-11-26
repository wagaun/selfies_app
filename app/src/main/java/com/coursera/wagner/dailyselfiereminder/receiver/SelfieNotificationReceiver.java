package com.coursera.wagner.dailyselfiereminder.receiver;

import java.text.DateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.coursera.wagner.dailyselfiereminder.DailySelfieActivity;
import com.coursera.wagner.dailyselfiereminder.R;

public class SelfieNotificationReceiver  extends BroadcastReceiver {
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 42;
	private static final String TAG = "AlarmNotificationReceiver";

	// Notification Text Elements
	private final CharSequence contentText = "Time for another selfie";

	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;

	@Override
	public void onReceive(Context context, Intent intent) {

		// The Intent to be used when the user clicks on the Notification View
		mNotificationIntent = new Intent(context, DailySelfieActivity.class);

		// The PendingIntent that wraps the underlying Intent
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, PendingIntent.FLAG_ONE_SHOT);

		// Build the Notification
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker("Daily Selfie")
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle("Daily Selfie")
				.setContentText(contentText).setContentIntent(mContentIntent)
                .setSmallIcon(R.drawable.ic_menu_camera);

		// Get the NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Pass the Notification to the NotificationManager:
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());

		// Log occurence of notify() call
		Log.i(TAG, "Sending notification at:"
				+ DateFormat.getDateTimeInstance().format(new Date()));

	}
}
