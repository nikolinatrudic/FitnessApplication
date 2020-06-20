package com.example.fitnessapplication.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessapplication.AlarmReceiver;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.singleton.LoggedInUser;

import java.time.LocalDate;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class StepCounterFragment extends Fragment {

    public static TextView stepsNumber;
    private Button mapsButton;
    private Button statsButton;

    private AlarmManager alarmManager;
    private SensorManager sensorManager;
    private Sensor sensor;

    private double magnitudePrevious = 0;
    public static Integer stepCount = 0;

    private SensorEventListener stepDetector;


    private static final int RQ_SYNC_SERVICE = 1101;

    public StepCounterFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        alarmManager = stepAlarmManager();
        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];


                    double magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
                    double magnitudeDelta = magnitude - magnitudePrevious;
                    magnitudePrevious = magnitude;

                    if (magnitudeDelta > 6) {
                        stepCount++;
                    }
                    stepsNumber.setText(stepCount.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL, 10000);
    }

    private AlarmManager stepAlarmManager(){
        AlarmManager mAlarmManger = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //Create pending intent & register it to your alarm notifier class
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.setAction("com.app.ACTION_ONE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), RQ_SYNC_SERVICE, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 30);

        mAlarmManger.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        return mAlarmManger;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);

        stepsNumber = (TextView) view.findViewById(R.id.stepsNumber);

        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
        stepsNumber.setText(stepCount.toString());

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        //sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL, 10000);
        /*SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();*/
    }

    @Override
    public void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(stepDetector);
        /*SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LoggedInUser.getInstance().setUser(null);

        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged_in_user_username", null);
        editor.apply();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        StartPageFragment startPageFragment = new StartPageFragment();
        fragmentTransaction.replace(R.id.frameLayout, startPageFragment);
        fragmentTransaction.commit();

        return super.onOptionsItemSelected(item);
    }
}