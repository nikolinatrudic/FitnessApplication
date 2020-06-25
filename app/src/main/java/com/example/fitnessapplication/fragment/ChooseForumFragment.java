package com.example.fitnessapplication.fragment;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.entities.Forum;

import java.util.ArrayList;
import java.util.List;

public class ChooseForumFragment extends Fragment {

    private ListView listView;
    private List<ItemForum> itemForums;
    private ItemForumAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_forum, container, false);

        listView = view.findViewById(R.id.listForums);
        adapter = new ItemForumAdapter(getContext(), 0);
        List<Forum> forums = FitnessDatabase.getInstance(getContext()).forumDao().getForums();
        itemForums = new ArrayList<>();
        for (Forum f : forums) {
            ItemForum newItem = new ItemForum();
            newItem.setName(f.getName());
            itemForums.add(newItem);
        }

        adapter.setItems(itemForums);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TextView tv = view.findViewById(R.id.textViewItemForum);
                String selected = tv.getText().toString();
                Log.e("msg", "Selected: " + selected);

                ForumFragment forumFragment = new ForumFragment();
                forumFragment.setSport(FitnessDatabase.getInstance(getContext()).sportDao().findSport(selected));

                Forum forum = FitnessDatabase.getInstance(getContext()).forumDao().findForum(selected);
                forumFragment.setForum(forum);

                fragmentTransaction.replace(R.id.fragment_container, forumFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return view;
    }
}
