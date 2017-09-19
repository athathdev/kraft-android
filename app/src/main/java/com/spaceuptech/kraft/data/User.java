package com.spaceuptech.kraft.data;

import java.io.Serializable;

/**
 * Created by ubuntu on 19/9/17.
 */

public class User {
    public String id, name, pic;
    public User(String id, String name, String pic) {
        this.id = id;
        this.name = name;
        this.pic = pic;
    }
}
