package com.example.fitnessapplication.iterator;

import com.example.fitnessapplication.database.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class PostCollection {

    private List<Post> posts;


    public PostCollection(List<Post> list) {
        this.posts = list;
    }

    public void add(Post post) {
        if (post != null) {
            posts.add(post);
        }
    }


    public List<Post> getPosts() {
        return posts;
    }

    public ListIterator createIterator() {
        return new ListIterator(posts);
    }

    class ListIterator implements IteratorInterface {

        private List<Post> list;
        private int index = 0;

        public ListIterator(List<Post> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
           return index < list.size();
        }

        @Override
        public Post next() {
            Post post = list.get(index);
            index++;
            return post;
        }
    }

}
