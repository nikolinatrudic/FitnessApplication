package com.example.fitnessapplication.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.proxy.ProxyImage;
import com.example.fitnessapplication.proxy.RealImage;
import com.example.fitnessapplication.singleton.LoggedInUser;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

    private ImageView imageProfile;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewHeight;
    private TextView textViewWeight;

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

        return view;
    }
}
