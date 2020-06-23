package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.proxy.ProxyImage;
import com.example.fitnessapplication.database.LoggedInUser;

public class ProfileFragment extends Fragment {

    private ImageView imageProfile;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private Button buttonEdit;

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

        return view;
    }
}
