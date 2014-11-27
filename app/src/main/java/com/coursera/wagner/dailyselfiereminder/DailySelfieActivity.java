package com.coursera.wagner.dailyselfiereminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.coursera.wagner.dailyselfiereminder.receiver.SelfieNotificationReceiver;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class DailySelfieActivity extends ActionBarActivity {

    private ImageDataManager mImageDataManager;

    private SelfiesListFragment mListFragment;

    // 2 * 60 * 1000 = 2MIN
    private final static int ALARM_INTERVAL = 120000;

    private AlarmManager mAlarmManager;

    private PendingIntent mAlarmIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.main_frame);
        setContentView(frame, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        try {
            mImageDataManager = new ImageDataManager();
        } catch(IOException e) {
            Log.e(DailySelfieActivity.class.getCanonicalName(), "Error initializing image file parameters", e);
            return;
        }

        if (savedInstanceState == null) {
            mListFragment = new SelfiesListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.main_frame, mListFragment).commit();
        }
        createAlarm();
    }

    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create action menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    // Process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.picture:
                dispatchTakePictureIntent();
                return true;
            case R.id.disable_alarm:
                mAlarmManager.cancel(mAlarmIntent);
                Toast.makeText(this, "Alarm Disabled", Toast.LENGTH_LONG).show();
                return true;
            case R.id.enable_alarm:
                enableAlarm(mAlarmIntent);
                Toast.makeText(this, "Alarm Enabled", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = new File(mImageDataManager.getDirectory(), mImageDataManager.createFileName());
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void createAlarm() {
        // Create an Intent to broadcast to the AlarmNotificationReceiver
        Intent mNotificationReceiverIntent = new Intent(this, SelfieNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mAlarmIntent = PendingIntent.getBroadcast(
                this, 0, mNotificationReceiverIntent, 0);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Create a repeating alarm
        enableAlarm(mAlarmIntent);
    }

    private void enableAlarm(PendingIntent alarmIntent) {
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis() + ALARM_INTERVAL, ALARM_INTERVAL, alarmIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK) {
                SelfiesArrayAdapter adapter = new SelfiesArrayAdapter(this, mImageDataManager.getFileList());
                mListFragment.setListAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Selfie saved successfuly", Toast.LENGTH_LONG).show();
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Selfie capture canceled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
