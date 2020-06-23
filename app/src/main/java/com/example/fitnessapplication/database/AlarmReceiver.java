package com.example.fitnessapplication.database;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.fitnessapplication.fragment.StepCounterFragment;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        LocalDate currentDate = LocalDate.now();
        double km = (double) StepCounterFragment.stepCount / (double) 1320;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        String stringKm = df.format(km);

        /*ActivityHistory ac = new ActivityHistory(currentDate.toString(), FirstFragment.stepCount, Double.parseDouble(stringKm));
        FirstFragment.database.activityHistoryDao().insertActivity(ac);

        FirstFragment.stepCount = 0;
        FirstFragment.stepsNumber.setText("0");*/

        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", 0);
    }
}