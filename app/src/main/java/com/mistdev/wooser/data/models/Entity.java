package com.mistdev.wooser.data.models;

/**
 * Created by mcastro on 04/03/17.
 */

public class Entity {

    private long id;

    public Entity() {
    }

    public Entity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
