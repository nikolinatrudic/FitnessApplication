package com.example.fitnessapplication.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fitnessapplication.R;

import java.util.List;

public class ItemForumAdapter extends ArrayAdapter {

    private List<ItemForum> list;

    public ItemForumAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public List<ItemForum> getItems() {
        return list;
    }

    public void setItems(List<ItemForum> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView line;
        TextView line_heading;
        TextView line_username;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_forum, parent, false);
        }
        line = (TextView) convertView.findViewById(R.id.textViewItemForum);

        line.setText(list.get(position).getName());


        return convertView;
    }
}
