package com.example.fitnessapplication.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitnessapplication.database.AlarmReceiver;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.dao.SportDao;
import com.example.fitnessapplication.database.entities.Forum;
import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.menu.DrawerLocker;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.LoggedInUser;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class StepCounterFragment extends Fragment {

    public static TextView stepsNumber;
    private TextView text33;
    private TextView text_username;
    private TextView text_email;

    private Button skiBtn;
    private Button hikeBtn;
    private Button runBtn;
    private Button bordBtn;

    private ProgressBar progressBar;

    private AlarmManager alarmManager;
    private SensorManager sensorManager;
    private Sensor sensor;

    private double magnitudePrevious = 0;
    public static Integer stepCount = 0;

    private SensorEventListener stepDetector;
    private HorizontalScrollView horizontalScrollView;

    private DrawerLocker dl;

    private String type;


    private static final int RQ_SYNC_SERVICE = 1101;

    public StepCounterFragment() {
        // Required empty public constructor
    }

    public static class Builder {
        private final Bundle bundle;

        public Builder() {
            bundle = new Bundle();
        }

        public Builder setType(String type) {
            bundle.putString("type", type);
            return this;
        }

        public StepCounterFragment build() {
            StepCounterFragment fragment = new StepCounterFragment();
            fragment.setArguments(bundle);

            return fragment;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

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
                    progressBar.setProgress(calculateProgress(stepCount));
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

//        type = getArguments().getString("type");
        
        dl =  (DrawerLocker) getActivity();
        NavigationView nav = getActivity().findViewById(R.id.nav_view);

        View header_view = (View) nav.getHeaderView(0);

        text_username = (TextView) header_view.findViewById(R.id.text_username);// Ovde username bude null kad se menja u edit profile, problem!
        text_email = (TextView) header_view.findViewById(R.id.text_email);

        stepsNumber = (TextView) view.findViewById(R.id.stepsNumber);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
        progressBar.setProgress(calculateProgress(stepCount));
        stepsNumber.setText(stepCount.toString());

        text33 = view.findViewById(R.id.text33);

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);

        skiBtn = (Button) view.findViewById(R.id.skibtn);
        skiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSport("Ski");
            }
        });
        hikeBtn =(Button) view.findViewById(R.id.hikebtn);
        hikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSport("Climb");
            }
        });
        runBtn = (Button) view.findViewById(R.id.runbtn);
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forum forumRun = new Forum("Forum run");
                FitnessDatabase.getInstance(getContext()).forumDao().insertForum(forumRun);
                Sport sportRun = new Sport("Run", 50);
                sportRun.setForumId(forumRun.getForumId());
                FitnessDatabase.getInstance(getContext()).sportDao().insertSport(sportRun);
               // openSport("Run");

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SportPage sportPageFragmet = new SportPage();
                //SportDao sportDao= FitnessDatabase.getInstance(getContext()).sportDao();
                Log.d("Sport name",sportRun.getName());
                sportPageFragmet.setSport(sportRun);
                fragmentTransaction.replace(R.id.fragment_container, sportPageFragmet);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        bordBtn= (Button) view.findViewById(R.id.bordbtn);
        bordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSport("Skate");
            }
        });

    /*    dl.setDrawerEnabled(type.equals("guest") ? false  : true);
        text33.setVisibility(type.equals("guest") ? View.GONE  : View.VISIBLE);
        horizontalScrollView.setVisibility(type.equals("guest") ? View.GONE  : View.VISIBLE);
        skiBtn.setVisibility(type.equals("guest") ? View.GONE  : View.VISIBLE);
        hikeBtn.setVisibility(type.equals("guest") ? View.GONE  : View.VISIBLE);
        runBtn.setVisibility(type.equals("guest") ? View.GONE  : View.VISIBLE);
        bordBtn.setVisibility(type.equals("guest") ? View.GONE  : View.VISIBLE); */

     //   if(type.equals("user")) {
        if (LoggedInUser.getInstance().getUser() != null) {
            text_username.setText(LoggedInUser.getInstance().getUser().getUsername());
            text_email.setText(LoggedInUser.getInstance().getUser().getEmail());
        }
    //    }
        return view;
    }
    // SPORT FRAGMENT
    private void openSport(String sport){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SportPage sportPageFragmet = new SportPage();
        SportDao sportDao= FitnessDatabase.getInstance(getContext()).sportDao();
        Sport spor=sportDao.findSport(sport);
        Log.d("Sport name",spor.getName());
        sportPageFragmet.setSport(spor);
        fragmentTransaction.replace(R.id.fragment_container, sportPageFragmet);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        //sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL, 10000);
        /*SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);*/
    }
    private int calculateProgress(int steps){
        return steps/60;
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



    /*@Override
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
        fragmentTransaction.replace(R.id.fragment_container, startPageFragment);
        fragmentTransaction.commit();

        return super.onOptionsItemSelected(item);
    }*/
}