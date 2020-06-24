package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.imagesproxy.ProxyImage;
import com.example.fitnessapplication.database.LoggedInUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private ImageView imageProfile;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private Button buttonEdit;
    private Button generateStats;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
            User user = LoggedInUser.getInstance().getUser();

            ProxyImage proxyImage = new ProxyImage(getResources().getDrawable(R.drawable.profile), getResources().getDrawable(R.drawable.profilegirl), user.getGender(), imageProfile);
            proxyImage.display();

            textViewUsername.setText(user.getUsername());
            textViewEmail.setText(user.getEmail());
            textViewHeight.setText(user.getHeight() + " cm");
            textViewWeight.setText(user.getWeight() + " kg");

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
                generateStatistics();
            }
        });
        return view;
    }

    private void generateStatistics() {
        String text = "OVO JE PROBA";
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
// this will create a new name everytime and unique
            File root = new File(Environment.getExternalStorageDirectory(), "FitnessApplication");
// if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, timeStamp + ".txt"); // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(text);
            writer.flush();
            writer.close();
            String m = "File generated with name " + timeStamp + ".txt";
            Toast.makeText(getActivity(),m, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "GRESKA", Toast.LENGTH_SHORT).show();
        }

    }
}
