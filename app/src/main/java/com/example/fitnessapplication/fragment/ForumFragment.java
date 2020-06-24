package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.entities.Sport;

public class ForumFragment extends Fragment {

    private Button buttonAddPost;
    private ListView listViewPosts;
    private Sport sport;
    private TextView textViewTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        buttonAddPost = view.findViewById(R.id.buttonAddPost);
        textViewTitle = view.findViewById(R.id.textViewForum);
        listViewPosts = view.findViewById(R.id.list);


        textViewTitle.setText("     FORUM - " + sport.getName());


        return view;

    }

    public void setSport(Sport sport) {
        if (sport != null) {
            this.sport = sport;
        }
    }
}
