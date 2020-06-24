package com.example.fitnessapplication.iterator;

import com.example.fitnessapplication.database.entities.Post;

public interface IteratorInterface {

    boolean hasNext();

    Post next();

}
