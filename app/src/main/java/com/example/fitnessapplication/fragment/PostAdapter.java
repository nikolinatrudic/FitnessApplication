package com.example.fitnessapplication.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.entities.Post;

import java.util.List;

public class PostAdapter extends ArrayAdapter {

    public List<Post> posts;

    public PostAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public List<Post> getItems() {
        return posts;
    }

    public void setItems(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView line;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.one_post, parent, false);
        }
        line = (TextView) convertView.findViewById(R.id.textViewItem);
        line.setText(posts.get(position).getText());

        return convertView;
    }

}
