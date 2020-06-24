package com.example.fitnessapplication.fragment;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapplication.BuildConfig;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.dao.SportDao;
import com.example.fitnessapplication.database.dao.WorkoutDao;
import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.database.entities.Workout;
import com.example.fitnessapplication.imagesproxy.ProxyImage;
import com.example.fitnessapplication.database.LoggedInUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView imageProfile;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private Button buttonEdit;
    private Button generateStats;

    private static final int STORAGE_PERMISSION_CODE = 101;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageProfile = view.findViewById(R.id.imageView);
        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewHeight = view.findViewById(R.id.textViewHeight);
        textViewWeight = view.findViewById(R.id.textViewWeight);

        if (LoggedInUser.getInstance().getUser() != null) {
            user = LoggedInUser.getInstance().getUser();

            ProxyImage proxyImage = new ProxyImage(getResources().getDrawable(R.drawable.profile), getResources().getDrawable(R.drawable.profilegirl), user.getGender(), imageProfile);
            proxyImage.display();


            textViewUsername.setText(user.getUsername());
            textViewEmail.setText(user.getEmail());
            textViewHeight.setText(user.getHeight() + " cm");
            textViewWeight.setText(user.getWeight() + " kg");

            WorkoutDao workoutDao=FitnessDatabase.getInstance(getContext()).workoutDao();
            SportDao sportDao=FitnessDatabase.getInstance(getContext()).sportDao();
            List<Workout> workouts=workoutDao.findWorkoutByUserId(user.getId());
            if(workouts==null||workouts.size()==0){
                Sport s=sportDao.findSport("Run");
                int uid=user.getId();
                Workout w=new Workout();
                w.setKm(23);
                w.setSportId(s.getSportId());
                w.setUserId(uid);
                workoutDao.insertWorkout(w);

                Workout w1=new Workout();
                w1.setKm(33);
                w1.setSportId(s.getSportId());
                w1.setUserId(uid);
                workoutDao.insertWorkout(w1);
                Log.d("Workout add",w1.getKm()+"");
            }
        }

        buttonEdit = view.findViewById(R.id.buttonEditProfile);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                EditProfileFragment editProfileFragment = new EditProfileFragment();
                fragmentTransaction.replace(R.id.fragment_container, editProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        generateStats = view.findViewById(R.id.genStats);
        generateStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                    return;
                }
                generateStatistics();
            }
        });
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateStatistics();
            }
            else {
                Toast.makeText(getActivity(),"Storage Permission Denied",   Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void generateStatistics() {
        //String text = mEditText.getText().toString();
        int uid=user.getId();
        WorkoutDao workoutDao=FitnessDatabase.getInstance(getContext()).workoutDao();
        SportDao sportDao=FitnessDatabase.getInstance(getContext()).sportDao();
        List<Workout> listWorkout=workoutDao.findWorkoutByUserId(uid);

        StringBuilder builder=new StringBuilder();
        for (Workout w:listWorkout) {
            String sport=sportDao.findSportId(w.getSportId()).getName();
            builder.append("Sport"+ sport+", km"+w.getKm());
        }
        String text =builder.toString();

            File file = new File(getContext().getExternalFilesDir("text/plain"), "text");
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName =  timeStamp + ".txt";
                File gpxfile = new File(file, fileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(text);
                writer.flush();
                writer.close();
                Uri uri = Uri.parse(gpxfile.getAbsolutePath());
                openFile(uri);
               // Toast.makeText(getActivity(), "Saved your text at "+gpxfile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) { }


    }

    public void openFile(Uri uri){
                File f;
                f = new File(uri.getPath());
                if(f.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    //intent.setData(uri);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
                    Toast.makeText(getActivity(), "Saved your text at " + uri, Toast.LENGTH_LONG).show();
                    intent.setDataAndType(uri, "text/plain");
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


    }

}
