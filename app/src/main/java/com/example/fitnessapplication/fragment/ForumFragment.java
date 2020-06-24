package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.LoggedInUser;
import com.example.fitnessapplication.database.entities.Forum;
import com.example.fitnessapplication.database.entities.Post;
import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.User;

import java.util.List;

public class ForumFragment extends Fragment {

    private Button buttonAddPost;
    private ListView listViewPosts;
    private TextView textViewTitle;

    private Sport sport;
    private Forum forum;
    private User activeUser;

    private PostAdapter adapter;

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
        activeUser = LoggedInUser.getInstance().getUser();
        adapter = new PostAdapter(getContext(), 0);

        textViewTitle.setText("     FORUM - " + sport.getName());

        List<Post> postsList = FitnessDatabase.getInstance(getContext()).postDao().getAllPosts();
        Log.e("msg", "Number of posts: " + postsList.size());
        adapter.setItems(postsList);
        listViewPosts.setAdapter(adapter);

        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddPostFragment addPostFragment = new AddPostFragment();
                addPostFragment.setForum(forum);
                addPostFragment.setSport(sport);
                addPostFragment.setUser(activeUser);
                fragmentTransaction.replace(R.id.fragment_container, addPostFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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
}
