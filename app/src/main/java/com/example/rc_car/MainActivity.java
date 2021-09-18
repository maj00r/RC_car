package com.example.rc_car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

public class MainActivity extends AppCompatActivity {
    VerticalSeekBar mThrottle;
    ArcSeekBar mWheel;
    RadioGroup mDriveObey;
    Controller mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mController = new Controller();
        initializeDrivingObey();
        initializeThrottle();
        initializeSteeringWheel();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.settings_button) {
            Intent intent = new Intent();
            intent.setClassName(this, "com.example.rc_car.SettingsActivity");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeDrivingObey() {
        mDriveObey = findViewById(R.id.radioGroup);
        mDriveObey.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.D_obey:
                    mController.setmDriveObeyValue('D');
                    break;
                case R.id.R_obey:
                    mController.setmDriveObeyValue('R');
                    break;
                default:
                    mController.setmDriveObeyValue('N');
                    break;
            }
            mController.updateControlState();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String ip = sharedPreferences.getString("et_ip", "192.168.0.1");
        int port = Integer.parseInt(sharedPreferences.getString("et_port", "8080"));
        int updateDelay = Integer.parseInt(sharedPreferences.getString("et_pause", "200"));
        if (updateDelay < 1){
            updateDelay = 10;
        }
        mController.startServer(ip, port, updateDelay);
        mController.updateControlState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.stopServer();

    }

    private void initializeThrottle() {
        mThrottle = findViewById(R.id.throttle);
        mThrottle.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mController.setmThrottleValue((int)(seekBar.getProgress() * 2.55 ));
                mController.updateControlState();
                Log.d("rc", String.valueOf((int)(seekBar.getProgress() * 2.55 )));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                seekBar.setProgress(0);
                mController.setmThrottleValue(0);
                mController.updateControlState();
            }
        });
    }

    private void initializeSteeringWheel() {
        mWheel = findViewById(R.id.turn_angle);
        // arc seek bar in range [0 ; 100] to range [-100 ; 100]
        ProgressListener progressListener = progress -> {
            mController.setmTurnValue(mWheel.getProgress() - 255);
            mController.updateControlState();
            Log.d("rc", String.valueOf(mWheel.getProgress() - 255));
        };
        progressListener.invoke(0);
        ProgressListener stoppedListener =  progress ->
        {
            mWheel.setProgress(255);
            mController.setmTurnValue(0);
            mController.updateControlState();
        };
        mWheel.setOnProgressChangedListener(progressListener);
        mWheel.setOnStopTrackingTouch(stoppedListener);
    }
   
}

