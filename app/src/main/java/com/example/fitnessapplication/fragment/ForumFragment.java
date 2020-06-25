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
import com.example.fitnessapplication.iterator.IteratorInterface;
import com.example.fitnessapplication.iterator.PostCollection;

import java.util.ArrayList;
import java.util.List;

public class ForumFragment extends Fragment {

    private Button buttonAddPost;
    private ListView listViewPosts;
    private TextView textViewTitle;

    private Sport sport;
    private Forum forum;
    private User activeUser;

    private PostAdapter adapter;
    private List<ItemPost> itemPosts;

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
        itemPosts = new ArrayList<>();

        textViewTitle.setText("Forum for " + sport.getName());

        //List<Post> postsList = FitnessDatabase.getInstance(getContext()).postDao().getAllPosts();
        PostCollection postCollection = new PostCollection(FitnessDatabase.getInstance(getContext()).postDao().getForumPosts(forum.getForumId()));
        Log.e("msg", "Number of posts: " + postCollection.getPosts().size());

        //adapter.setItems(postCollection.getPosts());

        IteratorInterface iterator = postCollection.createIterator();
        while (iterator.hasNext()) {
         //   Log.e("msg", "Post: " + iterator.next().getHeading());
            Post post = iterator.next();
            String username = FitnessDatabase.getInstance(getContext()).userDao().getUserById(post.getUserId()).getUsername();
            ItemPost itemPost = new ItemPost();
            itemPost.setHeading(post.getHeading());
            itemPost.setUsername(username);
            itemPost.setText(post.getText());
            itemPosts.add(itemPost);
        }
        adapter.setItems(itemPosts);
        listViewPosts.setAdapter(adapter);

        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddPostFragment addPostFragment = new AddPostFragment(activeUser,forum, sport);

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
