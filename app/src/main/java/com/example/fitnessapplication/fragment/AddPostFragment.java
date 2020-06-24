package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.entities.Forum;
import com.example.fitnessapplication.database.entities.Post;
import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.User;

public class AddPostFragment extends Fragment {

    private EditText textArea;
    private Button buttonPost;
    private EditText editTextHeading;

    private Forum forum;
    private Sport sport;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_post, container, false);

        textArea = view.findViewById(R.id.textAreaNewPost);
        buttonPost = view.findViewById(R.id.buttonAdd);
        editTextHeading = view.findViewById(R.id.editTextHeading);

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post newPost = new Post();
                newPost.setUserId(user.getId());
                newPost.setForumId(forum.getForumId());
                newPost.setText(textArea.getText().toString());
                newPost.setHeading(editTextHeading.getText().toString());
                FitnessDatabase.getInstance(getContext()).postDao().insertPost(newPost);

            }
        });



        return view;
    }

    public void setSport(Sport sport) {
        if (sport != null) {
            this.sport = sport;
        }
    }

    public void setForum(Forum forum) {
        if (forum != null) {
            this.forum = forum;
        }
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }
}
