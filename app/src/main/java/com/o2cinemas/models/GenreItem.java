package com.o2cinemas.models;

/**
 * Created by admin on 9/10/16.
 */
public class GenreItem {
    String id;
    String name;

    public GenreItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
